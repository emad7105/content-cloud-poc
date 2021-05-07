package be.heydari.lazyabacfilter.config;

import lombok.Data;
import lombok.ToString;
import org.springframework.util.Assert;

import java.util.List;

@Data
@ToString
public class SingleConfig {
    private String abacQuery;
    private List<String> abacUnknowns;

    public SingleConfig(String abacQuery, List<String> abacUnknowns) {
        this.abacQuery = abacQuery;
        this.abacUnknowns = abacUnknowns;
    }

    public SingleConfig(Config config) {
        String query = config.getAbacQuery();
        Assert.notNull(query, "single mode should have an abac query");
        List<String> unknowns = config.getAbacUnknowns();
        Assert.notNull(unknowns, "single mode should have abac unknowns");

        this.abacQuery = query;
        this.abacUnknowns = unknowns;
    }
}
