#!/bin/bash

eval $(minikube docker-env)
./build.sh
eval $(minikube docker-env -u)
