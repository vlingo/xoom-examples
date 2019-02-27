
# E-commerce Example 

This example illustrates the use of event-sourced actors using e-commerce concepts.  The implementation is conceptual- there
is no authorization/authentication, etc.

# API

* See CartResource
* See OrderResource

# Build and tests

    mvn clean verify -pl vlingo-ecommerce

# Improvements / Future case ideas
* Payment notifications coming from Payment actor choreography
* "Product recommendation" - choreography of basket and order events to show list of "recommended" products
* "Cart reminders" - illustrate actor timers to remind users of items "left" in the cart

# Issues
* Separate command from query; create a query actor for supporting the views into the orders, carts
* README (done)
