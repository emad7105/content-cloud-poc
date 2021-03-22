package be.heyadri.contentcloud.accountstatepostfilter.provisioning;

import be.heyadri.contentcloud.accountstatepostfilter.AccountState;
import be.heyadri.contentcloud.accountstatepostfilter.AccountStateAttribute;
import be.heyadri.contentcloud.accountstatepostfilter.AccountStateRepository;
import be.heydari.contentcloud.domaingenerator.Generators;
import be.heydari.contentcloud.domaingenerator.generators.ValueGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class Provisioner {
    private AccountStateRepository accountStateRepository;

    @Autowired
    public Provisioner(AccountStateRepository accountStateRepository) {
        this.accountStateRepository = accountStateRepository;
    }

    public void provision() {
        Generators generators = Generators.Example1();

        for (int i = 0; i != 100; i++) {
            AccountState state = new AccountState();
            List<AccountStateAttribute> attributes = new ArrayList<>();
            Map<String, ValueGenerator> singles = generators.getSingleGenerators();
            for (String key: singles.keySet()) {
                AccountStateAttribute attr = new AccountStateAttribute();
                attr.setName(key);
                attr.setValue(singles.get(key).generate());
                attributes.add(attr);
            }

            Map<String, ValueGenerator> multiples = generators.getMultiValuedGenerators();
            for (String key: multiples.keySet()) {
                AccountStateAttribute attr = new AccountStateAttribute();
                attr.setName(key);
                attr.setValue(multiples.get(key).generate());
            }

            state.setAttributes(attributes);
            accountStateRepository.save(state);
        }
    }

}
