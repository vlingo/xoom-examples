# vlingo-kubernetes-cluster

This is a basic example to demo how to run a `vlingo/cluster` on Kubernetes. 

## Structure

The codebase does not have neither an API nor businnes logic, but only required `vlingo/cluster` configuration code to run each node. Here's the structure:

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

Find the full `vlingo-cluster` configuration guide [here](https://docs.vlingo.io/vlingo-cluster#using-the-vlingo-cluster).

## Build

Whereas `Bootstrap` is the application main class, an executable JAR is required to run the cluster node. The maven build will generate binary with the `manifest` file by means of plugins in [pom.xml](https://github.com/vlingo/vlingo-examples/blob/master/vlingo-kubernetes-cluster/pom.xml) (note `maven-assembly-plugin` and `exec-maven-plugin` configuration). The project build is achieved by the following command: 

```
    $ ./mvn clean package
```

## Deployment

Every node will run within a Docker container. Therefore, the next step is to build a Docker image based on executable jar previously generated, tagging it as `latest` and pushing it to a Docker repository. The `vlingo-kubernetes-cluster` image is already published in a [public repository](https://hub.docker.com/repository/docker/dambrosio/vlingo-kubernetes-cluster), so the next commands is shown only for information purpose because the `vlingo-kubernetes-cluster` image is already published in a [public repository](https://hub.docker.com/repository/docker/dambrosio/vlingo-kubernetes-cluster). 

```
    $ ./docker build .\ -t vlingo-kubernetes-cluster:latest
    $ ./docker tag vlingo-kubernetes-cluster:latest [publisher]/vlingo-kubernetes-cluster:latest
    $ ./docker push [publisher]/vlingo-kubernetes-cluster
```

See more details about the image in the [Dockerfile](https://github.com/vlingo/vlingo-examples/blob/master/vlingo-kubernetes-cluster/Dockerfile).   

Now, the published Docker image, which holds the vlingo/cluster node, can be referenced in the Kubernetes deployment rules. Through the Kubernetes workload objects, there are multiple ways to make it and, in the current project, the choice is [Deployment Controller](https://kubernetes.io/docs/concepts/workloads/controllers/deployment/) executing three containers, one for each node, inside the same POD, so they can communicate each other at localhost using different ports. In `vlingo-cluster.yaml`, under `deployment` folder, the container is declared matching the invidual node configuration in `vlingo-cluster.properties`:

```
    ...
    
    containers:
      - name: first-node
        image: dambrosio/vlingo-kubernetes-cluster:latest
        env:
          - name: NODE_NAME
            value: "node1"
        ports:
          - containerPort: 8080
            name: app
          - containerPort: 9080
            name: op
      - name: second-node
        image: dambrosio/vlingo-kubernetes-cluster:latest
        env:
          - name: NODE_NAME
            value: "node2"
        ports:
          - containerPort: 8081
            name: app
          - containerPort: 9081
            name: op
      - name: third-node
        image: dambrosio/vlingo-kubernetes-cluster:latest
        env:
          - name: NODE_NAME
            value: "node3"
        ports:
          - containerPort: 8082
            name: app
          - containerPort: 9082
            name: op
```

Finally, if the Kubernetes cluster is already running fine, the Deployment Controller can be applied:

```
    $ ./kubectl apply -f ./deployments/vlingo-cluster.yaml
```

Check if the POD status is ok:

```
    $ ./kubectl get pods

    NAME                              READY   STATUS    RESTARTS   AGE
    vlingo-cluster-5bfdf5c58c-zzb26   2/2     Running   0          18h    
```

## Cluster Health, Quorum, and Leadership

According to `vlingo-cluster` [docs](https://docs.vlingo.io/vlingo-cluster#resiliency-and-scale), if, at least, two nodes are running and communicating to each other, the cluster is healthy. In case of one node is live, the cluster is considered. That can be verified reading the Docker container logs for one of nodes. Find the `Container ID` with the `docker ps` command then read logs as following: 

```
    $ ./docker logs [container-id]
```    

It takes a few seconds to each node join the cluster and, until at least two nodes are joined, the cluster is considered unhealthy. The snapshot below shows when the first node has joined the cluster:

```
15:30:11.095 [pool-2-thread-2] DEBUG io.vlingo.actors.Logger - io.vlingo.cluster.model.node.LocalLiveNodeActor - IDLE Id[1] JOIN: Join[Node[Id[2],Name[node2],Address[Host[localhost],9081,OP], Address[Host[localhost],8081,APP]]]
15:30:11.119 [pool-2-thread-2] DEBUG io.vlingo.actors.Logger - io.vlingo.kubernetescluster.KubernetesClusterApplicationActor - APP: Id[1] joined cluster
15:30:11.119 [pool-2-thread-2] DEBUG io.vlingo.actors.Logger - io.vlingo.kubernetescluster.KubernetesClusterApplicationActor - APP: Cluster is NOT healthy
```

After two or three nodes are actually part of the cluster, it becomes healthy. Further, the quorum is achieved and a leader node is elected:

```
15:30:12.507 [pool-2-thread-3] DEBUG io.vlingo.actors.Logger - io.vlingo.kubernetescluster.KubernetesClusterApplicationActor - APP: Cluster is healthy
15:30:12.507 [pool-2-thread-3] DEBUG io.vlingo.actors.Logger - io.vlingo.kubernetescluster.KubernetesClusterApplicationActor - APP: Quorum achieved
...
15:30:18.806 [pool-2-thread-2] DEBUG io.vlingo.actors.Logger - io.vlingo.kubernetescluster.KubernetesClusterApplicationActor - APP: Leader elected: Id[3]
```

## Additional Information

It's possible to watch more carefully how the cluster behaves in exceptional conditions by removing any node in `vlingo-cluster.yaml` (container section).  

The `vlingo-cluster` [docs](https://docs.vlingo.io/vlingo-cluster) is highly recommended to adjust its capabilities to your needs.
