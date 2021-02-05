package be.heydari.contentcloud.gateway.lazyabac;

import lombok.Data;

@Data
public class OpaInput {

    private String token;

    public OpaInput(String token) {
        this.token = token;
    }
}
