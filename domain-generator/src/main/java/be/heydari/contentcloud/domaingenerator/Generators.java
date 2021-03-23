package be.heydari.contentcloud.domaingenerator;

import be.heydari.contentcloud.domaingenerator.generators.*;
import lombok.Getter;
import org.javatuples.Pair;

import java.util.*;

public class Generators {

    @Getter
    public static class HardcodedGenerator {
        private final Random rand;
        private final ValueGenerator<Integer> accountState;
        private final ValueGenerator<String> broker;
        private final ValueGenerator<String> roles;
        private final ValueGenerator<Boolean> probation;
        private final ValueGenerator<String> clearanceLevel;
        private final ValueGenerator<String> attribute;

        public HardcodedGenerator() {
            rand = Generators.rand;
            var brokers = new ArrayList<String>();
            var roles = new ArrayList<String>();
            var attributes = new ArrayList<String>();
            for (var i = 0; i != 10; i++) {
                brokers.add("broker" + i);
                roles.add("role" + i);
                attributes.add("value" + i);
            }

            this.accountState = new IntGenerator(rand, 10_000, 10_000_000);
            this.broker = new StringGenerator(rand, brokers);
            this.roles = new StringGenerator(rand, roles);
            this.probation = new BoolGenerator(rand);
            this.clearanceLevel = new StringGenerator(rand, Arrays.asList("public", "confidential", "secret", "top-secret"));
            this.attribute = new StringGenerator(rand, attributes);
        }
    }

    public static Generators Example1() {
        var rand = Generators.rand;
        var brokers = new ArrayList<String>();
        var generator = new Generators();
        for (int i = 0; i != 10; i++) {
            brokers.add("broker" + i);
        }
        generator.add("broker", new StringGenerator(rand, brokers));

        var roles = new ArrayList<String>();
        for (int i = 0; i != 10; i++) {
            roles.add("role" + i);
        }
        generator.add("roles", new StringGenerator(rand, roles));

        generator.add("accountState", new IntStringGenerator(rand, 1000, 10_000_000));
        generator.add("clearanceLevel", new StringGenerator(rand, Arrays.asList("public", "confidential", "secret", "top-secret")));
        generator.add("probation", new BoolStringGenerator(rand));

        for (int i = 0; i != 10; i++) {
            generator.generators.put("attribute" + i, new IntStringGenerator(rand, 0, 10));
        }

        return generator;
    }

    public static Random rand = new Random();

    //    public List<String> brokers = new ArrayList<>();
//    public List<String> roles = new ArrayList<>();
//    public List<String> clearanceLevels = new ArrayList<>();
//    public Pair<Integer, Integer> accountStateRange = new Pair<>(0,0);
    public Pair<Integer, Integer> numberRange = new Pair<>(0, 0);
    public Map<String, StringValueGenerator> generators = new HashMap<>();
    public Map<String, StringValueGenerator> multiValue = new HashMap<>();

    public void add(String name, StringValueGenerator generator) {
        this.generators.put(name, generator);
    }

    public StringValueGenerator get(String name) {
        return this.generators.get(name);
    }

    public Map<String, StringValueGenerator> getSingleGenerators() {
        return generators;
    }

    public void addMultiValue(String name, StringValueGenerator generator) {
        this.multiValue.put(name, generator);
    }

    public Map<String, StringValueGenerator> getMultiValuedGenerators() {
        return multiValue;
    }
}
