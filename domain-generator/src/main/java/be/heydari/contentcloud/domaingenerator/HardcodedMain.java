package be.heydari.contentcloud.domaingenerator;

import be.heydari.contentcloud.domaingenerator.generators.BoolGenerator;
import be.heydari.contentcloud.domaingenerator.keycloak.*;
import org.keycloak.admin.client.KeycloakBuilder;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

        attributes.add(new MultiValuedAttribute("roles", generators.getRoles(), (int) Math.min(generators.getRoles().uniqueEntryCount(), 5)));


        var policySizeAttributes = new ArrayList<Attribute>();
        for (var i = 0; i != 25; i++) {
            policySizeAttributes.add(new SingletonAttribute("attribute" + i, new BoolGenerator(null)));
        }

        var selectivityAttributes = new ArrayList<Attribute>();
        for (var selectivity: Arrays.asList(1, 10, 20, 40, 60, 80, 100)) {
            selectivityAttributes.add(new SingletonAttribute("select_" + selectivity, new BoolGenerator(null)));
        }

        var allAttrs = new ArrayList<Attribute>();
        allAttrs.addAll(attributes);
        allAttrs.addAll(policySizeAttributes);
        allAttrs.addAll(selectivityAttributes);

        var mappers = allAttrs.stream().map(Attribute::mapper).collect(Collectors.toList());
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

            for (var attr: policySizeAttributes) {
                values.put(attr.getName(), Collections.singletonList(broker));
            }

            for (var attr: selectivityAttributes) {
                values.put(attr.getName(), Collections.singletonList("true"));
            }

            user.setAttributes(values);
        }
    }
}
