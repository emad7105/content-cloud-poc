package be.heydari.lazyabacfilter.config;

import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@ToString
public class Config {
    private String mode;
    private String abacQuery;
    private List<String> abacUnknowns;
    private List<Rule> rules;

    public Config() {

    }

    public Config(String mode, String abacQuery, List<String> abacUnknowns, List<Rule> rules) {
        this.mode = mode;
        this.abacQuery = abacQuery;
        this.abacUnknowns = abacUnknowns;
        this.rules = rules;
    }
}
