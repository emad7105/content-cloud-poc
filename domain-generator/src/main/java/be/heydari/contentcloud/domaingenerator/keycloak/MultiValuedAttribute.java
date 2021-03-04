package be.heydari.contentcloud.domaingenerator.keycloak;

import be.heydari.contentcloud.domaingenerator.generators.ValueGenerator;
import org.javatuples.Pair;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MultiValuedAttribute extends Attribute {
    private final int count;
    public MultiValuedAttribute(String name, ValueGenerator generator, int count) {
        super(name, generator);
        this.count = count;
    }

    @Override
    public Pair<String, List<String>> generate() {
         var value = this.generator.generateMultiple(count);
         return new Pair<>(this.name, value);
    }

    @Override
    public Map<String, String> extraConfig() {
       var config = new HashMap<String, String>();
       config.put("multivalued", String.valueOf(true));
       return config;
    }
}
