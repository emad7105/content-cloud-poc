#!/bin/bash

mvn -f lazy-abac-filter/pom.xml clean install -DskipTests
mvn -f domain-generator/pom.xml clean install -DskipTests
mvn -f opa/pom.xml clean package -DskipTests
mvn -f gateway/pom.xml clean package -DskipTests
mvn -f gateway2/pom.xml clean package -DskipTests
mvn -f account-state-common/pom.xml clean install -DskipTests
mvn -f account-state-service/pom.xml clean package -DskipTests
mvn -f account-state-postfilter/pom.xml clean package -DskipTests
mvn -f account-state-hardcoded/pom.xml clean package -DskipTests

docker build -f domain-generator/dockerfile -t contentcloudpoc.azurecr.io/fill-keycloak .
docker build -f locust-client/dockerfile -t contentcloudpoc.azurecr.io/locust .
docker build -f opa/dockerfile -t contentcloudpoc.azurecr.io/policy-pusher .
docker build -f gateway/dockerfile -t contentcloudpoc.azurecr.io/gateway .
docker build -f gateway2/dockerfile -t contentcloudpoc.azurecr.io/gateway2 .
docker build -f account-state-service/dockerfile -t contentcloudpoc.azurecr.io/account-state-query .
docker build -f account-state-postfilter/dockerfile -t contentcloudpoc.azurecr.io/account-state-postfilter .
docker build -f account-state-hardcoded/dockerfile -t contentcloudpoc.azurecr.io/account-state-hardcoded .

docker push contentcloudpoc.azurecr.io/fill-keycloak
docker push contentcloudpoc.azurecr.io/locust
docker push contentcloudpoc.azurecr.io/policy-pusher
docker push contentcloudpoc.azurecr.io/gateway
docker push contentcloudpoc.azurecr.io/gateway2
docker push contentcloudpoc.azurecr.io/account-state-query
docker push contentcloudpoc.azurecr.io/account-state-postfilter
docker push contentcloudpoc.azurecr.io/account-state-hardcoded

docker rmi $(docker images -f dangling=true -q) || true
