package be.heydari.contentcloud.domaingenerator.keycloak;

import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.representations.idm.RealmRepresentation;

import javax.validation.constraints.NotNull;

public class Realm {
    private final String name;
    private RealmResource realm = null;

    public Realm(String name) {
        this.name = name;
    }

    public RealmResource create(@NotNull Keycloak keycloak) {
        var realm = new RealmRepresentation();
        realm.setRealm(name);
        realm.setEnabled(true);
        keycloak.realms().create(realm);

        this.realm = keycloak.realm(name);
        return this.realm;
    }

    public RealmResource resource() {
        return this.realm;
    }
}
