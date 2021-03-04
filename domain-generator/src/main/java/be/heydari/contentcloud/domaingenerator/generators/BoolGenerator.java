package be.heydari.contentcloud.domaingenerator.generators;

import java.util.Random;

public class BoolGenerator implements ValueGenerator {
    private final Random rand;

    public BoolGenerator(Random rand) {
        this.rand = rand;
    }

    public String generate() {
        var b = this.rand.nextBoolean();
        return String.valueOf(b);
    }

    public long uniqueEntries() {
        return 2;
    }

    public String generatedType() {
        return "boolean";
    }
}
