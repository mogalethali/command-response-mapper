# Command response mapper spring boot

### Requirement before running this services
* ms sql server must be running
* java 11 is required for this project
#### Swagger endpoints
* /{project-name}/v3/api-docs
* /{project-name}/v3/api-docs.yaml
* /{project-name}/swagger-ui/index.html


### MS SQL
* Ms sql must be up and running


### Reference Documentations
For further reference, please consider the following sections

* [Documenting Microservice using OpenAPI 3.0 specification ](https://swagger.io/specification/)
* [Spring boot maven plugin Reference guide](https://docs.spring.io/spring-boot/docs/2.6.5/maven-plugin/reference/htmlsingle/)
* [Creating an OCI image](https://docs.spring.io/spring-boot/docs/2.6.5/maven-plugin/reference/htmlsingle/#build-image)
* [Spring boot DevTool](https://docs.spring.io/spring-boot/docs/2.6.5/reference/htmlsingle/#using.devtools)
* [Spring data JPA](https://docs.spring.io/spring-boot/docs/2.6.5/reference/htmlsingle/#data.sql.jpa-and-spring-data)
* [Spring boot Messaging ](https://docs.spring.io/spring-boot/docs/2.6.5/reference/htmlsingle/#messaging)
* [Spring Actuator Monitor and Manage App](https://docs.spring.io/spring-boot/docs/current/reference/html/actuator.html)

### Guides

The following guides shows how to use some features:

* [Documenting Microservice using OpenAPI 3.0](https://swagger.io/specification/)
* [Build restful services](https://spring.io/guides/gs/rest-service/)
* [Circuit breaker](https://spring.io/guides/gs/cloud-circuit-breaker/)
* [Accessing Data with JPA](https://spring.io/guides/gs/accessing-data-rest/)

### Docker
To build docker image using Docker file:
* Cd in the root director of the project
* To build the docker image, execute the command below
  - docker build -t command-response-mapper .
    
* The command above create a Docker image named command-response-mapper.
* In dicate that the Dockerfile is in the current directory

## Kubernetes
Kubernetes, also known as K8s, is an open-source system for automating deployment, scaling, and management of containerized applications. Restarts containers that fail, replaces and reschedules containers when nodes die, kills containers that don't respond to your user-defined health check, and doesn't advertise them to clients until they are ready to serve.

Kubernetes progressively rolls out changes to your application or its configuration, while monitoring application health to ensure it doesn't kill all your instances at the same time. If something goes wrong, Kubernetes will rollback the change for you. Take advantage of a growing ecosystem of deployment solutions.
### Kubernetes deployment file
Int the root directory there is file named deployment.yaml with code snippet


#### Deploying to kubernetes

Now that we have kubernets deployment file we can deploy in to the cluster. Excute the command below to deploy the application to the cluster.
* kubectl apply -f deployment.yaml

We can check on the kubernetes dashbaord that the deployment is running with no error or we can run this command to see if our apps are running

* kubectl get po

This will list all running app with their status. To tail app logs check on the kubernetes dashboard or run the following command

kubectl logs -f name-of-the kr8-app

#### Scaling app

Run the following command to scale down or up app
* kubectl scale --replicas=3 deploy name of the app

#### Restarting App

Run the following command 

* kubectl rollout restart deployment {name of the app}

#Prometheus Installation
https://tanzu.vmware.com/developer/guides/observability-prometheus-grafana-p1/

https://suchit-g.medium.com/prometheus-on-docker-on-mac-e853df1ae4f5



#PROMETHEUS
###WHAT IS PROMETHEUS?
Is an open source and community-driven project that graduated from the Cloud Native Computing Foundation. It can aggregate data from almost everything:

* Microservices
* Multiple languages
* Linux servers
* Windows servers

### WHY DO WE NEED PROMETHEUS?
In our modern times of microservices, DevOps is becoming more and more complex and therefore needs automation.
We have hundreds of processes running over multiple servers, and they are all interconnected.

If we would not monitor these services then we have no clue about what is happening on hardware level or application level.
There are many things which we want to be notified about, like:

* Errors
* Response latency
* System overload
* Resources

When we are working with so many moving pieces, we want to be able to quickly identify a problem when something goes wrong inside one of our services.
If we wouldn’t monitor, it could be very time-consuming, since we have no idea where to look.
