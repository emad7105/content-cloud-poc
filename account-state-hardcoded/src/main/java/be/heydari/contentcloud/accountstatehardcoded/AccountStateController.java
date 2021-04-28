package be.heydari.contentcloud.accountstatehardcoded;

import be.heydari.contentcloud.accountstatehardcoded.util.JWT;
import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
public class AccountStateController {

    private static final Logger LOGGER = Logger.getLogger(AccountStateController.class);

    private final AccountStateRepository accountStateRepository;
    private final ActiveRedactor activeRedactor;

    @Autowired
    public AccountStateController(AccountStateRepository accountStateRepository, ActiveRedactor activePolicy) {
        this.accountStateRepository = accountStateRepository;
        this.activeRedactor = activePolicy;
        this.activeRedactor.set("policies",
            (token, size) -> {
                LOGGER.info("token: " + token.get("select_1"));
                return accountStateRepository.selector_1((Boolean) token.get("select_1"));
            }
        );
    }

    @GetMapping("/accountStates")
    public List<AccountState> accountStates(@RequestHeader("Authorization") String token, @RequestParam("size") Optional<Integer> size){
        Map<String, Object> jwt = JWT.parse(token);

        AccountStateRedactor redactor = this.activeRedactor.get();
        return redactor.query(jwt, size.orElse(100));
    }

    @GetMapping("/policy")
    public void postPolicy(@RequestParam("name") String policy) {
        LOGGER.info("policy name: " + policy);
        switch (policy) {
            case "policies":
                this.activeRedactor.set(
                    "policies",
                    (token, size) -> accountStateRepository.policies((String) token.get("broker"))
                );
            case "selectivity_1":
                this.activeRedactor.set(
                    "selector_1",
                    (token, size) -> accountStateRepository.selector_1((Boolean) token.get("select_1"))
                ); break;
            case "selectivity_10":
                this.activeRedactor.set(
                    "selector_10",
                    (token, size) -> accountStateRepository.selector_10((Boolean) token.get("select_10"))
                ); break;
            case "selectivity_20":
                this.activeRedactor.set(
                    "selector_20",
                    (token, size) -> accountStateRepository.selector_20((Boolean) token.get("select_20"))
                ); break;
            case "selectivity_40":
                this.activeRedactor.set(
                    "selector_40",
                    (token, size) -> accountStateRepository.selector_40((Boolean) token.get("select_40"))
                ); break;
            case "selectivity_60":
                this.activeRedactor.set(
                    "selector_60",
                    (token, size) -> accountStateRepository.selector_60((Boolean) token.get("select_60"))
                ); break;
            case "selectivity_80":
                this.activeRedactor.set(
                    "selector_80",
                    (token, size) -> accountStateRepository.selector_80((Boolean) token.get("select_80"))
                ); break;
            case "selectivity_100":
                this.activeRedactor.set(
                    "selector_100",
                    (token, size) -> accountStateRepository.selector_100((Boolean) token.get("select_100"))
                ); break;
            default:
                throw new IllegalArgumentException("Unknown policy: " + policy);
        }
    }

    @GetMapping("/policies")
    public String getPolicy() {
        return this.activeRedactor.getName();
    }
}
