package be.heydari.contentcloud.domaingenerator.generators;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public interface ValueGenerator<T> {
    T generate();

    String generateString();

    String generatedType();

    long uniqueEntryCount();

    List<T> uniqueEntries();

    default List<T> generateMultiple(int count) {
        var set = new HashSet<T>();

        while (set.size() != count) {
            var str = generate();
            set.add(str);
        }

        return new ArrayList<>(set);
    }

    default List<String> generateMultipleString(int count) {
        var set = new HashSet<String>();

        while (set.size() != count) {
            var str = generateString();
            set.add(str);
        }

        return new ArrayList<>(set);
    }
}
