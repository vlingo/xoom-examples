package io.vlingo.examples.ecommerce.model;

import io.vlingo.common.Completes;
import io.vlingo.lattice.model.sourcing.EventSourced;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

public class CartEntity extends EventSourced<CartEntity.State> implements Cart {

    private State state;

    public CartEntity(final String shoppingCartId, final UserId userId) {
        apply(CartEvents.CreatedEvent.forUser(shoppingCartId, userId));
    }

    @Override
    public String streamName() {
        return "cartEvents";
    }

    static {
        BiConsumer<CartEntity, CartEvents.CreatedEvent> applyCartCreated = CartEntity::applyCartCreated;
        EventSourced.registerConsumer(CartEntity.class, CartEvents.CreatedEvent.class, applyCartCreated);

        BiConsumer<CartEntity, CartEvents.ProductQuantityChangeEvent> applyQuantityChange = CartEntity::applyQuantityChange;
        EventSourced.registerConsumer(CartEntity.class, CartEvents.ProductQuantityChangeEvent.class, applyQuantityChange);

        BiConsumer<CartEntity, CartEvents.AllItemsRemovedEvent> applyRemoveAll = CartEntity::applyAllItemsRemoved;
        EventSourced.registerConsumer(CartEntity.class, CartEvents.AllItemsRemovedEvent.class, applyRemoveAll);
    }

    @Override
    public Completes<List<CartItem>> addItem(ProductId productId) {
        apply(CartEvents
                .ProductQuantityChangeEvent
                .with(state.userId,
                        productId,
                        1,
                        state.calcNewQuantityByProductId(productId, 1)),
                () -> state.getCartItems());
        return completes();
    }

    @Override
    public Completes<List<CartItem>> removeItem(ProductId productId) {
        apply(CartEvents
                        .ProductQuantityChangeEvent
                        .with(state.userId,
                                productId,
                                -1,
                                state.calcNewQuantityByProductId(productId, -1)),
                () -> state.getCartItems());
        return completes();
    }

    @Override
    public Completes<List<CartItem>> queryCart() {
        return completes().with(state.getCartItems());
    }

    @Override
    public Completes<List<CartItem>> removeAllItems() {
        apply(CartEvents.AllItemsRemovedEvent.with(state.userId), () -> state.getCartItems());
        return completes();
    }

    static class State {
        final String cartId;
        final Map<ProductId, Integer> basketProductsById;
        final UserId userId;


        private State(String cartId, UserId userId, Map<ProductId, Integer> basketProductsById) {
            this.cartId = cartId;
            this.basketProductsById = new HashMap<>(basketProductsById);
            this.userId = userId;
        }

        static State created(String cartId, UserId userId) {
            return new State(cartId, userId, new HashMap<>());
        }

        State removeAllItems() {
            return created(cartId, userId);
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
            Integer quantity = basketProductsById.getOrDefault(productId, 0);
            Integer newQuantity = quantity + quantityChange;
            return (newQuantity < 0) ?  0 : newQuantity;
        }

        private List<CartItem> getCartItems() {
            return basketProductsById.entrySet().stream()
                    .map(entry -> new CartItem(entry.getKey(), entry.getValue()))
                    .collect(Collectors.toList());
        }
    }


    private void applyCartCreated(final CartEvents.CreatedEvent e) {
        state = State.created(e.shoppingCartId, e.userId);
    }

    private void applyQuantityChange(final CartEvents.ProductQuantityChangeEvent e) {
        state = state.productQuantityChange(e.productId, e.newQuantity);
    }

    private void applyAllItemsRemoved(final CartEvents.AllItemsRemovedEvent e) {
        state = state.removeAllItems();
    }

}
