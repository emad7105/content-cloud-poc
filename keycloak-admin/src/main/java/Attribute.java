import org.javatuples.Pair;
import org.keycloak.admin.client.resource.ClientResource;
import org.keycloak.representations.idm.ClientRepresentation;
import org.keycloak.representations.idm.ProtocolMapperRepresentation;

import javax.validation.constraints.NotNull;
import java.util.HashMap;

public class Attribute {
    private final String name;
    private final String type;
    private final ValueGenerator generator;

    Attribute(String name, String type, ValueGenerator generator) {
        this.name = name;
        this.type = type;
        this.generator = generator;
    }

    Pair<String, String> generate() {
        var value = generator.generate();
        return new Pair<>(name, value);
    }

     ProtocolMapperRepresentation mapper() {
        var mapper = new ProtocolMapperRepresentation();
        mapper.setProtocol("openid-connect");
        mapper.setProtocolMapper("oidc-usermodel-attribute-mapper");
        mapper.setName(name);
        var config = new HashMap<String,String>();
        config.put("userinfo.token.claim", "true");
        config.put("user.attribute", name);
        config.put("id.token.claim", "true");
        config.put("access.token.claim", "true");
        config.put("claim.name", name);
        config.put("jsonType.label", type);
        mapper.setConfig(config);
        return mapper;
    }
}
