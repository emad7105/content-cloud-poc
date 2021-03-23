package be.heydari.contentcloud.domaingenerator.generators;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class IntGenerator implements ValueGenerator<Integer> {
    private final Random rand;
    private final int begin;
    private final int end;

    public IntGenerator(Random rand, int begin, int end) {
        this.rand = rand;
        this.begin = begin;
        this.end = end;
    }

    @Override
    public Integer generate() {
        return rand.nextInt(end - begin) + begin;
    }

    @Override
    public String generateString() {
        var i = generate();
        return String.valueOf(i);
    }

    @Override
    public String generatedType() {
        return "long";
    }

    @Override
    public long uniqueEntryCount() {
        return end-begin;
    }

    @Override
    public List<Integer> uniqueEntries() {
        var list = new ArrayList<Integer>();
        for (int i = begin; i != end; i++) {
            list.add(i);
        }

        return list;
    }
}
