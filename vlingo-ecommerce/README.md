
# E-commerce Example 

This example illustrates the use of event-sourced actors using e-commerce concepts.  This is an example implementation with 
nominal dependencies and some limitations:
* Persistence: 
  *  in-memory persistence stores are used
* Tests: 
  * End-to-end tests have been written, but not unit or more specific integration tests
* Setup code
  * The Bootstrap code that sets up the environment is a singleton, it is not designed to run multiple times
  
# API

* CartResource
  * See CartResource.java:63
  * This module defines the paths, methods and arguments for interacting with the Cart actor
* OrderResource
  * See OrderResource.java:66
  * This module defines the paths, methods and arguments for interacting with the Order actor
  
# Build and tests

    mvn clean verify -pl vlingo-ecommerce

# Improvements / Future case ideas
* Payment notifications coming from Payment actor choreography
* "Product recommendation" - choreography of basket and order events to show list of "recommended" products
* "Cart reminders" - illustrate actor timers to remind users of items "left" in the cart

# Issues
* Separate command from query; create a query actor for supporting the views into the orders, carts
  * The cart summary illustrates how this could replace querying the actor
