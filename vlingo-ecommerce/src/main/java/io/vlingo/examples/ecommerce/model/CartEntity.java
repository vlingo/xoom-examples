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

        BiConsumer<CartEntity, CartEvents.ProductAddedEvent> applyItemAdded = CartEntity::applyItemAdded;
        EventSourced.registerConsumer(CartEntity.class, CartEvents.ProductAddedEvent.class, applyItemAdded);

        BiConsumer<CartEntity, CartEvents.ProductRemovedEvent> applyItemRemoved = CartEntity::applyItemRemoved;
        EventSourced.registerConsumer(CartEntity.class, CartEvents.ProductRemovedEvent.class, applyItemRemoved);

        BiConsumer<CartEntity, CartEvents.AllItemsRemovedEvent> applyRemoveAll = CartEntity::applyAllItemsRemoved;
        EventSourced.registerConsumer(CartEntity.class, CartEvents.AllItemsRemovedEvent.class, applyRemoveAll);
    }

    @Override
    public Completes<List<CartItem>> addItem(ProductId productId) {
        apply(CartEvents.ProductAddedEvent.with(state.userId, productId), () -> state.getCartItems());
        return completes();
    }

    @Override
    public Completes<List<CartItem>> removeItem(ProductId productId) {
        //Ignore silent failure case
        if (state.basketProductsById.containsKey(productId)) {
            apply(CartEvents.ProductRemovedEvent.with(state.userId, productId), () -> state.getCartItems());
            return completes();
        }
        else
            return queryCart();
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

        State productAdded(ProductId addedProductId) {
            HashMap<ProductId, Integer> copyBasketProductsById = new HashMap<>(basketProductsById);
            Integer quantity = copyBasketProductsById.getOrDefault(addedProductId, 0);
            copyBasketProductsById.put(addedProductId, quantity + 1);

            return new State(cartId, userId, copyBasketProductsById);
        }

        State productRemoved(ProductId removedProductId) {
            HashMap<ProductId, Integer> copyBasketProductsById = new HashMap<>(basketProductsById);
            Integer quantity = copyBasketProductsById.getOrDefault(removedProductId, 0);
            if (quantity <= 1) {
                copyBasketProductsById.remove(removedProductId);
            } else {
                copyBasketProductsById.put(removedProductId, quantity - 1);
            }
            return new State(cartId, userId, copyBasketProductsById);
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

    private void applyItemAdded(final CartEvents.ProductAddedEvent e) {
        state = state.productAdded(e.productId);
    }

    private void applyItemRemoved(final CartEvents.ProductRemovedEvent e) {
        state = state.productRemoved(e.productId);
    }

    private void applyAllItemsRemoved(final CartEvents.AllItemsRemovedEvent e) {
        state = state.removeAllItems();
    }

}
