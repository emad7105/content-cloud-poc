package be.heydari.contentcloud.domaingenerator.generators;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class IntStringGenerator implements StringValueGenerator {
    private final Random rand;
    private final int begin;
    private final int end;

    public IntStringGenerator(Random rand, int begin, int end) {
        this.rand = rand;
        this.begin = begin;
        this.end = end;
    }

    public String generate() {
        int i = rand.nextInt(end - begin) + begin;
        return String.valueOf(i);
    }

    public long uniqueEntryCount() {
        return end - begin;
    }

    public List<String> uniqueEntries() {
        var list = new ArrayList<String>();
        for (int i = begin; i != end; i++) {
            list.add(String.valueOf(i));
        }

        return list;
    }

    public String generatedType() {
        return "long";
    }
}
