package be.heydari.contentcloud.domaingenerator.generators;

import java.util.List;

public class NullGenerator implements ValueGenerator<Object> {

    @Override
    public Object generate() {
        return null;
    }

    @Override
    public String generateString() {
        return null;
    }

    @Override
    public String generatedType() {
        return "string";
    }

    @Override
    public long uniqueEntryCount() {
        return 0;
    }

    @Override
    public List<Object> uniqueEntries() {
        return null;
    }
}
