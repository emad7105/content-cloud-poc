import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.representations.idm.RealmRepresentation;

import java.util.*;
import java.util.stream.Collectors;

public class main {
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

        var attributes = new ArrayList<Attribute>();
        for (int i = 0; i != 10; i++) {
            var generator = new IntGenerator(rand, 0, 10);
            var attribute = new Attribute("attribute" + i, "long", generator);
            attributes.add(attribute);
        }

        var mappers = attributes.stream().map(Attribute::mapper).collect(Collectors.toList());
        client.setAttributes(mappers);

        for (int i = 0; i != 10; i++) {
            var user = new User("user" + i);
            user.create(realm.resource());
            var values = new HashMap<String, List<String>>();
            for (var attr : attributes) {
                var value = attr.generate();
                values.put(value.getValue0(), Collections.singletonList(value.getValue1()));
            }
            user.setAttributes(values);
        }
    }
}
