import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;

import javax.validation.constraints.NotNull;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class User {
    private final String username;
    private UserResource user = null;

    User(String username) {
        this.username = username;
    }

    UserResource create(@NotNull RealmResource realm) {
        var credentials = new CredentialRepresentation();
        credentials.setValue(username);

        var user = new UserRepresentation();
        user.setUsername(username);
        user.setEnabled(true);
        user.setEmailVerified(false);
        user.setCredentials(Collections.singletonList(credentials));
        user.setRequiredActions(Collections.emptyList());

        realm.users().create(user);
        this.user = this.resource(realm);
        return this.user;
    }

    void setAttributes(Map<String, List<String>> attributes) {
        var user = this.user.toRepresentation();
        user.setAttributes(attributes);
        this.user.update(user);
    }

    private UserResource resource(@NotNull RealmResource realm) {
        var users = realm.users().search(username);
        if (users.size() == 0) {
            throw new RuntimeException("user" + username + "does not exist");
        }

        var userRepresentation = users.get(0);
        return realm.users().get(userRepresentation.getId());
    }
}
