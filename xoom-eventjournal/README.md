xoom-eventjournal example
===========================

Usually event journals are used with vlingo/lattice, however, this is
an example on how to use it directly to share the basics.

This requires a PostgreSQL database running in your machine, if you
have docker installed, run the docker-compose in this directory to crete
a basic PostgreSQL cluster.

This application implements an [event producer](src/main/java/io/vlingo/xoom/examples/eventjournal/counter/CounterActor.java) 
and an [event consumer](src/main/java/io/vlingo/xoom/examples/eventjournal/counter/CounterQueryActor.java).

