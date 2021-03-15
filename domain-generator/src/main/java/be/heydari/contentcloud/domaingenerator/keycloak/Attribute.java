package be.heydari.contentcloud.domaingenerator.keycloak;

import be.heydari.contentcloud.domaingenerator.generators.ValueGenerator;
import org.javatuples.Pair;
import org.keycloak.representations.idm.ProtocolMapperRepresentation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class Attribute {
    protected final String name;
    protected final ValueGenerator generator;

    public Attribute(String name, ValueGenerator generator) {
        this.name = name;
        this.generator = generator;
    }

    public abstract Pair<String, List<String>> generate();

    public abstract Map<String, String> extraConfig();

    public ProtocolMapperRepresentation mapper() {
        var mapper = new ProtocolMapperRepresentation();
        mapper.setProtocol("openid-connect");
        mapper.setProtocolMapper("oidc-usermodel-attribute-mapper");
        mapper.setName(name);

        var config = new HashMap<String, String>();
        config.put("userinfo.token.claim", "true");
        config.put("user.attribute", name);
        config.put("id.token.claim", "true");
        config.put("access.token.claim", "true");
        config.put("claim.name", name);
        config.put("jsonType.label", generator.generatedType());
        config.putAll(this.extraConfig());
        mapper.setConfig(config);

        return mapper;
    }
}
