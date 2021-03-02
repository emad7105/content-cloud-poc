import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.ClientRepresentation;
import org.keycloak.representations.idm.RealmRepresentation;

import java.util.Arrays;
import java.util.Collections;

public class Generator {
    private final Keycloak keycloak;

    Generator(Keycloak keycloak) {
        this.keycloak = keycloak;
    }

    public void createRealm(String realmName) {
        var realm = new RealmRepresentation();
        realm.setRealm(realmName);
        realm.setEnabled(true);
        this.keycloak.realms().create(realm);
    }

    public void createClient(String realmName) {
        var client = new ClientRepresentation();
        client.setClientId("content-cloud-gateway");
        client.setRedirectUris(Collections.singletonList("*"));
        client.setEnabled(true);
        client.setSecret("22f4efff-8e2d-4a6f-8462-4b193b6aae34");
        client.setClientAuthenticatorType("client-secret");
        client.setStandardFlowEnabled(true);

        this.keycloak.realm(realmName).clients().create(client);
    }
}
