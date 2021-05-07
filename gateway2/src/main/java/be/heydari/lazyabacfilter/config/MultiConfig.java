package be.heydari.lazyabacfilter.config;

import lombok.Data;
import lombok.ToString;
import org.springframework.util.Assert;

import java.util.List;

@Data
@ToString
public class MultiConfig {
    private List<Rule> rules;

    public MultiConfig() {

    }

    public MultiConfig(List<Rule> rules) {
        this.rules = rules;
    }

    public MultiConfig(Config config) {
        List<Rule> rules = config.getRules();
        Assert.notNull(rules, "multiple mode should have rules");

        this.rules = rules;
    }
}
