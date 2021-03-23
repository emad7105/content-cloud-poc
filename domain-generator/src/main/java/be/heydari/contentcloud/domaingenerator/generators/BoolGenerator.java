package be.heydari.contentcloud.domaingenerator.generators;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class BoolGenerator implements ValueGenerator<Boolean> {
    private final Random rand;

    public BoolGenerator(Random rand) {
        this.rand = rand;
    }

    @Override
    public Boolean generate() {
        return this.rand.nextBoolean();
    }

    @Override
    public String generateString() {
        var b = generate();
        return String.valueOf(b);
    }

    @Override
    public String generatedType() {
        return "boolean";
    }

    @Override
    public long uniqueEntryCount() {
        return 2;
    }

    @Override
    public List<Boolean> uniqueEntries() {
        return Arrays.asList(true, false);
    }
}
