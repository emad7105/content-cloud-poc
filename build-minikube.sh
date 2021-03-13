#!/bin/bash

eval $(minikube docker-env)
mvn -f account-state-service/pom.xml clean package -DskipTests
mvn -f gateway/pom.xml clean package -DskipTests
mvn -f domain-generator/pom.xml clean package -DskipTests

docker build -f account-state-service/dockerfile -t content-cloud/account-state-service .
docker build -f gateway/dockerfile -t content-cloud/gateway .
docker build -f domain-generator/dockerfile -t content-cloud/domain-generator .
docker build -f locust-client/dockerfile -t content-cloud/locust-client .

docker rmi $(docker images -f dangling=true -q) || true
eval $(minikube docker-env -u)
