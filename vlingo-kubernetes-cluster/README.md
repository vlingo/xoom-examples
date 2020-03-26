# vlingo-kubernetes-cluster

This is a basic example to demo how to run a vlingo/cluster on Kubernetes and monitor its status. 

The codebase does not have neither an API nor businnes logic, but only required `vlingo-cluster` configuration code to run each node. Here's the structure:

    └── io
        └── vlingo
            └── kubernetescluster
                ├── Bootstrap (app initializer)
                ├── KubernetesClusterApplicationActor.java (cluster application adapter)
                └── KubernetesClusterInstantiator.java (adapter instatiation handling) 
     

Those are the needed classes to run a `vlingo-cluster`. In addition, the `vlingo-cluster.properties` contains 

Whereas `Bootstrap` is the application main class, an executable JAR is required to run the cluster node. The maven build will generate binary with the `manifest` file by means of plugins in [pom.xml](https://github.com/vlingo/vlingo-examples/blob/master/vlingo-kubernetes-cluster/pom.xml) (note `maven-assembly-plugin` and `exec-maven-plugin` configuration). The project build is achieved by the following command: 

```
    mvn clean package
```

Every node will run within a Docker container. Therefore, the next step is to build a Docker image based on executable jar previously generated, tagging it as `latest` and pushing it to a Docker repository. The `vlingo-kubernetes-cluster` image is already published in a [public repository](https://hub.docker.com/repository/docker/dambrosio/vlingo-kubernetes-cluster), so the next commands is shown only for information purpose because the `vlingo-kubernetes-cluster` image is already published in a [public repository](https://hub.docker.com/repository/docker/dambrosio/vlingo-kubernetes-cluster). 

```
    docker build .\ -t vlingo-kubernetes-cluster:latest
    docker tag vlingo-kubernetes-cluster:latest [publisher]/vlingo-kubernetes-cluster:latest
    docker push [publisher]/vlingo-kubernetes-cluster
```

If you want to get more details about the image, check the [Dockerfile](https://github.com/vlingo/vlingo-examples/blob/master/vlingo-kubernetes-cluster/Dockerfile).   

Now, the node 

According to `vlingo-cluster` [docs](https://docs.vlingo.io/vlingo-cluster#resiliency-and-scale), if, at least, two nodes are running and communicating to each other, the cluster is healthy. In case of one node is live, the cluster is considered.
