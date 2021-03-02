import org.keycloak.admin.client.resource.ClientResource;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.representations.idm.ClientRepresentation;
import org.keycloak.representations.idm.ProtocolMapperRepresentation;

import javax.validation.constraints.NotNull;
import java.util.Collections;
import java.util.List;

public class Client {
    private final String name;
    private final String secret;
    private ClientResource client = null;

    Client(String name, String secret) {
        this.name = name;
        this.secret = secret;
    }

    ClientResource create(@NotNull RealmResource realm) {
        var client = new ClientRepresentation();
        client.setClientId(name);
        client.setRedirectUris(Collections.singletonList("*"));
        client.setClientAuthenticatorType("client-secret");
        client.setSecret(secret);
        client.setEnabled(true);
        client.setStandardFlowEnabled(true);
        realm.clients().create(client);

        this.client = this.resource(realm);
        return this.client;
    }

    void setAttributes(List<ProtocolMapperRepresentation> mappers) {
        var rep = this.client.toRepresentation();
        rep.setProtocolMappers(mappers);
        this.client.update(rep);
    }

    private ClientResource resource(@NotNull RealmResource realm) {
        var clients = realm.clients().findByClientId(name);
        if (clients.size() == 0) {
            throw new RuntimeException("client" + name + "not found");
        }

        var rep = clients.get(0);
        return realm.clients().get(rep.getId());
    }
}
