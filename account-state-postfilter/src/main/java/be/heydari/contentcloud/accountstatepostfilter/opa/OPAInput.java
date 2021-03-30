package be.heydari.contentcloud.accountstatepostfilter.opa;

import be.heydari.contentcloud.accountstatepostfilter.AccountStatePostfilter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;

@Data
public class OPAInput {
    private String token;

    private AccountStatePostfilter accountState;

    public OPAInput(String token, AccountStatePostfilter accountState) {
        this.token = token;
        this.accountState = accountState;
    }

    public String toJson() throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(this);
    }
}
