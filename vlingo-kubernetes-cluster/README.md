# vlingo-kubernetes-cluster

This is a basic example to demo how to run a vlingo/cluster on Kubernetes. 

## Structure

The codebase does not have neither an API nor businnes logic, but only required `vlingo-cluster` configuration code to run each node. Here's the structure:

    └── io
        └── vlingo
            └── kubernetescluster
                ├── Bootstrap (app initializer)
                ├── KubernetesClusterApplicationActor.java (cluster application adapter)
                └── KubernetesClusterInstantiator.java (adapter instatiation handling) 
     

Those are the needed classes to run a `vlingo-cluster`. In addition, the `vlingo-cluster.properties`, under `src\main\resource\`, is the configuration file that specifies how cluster works, including, in the last section, the host and ports for each node:

```
    ################################
    # individual node configurations
    ################################

    node.node1.id = 1
    node.node1.name = node1
    node.node1.host = localhost
    node.node1.op.port = 9080
    node.node1.app.port = 8080

    node.node2.id = 2
    node.node2.name = node2
    node.node2.host = localhost
    node.node2.op.port = 9081
    node.node2.app.port = 8081

    node.node3.id = 3
    node.node3.name = node3
    node.node3.host = localhost
    node.node3.op.port = 9082
    node.node3.app.port = 8082

```

## Build

Whereas `Bootstrap` is the application main class, an executable JAR is required to run the cluster node. The maven build will generate binary with the `manifest` file by means of plugins in [pom.xml](https://github.com/vlingo/vlingo-examples/blob/master/vlingo-kubernetes-cluster/pom.xml) (note `maven-assembly-plugin` and `exec-maven-plugin` configuration). The project build is achieved by the following command: 

```
    mvn clean package
```

## Deployment

Every node will run within a Docker container. Therefore, the next step is to build a Docker image based on executable jar previously generated, tagging it as `latest` and pushing it to a Docker repository. The `vlingo-kubernetes-cluster` image is already published in a [public repository](https://hub.docker.com/repository/docker/dambrosio/vlingo-kubernetes-cluster), so the next commands is shown only for information purpose because the `vlingo-kubernetes-cluster` image is already published in a [public repository](https://hub.docker.com/repository/docker/dambrosio/vlingo-kubernetes-cluster). 

```
    docker build .\ -t vlingo-kubernetes-cluster:latest
    docker tag vlingo-kubernetes-cluster:latest [publisher]/vlingo-kubernetes-cluster:latest
    docker push [publisher]/vlingo-kubernetes-cluster
```

See more details about the image in the [Dockerfile](https://github.com/vlingo/vlingo-examples/blob/master/vlingo-kubernetes-cluster/Dockerfile).   

Now, the node 

According to `vlingo-cluster` [docs](https://docs.vlingo.io/vlingo-cluster#resiliency-and-scale), if, at least, two nodes are running and communicating to each other, the cluster is healthy. In case of one node is live, the cluster is considered.
