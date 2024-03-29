# Content Cloud PoC (security architecture)

This repository hosts a proof-of-concept project to demonstrate the security architecture of a sample microservice
application with the help of our efforts in the Content Cloud A project. The core features are:

- __Standard authentication and authorization__: using OpenID Connect and OAuth2 integration with the KeyCloak identity
  management tool and Spring Security
- __Lazy and distributed evaluation of ABAC policies__: using the [lazypolicy](https://github.com/emad7105/lazypolicy/)
  and [abac-for-springdata](https://github.com/paulcwarren/abac_spike_1) modules developed in the project with the help
  of Open Policy Agent (OPA) 
