#!/bin/bash

kubectl delete secret azurecrcred

kubectl create secret docker-registry azurecrcred \
  --namespace default \
  --docker-server=contentcloudpoc.azurecr.io \
  --docker-username=contentcloudpoc \
  --docker-password=$CONTENT_CLOUD_REGISTRY_PASSWORD