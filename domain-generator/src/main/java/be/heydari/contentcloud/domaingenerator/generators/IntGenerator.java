package be.heydari.contentcloud.domaingenerator.generators;

import java.util.Collections;
import java.util.List;
import java.util.Random;

public class IntGenerator implements ValueGenerator {
    private final Random rand;
    private final int begin;
    private final int end;

    public IntGenerator(Random rand, int begin, int end) {
        this.rand = rand;
        this.begin = begin;
        this.end = end;
    }

    public String generate() {
        int i = rand.nextInt(end - begin);
        return String.valueOf(i);
    }

    public long uniqueEntries() {
        return end - begin;
    }

    public String generatedType() {
        return "long";
    }
}
