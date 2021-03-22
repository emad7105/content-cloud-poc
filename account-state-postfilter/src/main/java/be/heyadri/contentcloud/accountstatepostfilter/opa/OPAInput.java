package be.heyadri.contentcloud.accountstatepostfilter.opa;

import be.heyadri.contentcloud.accountstatepostfilter.AccountStateAttribute;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;

import java.util.List;

@Data
public class OPAInput {
    private String token;

    private List<AccountStateAttribute> attributes;

    public OPAInput(String token, List<AccountStateAttribute> attributes) {
        this.token = token;
        this.attributes = attributes;
    }

    public String toJson() throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(this);
    }
}
