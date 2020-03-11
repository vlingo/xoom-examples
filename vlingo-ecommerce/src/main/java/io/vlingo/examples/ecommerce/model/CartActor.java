package io.vlingo.examples.ecommerce.model;

import io.vlingo.common.Completes;
import io.vlingo.lattice.model.sourcing.EventSourced;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

public class CartActor extends EventSourced implements Cart {

    private static final int SNAPSHOT_ONE_EVERY_N_TIMES = 10;
    private State state;

    public CartActor(final String cartId) {
        super(cartId);
        this.state = State.create(cartId);
    }

    @Override
    @SuppressWarnings("unchecked")
    public String snapshot() {
        if (nextVersion() % SNAPSHOT_ONE_EVERY_N_TIMES == 0) {
            return this.state.toString();
        } else {
            return null;
        }
    }

    static {
        BiConsumer<CartActor, CartEvents.CreatedForUser> applyCartCreated = CartActor::applyCartCreated;
        EventSourced.registerConsumer(CartActor.class, CartEvents.CreatedForUser.class,
                applyCartCreated);

        BiConsumer<CartActor, CartEvents.ProductQuantityChangeEvent> applyQuantityChange = CartActor::applyQuantityChange;
        EventSourced.registerConsumer(CartActor.class, CartEvents.ProductQuantityChangeEvent.class, applyQuantityChange);

        BiConsumer<CartActor, CartEvents.AllItemsRemovedEvent> applyRemoveAll = CartActor::applyAllItemsRemoved;
        EventSourced.registerConsumer(CartActor.class, CartEvents.AllItemsRemovedEvent.class, applyRemoveAll);
    }

    @Override
    public void createEmptyCartFor(UserId userId) {
        apply(CartEvents.CreatedForUser.forUser(state.cartId, userId));
    }

    @Override
    public Completes<List<CartItem>> addItem(ProductId productId) {
        return apply(CartEvents
                .ProductQuantityChangeEvent
                .with(state.cartId,
                        state.userId,
                        productId,
                        1,
                        state.calcNewQuantityByProductId(productId, 1)),
                () -> state.getCartItems());
    }

    @Override
    public Completes<List<CartItem>> removeItem(ProductId productId) {
    	return apply(CartEvents
                        .ProductQuantityChangeEvent
                        .with(state.cartId,
                                state.userId,
                                productId,
                                -1,
                                state.calcNewQuantityByProductId(productId, -1)),
                () -> state.getCartItems());
    }

    @Override
    public Completes<List<CartItem>> queryCart() {
        return completes().with(state.getCartItems());
    }

    @Override
    public Completes<List<CartItem>> removeAllItems() {
    	return apply(CartEvents.AllItemsRemovedEvent.with(state.cartId, state.userId), () -> state.getCartItems());
    }

    private void applyCartCreated(final CartEvents.CreatedForUser e) {
        state = state.initForUser(e.userId);
    }

    private void applyQuantityChange(final CartEvents.ProductQuantityChangeEvent e) {
        state = state.productQuantityChange(e.productId, e.quantityChange);
    }

    private void applyAllItemsRemoved(final CartEvents.AllItemsRemovedEvent e) {
        state = state.removeAllItems();
    }


    static class State {
        final String cartId;
        final Map<ProductId, Integer> basketProductsById;
        final UserId userId;


        private State(String cartId, UserId userId, Map<ProductId, Integer> basketProductsById) {
            this.cartId = cartId;
            this.basketProductsById = Collections.unmodifiableMap(basketProductsById);
            this.userId = userId;
        }

        static State create(String cartId) {
            return new State(cartId, UserId.Unspecified(), new HashMap<>());
        }

        State initForUser(UserId userId) {
            return new State(cartId, userId, new HashMap<>());
        }

        State removeAllItems() {
            return initForUser(userId);
        }

        State productQuantityChange(ProductId productId, int quantityChange) {
            HashMap<ProductId, Integer> copyBasketProductsById = new HashMap<>(basketProductsById);
            Integer quantity = calcNewQuantityByProductId(productId, quantityChange);
            if (quantity <= 0) {
                copyBasketProductsById.remove(productId);
            } else {
                copyBasketProductsById.put(productId, quantity);
            }
            return new State(cartId, userId, copyBasketProductsById);
        }

        int calcNewQuantityByProductId(ProductId productId, int quantityChange) {
            int quantity = basketProductsById.getOrDefault(productId, 0);
            int newQuantity = quantity + quantityChange;
            return Math.max(newQuantity, 0);
        }

        private List<CartItem> getCartItems() {
            return basketProductsById.entrySet().stream()
                    .map(entry -> new CartItem(entry.getKey(), entry.getValue()))
                    .collect(Collectors.toList());
        }
    }
}
