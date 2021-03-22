package be.heyadri.contentcloud.accountstatepostfilter;

import be.heyadri.contentcloud.accountstatepostfilter.opa.OPAClient;
import be.heyadri.contentcloud.accountstatepostfilter.opa.OPAClientConfig;
import be.heyadri.contentcloud.accountstatepostfilter.opa.OPAInput;
import brave.Tracer;
import com.fasterxml.jackson.annotation.JsonView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
public class AccountStateController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AccountStateController.class);
    private final AccountStateRepository accountStateRepository;
    private OPAClient opaClient;


    @Autowired
    public AccountStateController(AccountStateRepository accountStateRepository, OPAClientConfig config) {
        this.accountStateRepository = accountStateRepository;
        this.opaClient = new OPAClient(config);
    }

    @GetMapping("/accountstates")
    public List<AccountState> accountStates(@RequestHeader("Authorization") String token) {


        List<AccountState> states = new ArrayList<>();
        for(AccountState entry: accountStateRepository.findAll()) {
            String[] bearerArray = token.split("\\s+");
            OPAInput input = new OPAInput(bearerArray[1], entry.getAttributes());

            try {
                OPAClient.OPAResponse decision = this.opaClient.queryOPA(input);
                if (decision.result == true) {
                    states.add(entry);
                }
            } catch (IOException e) {
                LOGGER.error(e.getMessage(), e);
            }
        }

        return states;
    }
}
