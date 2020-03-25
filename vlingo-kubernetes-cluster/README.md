# vlingo-kubernetes-cluster

This is a basic example to demo how to run a vlingo/cluster on Kubernetes and monitor its status. 

The codebase does not have neither an API or businnes logic, but only required infrastructure configuration, using `vlingo-cluster` features, to run each node. Here's the structure:

    └── io
        └── vlingo
            └── kubernetescluster
                ├── Bootstrap (app initializer)
                ├── KubernetesClusterApplicationActor.java (cluster Application Adapter)
                └── KubernetesClusterInstantiator.java (adapter instatiation handling) 
     
Whereas `Bootstrap` is the application main class, an executable JAR is required to run the cluster node. Running `mvn clean package` will generate binary with the `manifest` file by means of maven plugins in `pom.xml` (note `maven-assembly-plugin` and `exec-maven-plugin` configuration).

According to `vlingo-cluster` [docs](https://docs.vlingo.io/vlingo-cluster#resiliency-and-scale), if, at least, two nodes are running and communicating to each other, the cluster is healthy. In case of one node running, the cluster is considered.
