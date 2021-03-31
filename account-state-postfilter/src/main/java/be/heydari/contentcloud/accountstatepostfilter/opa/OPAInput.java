package be.heydari.contentcloud.accountstatepostfilter.opa;

import be.heydari.contentcloud.accountstatepostfilter.AccountState;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;

@Data
public class OPAInput {
    private String token;

    private AccountState accountState;

    public OPAInput(String token, AccountState accountState) {
        this.token = token;
        this.accountState = accountState;
    }

    public String toJson() throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(this);
    }
}
