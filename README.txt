1. Do a Maven build, by running the following command in the project catalog:

mvn clean package


2. Make a docker image, by running the following command in the project catalog:

docker build -t rafal-pienkowski/fibo .


3. Create all Kubernetes resources, by running:

kubctl apply -f fib.yaml


4. Application is now available under following url (param "n" is changing):

http://localhost:8080/fib?n=1