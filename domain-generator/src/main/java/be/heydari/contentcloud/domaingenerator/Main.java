package be.heydari.contentcloud.domaingenerator;

import be.heydari.contentcloud.domaingenerator.generators.IntGenerator;
import be.heydari.contentcloud.domaingenerator.keycloak.*;
import com.google.gson.GsonBuilder;
import org.keycloak.admin.client.KeycloakBuilder;

import java.util.*;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        var keycloak = KeycloakBuilder.builder()
            .serverUrl("http://localhost:8080/auth")
            .realm("master")
            .clientId("admin-cli")
            .username("admin")
            .password("admin")
            .build();

        try {
            keycloak.realm("content-cloud-realm").remove();
        } catch (Exception e){
            // ignore
        }


        var rand = new Random();
        var realm = new Realm("content-cloud-realm");
        realm.create(keycloak);
        var client = new Client("content-cloud-gateway-client", "92064e45-a751-4efb-bc1f-b4fbc2e99565");
        client.create(realm.resource());

        var generators = Generators.Example1();
        var attributes = new ArrayList<Attribute>();
        var singleGens = generators.getSingleGenerators();
        for (var key: singleGens.keySet()) {
            var gen = singleGens.get(key);
            attributes.add(new SingletonAttribute(key, gen));
        }

        var multiGens = generators.getMultiValuedGenerators();
        for (var key: multiGens.keySet()) {
            var gen = multiGens.get(key);
            var unique = gen.uniqueEntries();
            attributes.add(new MultiValuedAttribute(key, gen,  (int) Math.min(unique, 5)));
        }

        var mappers = attributes.stream().map(Attribute::mapper).collect(Collectors.toList());
        client.setAttributes(mappers);

        for (int i = 0; i != 10; i++) {
            var user = new User("broker" + i);
            user.create(realm.resource());
            var values = new HashMap<String, List<String>>();
            for (var attr : attributes) {
                var value = attr.generate();
                values.put(value.getValue0(), value.getValue1());
            }
            user.setAttributes(values);
        }

        var gson = new GsonBuilder().setPrettyPrinting().create();
        System.out.println(
            gson.toJson(
                keycloak.realm("content-cloud-realm")
                    .clients()
                    .findByClientId("content-cloud-gateway-client")
                    .get(0)
            )
        );
    }
}
