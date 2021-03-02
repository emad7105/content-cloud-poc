import java.util.List;
import java.util.Random;

public class StringGenerator implements ValueGenerator {
    private final List<String> strings;
    private final Random rand;

    StringGenerator(List<String> strings, Random rand) {
        this.strings = strings;
        this.rand = rand;
    }

    public String generate() {
        return strings.get(rand.nextInt(strings.size()));
    }
}
