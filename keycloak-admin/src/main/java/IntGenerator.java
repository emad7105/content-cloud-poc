import java.util.Random;

public class IntGenerator implements ValueGenerator {
    private Random rand;
    private int begin;
    private int end;

    IntGenerator(Random rand, int begin, int end) {
        this.rand = rand;
        this.begin = begin;
        this.end = end;
    }

    public String generate() {
        int value = rand.nextInt(end - begin);
        return String.valueOf(value + begin);
    }
}
