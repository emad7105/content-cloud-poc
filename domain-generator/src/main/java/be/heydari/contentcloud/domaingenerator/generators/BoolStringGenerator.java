package be.heydari.contentcloud.domaingenerator.generators;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class BoolStringGenerator implements StringValueGenerator {
    private final Random rand;

    public BoolStringGenerator(Random rand) {
        this.rand = rand;
    }

    public String generate() {
        var b = this.rand.nextBoolean();
        return String.valueOf(b);
    }

    public long uniqueEntryCount() {
        return 2;
    }

    public List<String> uniqueEntries() {
        return Arrays.asList(String.valueOf(true), String.valueOf(false));
    }

    public String generatedType() {
        return "boolean";
    }
}
