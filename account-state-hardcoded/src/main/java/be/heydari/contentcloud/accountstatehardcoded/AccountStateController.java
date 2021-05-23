package be.heydari.contentcloud.accountstatehardcoded;

import be.heydari.contentcloud.accountstatehardcoded.util.JWT;
import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
                return accountStateRepository.selector_1((Boolean) token.get("select_1"), PageRequest.of(0, size));
            }
        );
    }

    @GetMapping("/accountStates")
    public Page<AccountState> accountStates(@RequestHeader("Authorization") String token, @RequestParam("size") Optional<Integer> size){
        Map<String, Object> jwt = JWT.parse(token);

        AccountStateRedactor redactor = this.activeRedactor.get();
        return redactor.query(jwt, size.orElse(100));
    }

    @GetMapping("/policy")
    public void postPolicy(@RequestParam("name") String policy) {
        LOGGER.info("policy name: " + policy);
        switch (policy) {
            case "tenant":
                this.activeRedactor.set(
                    "tenant",
                    (token, size) -> accountStateRepository.tenant((String) token.get("broker"), PageRequest.of(0, size))
                ); break;
            case "policies":
                this.activeRedactor.set(
                    "policies",
                    (token, size) -> accountStateRepository.policies((String) token.get("broker"), PageRequest.of(0, size))
                ); break;
            case "selectivity_0_01":
                this.activeRedactor.set(
                    "selector_0_01",
                    (token, size) -> accountStateRepository.selector_0_01((Boolean) token.get("select_0_01"), PageRequest.of(0, size))
                ); break;
            case "selectivity_0_1":
                this.activeRedactor.set(
                    "selector_0_1",
                    (token, size) -> accountStateRepository.selector_0_1((Boolean) token.get("select_0_1"), PageRequest.of(0, size))
                ); break;
            case "selectivity_1":
                this.activeRedactor.set(
                    "selector_1",
                    (token, size) -> accountStateRepository.selector_1((Boolean) token.get("select_1"), PageRequest.of(0, size))
                ); break;
            case "selectivity_10":
                this.activeRedactor.set(
                    "selector_10",
                    (token, size) -> accountStateRepository.selector_10((Boolean) token.get("select_10"), PageRequest.of(0, size))
                ); break;
            case "selectivity_20":
                this.activeRedactor.set(
                    "selector_20",
                    (token, size) -> accountStateRepository.selector_20((Boolean) token.get("select_20"), PageRequest.of(0, size))
                ); break;
            case "selectivity_40":
                this.activeRedactor.set(
                    "selector_40",
                    (token, size) -> accountStateRepository.selector_40((Boolean) token.get("select_40"), PageRequest.of(0, size))
                ); break;
            case "selectivity_60":
                this.activeRedactor.set(
                    "selector_60",
                    (token, size) -> accountStateRepository.selector_60((Boolean) token.get("select_60"), PageRequest.of(0, size))
                ); break;
            case "selectivity_80":
                this.activeRedactor.set(
                    "selector_80",
                    (token, size) -> accountStateRepository.selector_80((Boolean) token.get("select_80"), PageRequest.of(0, size))
                ); break;
            case "selectivity_100":
                this.activeRedactor.set(
                    "selector_100",
                    (token, size) -> accountStateRepository.selector_100((Boolean) token.get("select_100"), PageRequest.of(0, size))
                ); break;
            case "attr_count_1":
                this.activeRedactor.set(
                    "attribute_count_1",
                    (token, size) -> accountStateRepository.attr_count_1((String) token.get("attribute0"), PageRequest.of(0, size))
                ); break;
            case "attr_count_5":
                this.activeRedactor.set(
                    "attribute_count_5",
                    (token, size) -> accountStateRepository.attr_count_5(
                        (String) token.get("attribute0"),
                        (String) token.get("attribute1"),
                        (String) token.get("attribute2"),
                        (String) token.get("attribute3"),
                        (String) token.get("attribute4"),
                        PageRequest.of(0, size)
                    )
                ); break;
            case "attr_count_10":
                this.activeRedactor.set(
                    "attribute_count_10",
                    (token, size) -> accountStateRepository.attr_count_10(
                        (String) token.get("attribute0"),
                        (String) token.get("attribute1"),
                        (String) token.get("attribute2"),
                        (String) token.get("attribute3"),
                        (String) token.get("attribute4"),
                        (String) token.get("attribute5"),
                        (String) token.get("attribute6"),
                        (String) token.get("attribute7"),
                        (String) token.get("attribute8"),
                        (String) token.get("attribute9"),
                        PageRequest.of(0, size)
                    )
                ); break;
            case "attr_count_15":
                this.activeRedactor.set(
                    "attribute_count_15",
                    (token, size) -> accountStateRepository.attr_count_15(
                        (String) token.get("attribute0"),
                        (String) token.get("attribute1"),
                        (String) token.get("attribute2"),
                        (String) token.get("attribute3"),
                        (String) token.get("attribute4"),
                        (String) token.get("attribute5"),
                        (String) token.get("attribute6"),
                        (String) token.get("attribute7"),
                        (String) token.get("attribute8"),
                        (String) token.get("attribute9"),
                        (String) token.get("attribute10"),
                        (String) token.get("attribute11"),
                        (String) token.get("attribute12"),
                        (String) token.get("attribute13"),
                        (String) token.get("attribute14"),
                        PageRequest.of(0, size)
                    )
                ); break;
            case "attr_count_20":
                this.activeRedactor.set(
                    "attribute_count_20",
                    (token, size) -> accountStateRepository.attr_count_20(
                        (String) token.get("attribute0"),
                        (String) token.get("attribute1"),
                        (String) token.get("attribute2"),
                        (String) token.get("attribute3"),
                        (String) token.get("attribute4"),
                        (String) token.get("attribute5"),
                        (String) token.get("attribute6"),
                        (String) token.get("attribute7"),
                        (String) token.get("attribute8"),
                        (String) token.get("attribute9"),
                        (String) token.get("attribute10"),
                        (String) token.get("attribute11"),
                        (String) token.get("attribute12"),
                        (String) token.get("attribute13"),
                        (String) token.get("attribute14"),
                        (String) token.get("attribute15"),
                        (String) token.get("attribute16"),
                        (String) token.get("attribute17"),
                        (String) token.get("attribute18"),
                        (String) token.get("attribute19"),
                        PageRequest.of(0, size)
                    )
                ); break;
            case "attr_count_25":
                this.activeRedactor.set(
                    "attribute_count_25",
                    (token, size) -> accountStateRepository.attr_count_25(
                        (String) token.get("attribute0"),
                        (String) token.get("attribute1"),
                        (String) token.get("attribute2"),
                        (String) token.get("attribute3"),
                        (String) token.get("attribute4"),
                        (String) token.get("attribute5"),
                        (String) token.get("attribute6"),
                        (String) token.get("attribute7"),
                        (String) token.get("attribute8"),
                        (String) token.get("attribute9"),
                        (String) token.get("attribute10"),
                        (String) token.get("attribute11"),
                        (String) token.get("attribute12"),
                        (String) token.get("attribute13"),
                        (String) token.get("attribute14"),
                        (String) token.get("attribute15"),
                        (String) token.get("attribute16"),
                        (String) token.get("attribute17"),
                        (String) token.get("attribute18"),
                        (String) token.get("attribute19"),
                        (String) token.get("attribute20"),
                        (String) token.get("attribute21"),
                        (String) token.get("attribute22"),
                        (String) token.get("attribute23"),
                        (String) token.get("attribute24"),
                        PageRequest.of(0, size)
                    )
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
