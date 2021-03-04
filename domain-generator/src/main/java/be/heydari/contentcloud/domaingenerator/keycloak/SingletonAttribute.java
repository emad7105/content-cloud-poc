package be.heydari.contentcloud.domaingenerator.keycloak;

import be.heydari.contentcloud.domaingenerator.generators.ValueGenerator;
import org.javatuples.Pair;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SingletonAttribute extends Attribute {
    public SingletonAttribute(String name, ValueGenerator generator) {
        super(name, generator);
    }

    @Override
    public Pair<String, List<String>> generate() {
        var value = this.generator.generate();
        return new Pair<>(this.name, Collections.singletonList(value));
    }

    @Override
    public Map<String, String> extraConfig() {
        return new HashMap<>();
    }
}
