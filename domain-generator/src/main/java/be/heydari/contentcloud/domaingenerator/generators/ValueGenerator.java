package be.heydari.contentcloud.domaingenerator.generators;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public interface ValueGenerator {
    String generate();

    String generatedType();

    long uniqueEntries();

    default List<String> generateMultiple(int count) {
        var set = new HashSet<String>();

        while (set.size() != count) {
            var str = generate();
            set.add(str);
        }

        return new ArrayList<>(set);
    }
}
