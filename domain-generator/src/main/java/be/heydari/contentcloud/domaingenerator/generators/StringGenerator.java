package be.heydari.contentcloud.domaingenerator.generators;

import java.util.List;
import java.util.Random;

public class StringGenerator implements ValueGenerator {
    private final List<String> strings;
    private final Random rand;

    public StringGenerator(Random rand, List<String> strings) {
        this.strings = strings;
        this.rand = rand;
    }

    public String generate() {
        return strings.get(rand.nextInt(strings.size()));
    }

    public long uniqueEntryCount() {
        return strings.size();
    }

    public List<String> uniqueEntries() {
        return strings;
    }

    public String generatedType() {
        return "string";
    }

    public List<String> getStrings() {
        return strings;
    }
}
