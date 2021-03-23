package be.heydari.contentcloud.domaingenerator;

import be.heydari.contentcloud.domaingenerator.keycloak.*;
import org.keycloak.admin.client.KeycloakBuilder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class HardcodedMain {
    public static void main(String[] args) {
        var env = System.getenv();
        var keycloakAddr = env.getOrDefault("KEYCLOAK_ADDR", "localhost:8080");

        System.out.println("keycloak address::" + "http://" + keycloakAddr + "/auth");

        var keycloak = KeycloakBuilder.builder()
            .serverUrl("http://" + keycloakAddr + "/auth")
            .realm("master")
            .clientId("admin-cli")
            .username("admin")
            .password("admin")
            .build();

        try {
            keycloak.realm("content-cloud-realm").remove();
        } catch (Exception e) {
            System.out.println("realm not found!");
        }


        var realm = new Realm("content-cloud-realm");
        realm.create(keycloak);
        var client = new Client("content-cloud-gateway-client", "92064e45-a751-4efb-bc1f-b4fbc2e99565");
        client.create(realm.resource());

        var generators = new Generators.HardcodedGenerator();
        var attributes = new ArrayList<Attribute>();
        attributes.add(new SingletonAttribute("broker", generators.getBroker()));
        attributes.add(new SingletonAttribute("accountState", generators.getAccountState()));
        attributes.add(new SingletonAttribute("probation", generators.getProbation()));
        attributes.add(new SingletonAttribute("clearanceLevel", generators.getClearanceLevel()));

        for (var i = 0; i != 10; i++) {
            attributes.add(new SingletonAttribute("attribute" + i, generators.getAttribute()));
        }

        attributes.add(new MultiValuedAttribute("roles", generators.getRoles(), (int) Math.min(generators.getRoles().uniqueEntryCount(), 5)));

        var mappers = attributes.stream().map(Attribute::mapper).collect(Collectors.toList());
        client.setAttributes(mappers);


        for (var broker : generators.getBroker().uniqueEntries()) {
            var user = new User(broker);
            user.create(realm.resource());
            var values = new HashMap<String, List<String>>();
            for (var attr : attributes) {
                if (attr.getName().equals("broker")) {
                    values.put("broker", Collections.singletonList(broker));
                } else {
                    var value = attr.generate();
                    values.put(value.getValue0(), value.getValue1());
                }
            }
            user.setAttributes(values);
        }
    }
}
