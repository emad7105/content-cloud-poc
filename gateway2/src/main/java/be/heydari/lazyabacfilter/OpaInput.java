package be.heydari.lazyabacfilter;

import lombok.Data;

@Data
public class OpaInput {

    private String token;

    public OpaInput(String token) {
        this.token = token;
    }
}
