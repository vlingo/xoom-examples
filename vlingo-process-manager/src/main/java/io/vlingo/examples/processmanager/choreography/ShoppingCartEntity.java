package io.vlingo.examples.processmanager.choreography;

import io.vlingo.lattice.model.sourcing.EventSourced;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

import static io.vlingo.examples.processmanager.choreography.ShoppingCartEvents.*;

public class ShoppingCartEntity extends EventSourced implements ShoppingCart {

    private State state;

    public ShoppingCartEntity(final String shoppingCartId, final UserId userId) {
        apply(CreatedEvent.forUser(shoppingCartId, userId));
    }


    static {
        BiConsumer<ShoppingCartEntity, CreatedEvent> applyCartCreated = ShoppingCartEntity::applyCartCreated;
        EventSourced.registerConsumer(ShoppingCartEntity.class, CreatedEvent.class, applyCartCreated);

        BiConsumer<ShoppingCartEntity, ProductAddedEvent> applyItemAdded = ShoppingCartEntity::applyItemAdded;
        EventSourced.registerConsumer(ShoppingCartEntity.class, ProductAddedEvent.class, applyItemAdded);

        BiConsumer<ShoppingCartEntity, ProductRemovedEvent> applyItemRemoved = ShoppingCartEntity::applyItemRemoved;
        EventSourced.registerConsumer(ShoppingCartEntity.class, ProductAddedEvent.class, applyItemRemoved);

        BiConsumer<ShoppingCartEntity, AllItemsRemovedEvent> applyRemoveAll = ShoppingCartEntity::applyAllItemsRemoved;
        EventSourced.registerConsumer(ShoppingCartEntity.class, AllItemsRemovedEvent.class, applyRemoveAll);
    }

    @Override
    public void addItem(ProductId productId) {
        apply(ProductAddedEvent.with(state.userId, productId));
    }

    @Override
    public void removeItem(ProductId productId) {
        if (state.basketProductsById.containsKey(productId)) {
            apply(ProductRemovedEvent.with(state.userId, productId));
        }
    }

    @Override
    public List<CartItem> queryCart() {
        return state.basketProductsById.entrySet().stream()
                .map( entry -> new CartItem(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
    }

    @Override
    public void removeAllItems() {
        apply(AllItemsRemovedEvent.with(state.userId));
    }

    private static class State {
        final String shoppingCartId; //todo: make value type
        final Map<ProductId, Integer> basketProductsById;
        final UserId userId;


        private State(String shoppingCartId, UserId userId, Map<ProductId, Integer> basketProductsById) {
            this.shoppingCartId = shoppingCartId;
            this.basketProductsById = new HashMap<>(basketProductsById);
            this.userId = userId;
        }

        static State created(String shoppingCartId, UserId userId) {
            return new State(shoppingCartId, userId, new HashMap<>());
        }

        State removeAllItems() {
            return created(shoppingCartId, userId);
        }

        State productAdded(ProductId addedProductId) {
            HashMap<ProductId, Integer> copyBasketProductsById = new HashMap<>(basketProductsById);
            Integer quantity = copyBasketProductsById.getOrDefault(addedProductId, 0);
            copyBasketProductsById.put(addedProductId, quantity+1);

            return new State(shoppingCartId, userId, copyBasketProductsById);
        }

        State productRemoved(ProductId removedProductId) {
            HashMap<ProductId, Integer> copyBasketProductsById = new HashMap<>(basketProductsById);
            Integer quantity = copyBasketProductsById.getOrDefault(removedProductId, 0);
            if (quantity == 1) {
                copyBasketProductsById.remove(removedProductId);
            } else {
                copyBasketProductsById.put(removedProductId, quantity-1);
            }
            return new State(shoppingCartId, userId, copyBasketProductsById);
        }
    }


    private void applyCartCreated(final CreatedEvent e) {
        state = State.created(e.shoppingCartId, e.userId);
    }

    private void applyItemAdded(final ProductAddedEvent e) {
        state = state.productAdded(e.productId);
    }

    private void applyItemRemoved(final ProductRemovedEvent e) {
        state = state.productRemoved(e.productId);
    }

    private void applyAllItemsRemoved(final AllItemsRemovedEvent e) {
        state = state.removeAllItems();
    }

}
