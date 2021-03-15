package be.heydari.contentcloud.domaingenerator;

import be.heydari.contentcloud.domaingenerator.generators.*;
import org.javatuples.Pair;

import java.util.*;

public class Generators {
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

        generator.add("accountState", new IntGenerator(rand, 1000, 10_000_000));
        generator.add("clearanceLevel", new StringGenerator(rand, Arrays.asList("public", "confidential", "secret", "top-secret")));
        generator.add("probation", new BoolGenerator(rand));

        for (int i = 0; i != 10; i++) {
            generator.generators.put("attribute" + i, new IntGenerator(rand, 0, 10));
        }

        return generator;
    }

    public static Random rand = new Random();

    //    public List<String> brokers = new ArrayList<>();
//    public List<String> roles = new ArrayList<>();
//    public List<String> clearanceLevels = new ArrayList<>();
//    public Pair<Integer, Integer> accountStateRange = new Pair<>(0,0);
    public Pair<Integer, Integer> numberRange = new Pair<>(0, 0);
    public Map<String, ValueGenerator> generators = new HashMap<>();
    public Map<String, ValueGenerator> multiValue = new HashMap<>();

    public void add(String name, ValueGenerator generator) {
        this.generators.put(name, generator);
    }

    public ValueGenerator get(String name) {
        return this.generators.get(name);
    }

    public Map<String, ValueGenerator> getSingleGenerators() {
        return generators;
    }

    public void addMultiValue(String name, ValueGenerator generator) {
        this.multiValue.put(name, generator);
    }

    public Map<String, ValueGenerator> getMultiValuedGenerators() {
        return multiValue;
    }
}
