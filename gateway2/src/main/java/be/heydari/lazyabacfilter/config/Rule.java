package be.heydari.lazyabacfilter.config;

import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@ToString
public class Rule {
    private String path;
    private String policy;
    private List<String> unknowns;
}
