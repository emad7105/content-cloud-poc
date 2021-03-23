package be.heydari.contentcloud.domaingenerator.generators;

public interface StringValueGenerator extends ValueGenerator<String> {
    @Override
    default String generateString() {
        return generate();
    }
}
