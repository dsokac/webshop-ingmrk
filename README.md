# Webshop basics
The task for job application at a company Ingemark

## Instructions to run a project
It is necessary to setup Lombok library and Docker container. Afterwards, the project should be cloned from my github account. The instructions are as follows.

### (1) Lombok
Lombok is used to generate getters and setters. To ensure its correct functioning, it is necessary to do following steps.

#### Setup Lombok
- To install lombok and apply it to your IDE, visit following link: https://projectlombok.org/setup/maven
- To setup Spring Tools Suite, visit following link: https://projectlombok.org/setup/eclipse

#### Apply Lombok Annotations 
In the case of missing getters and setters, you will need to do following:
- Right click on root of the project and choose "Properties". Afterwards, navigate to the "Java Compiler" and then "Annotation Processing". Make sure that annotation processing is enabled. "Enable annotation processing" and "Enable processing in editor" should be ticked.

### (2) Docker container
Get docker image from using following command:
```
docker pull dsokac/webshop-dbserver-ingemark
```

Run docker image as container using following command:
```
docker run --publish 5432:5432 --name webshop-dbserver-ingemark -d dsokac/webshop-dbserver-ingemark
```
When the container is running the project will start correctly.

### (3) Project repository
The source code is located in my repository. To access the code, clone the repository from the link: https://github.com/dsokac/webshop-ingmrk
