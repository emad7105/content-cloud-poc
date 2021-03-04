package be.heydari.contentcloud.accountstateservice.provisioning;

import be.heydari.contentcloud.accountstateservice.*;
import be.heydari.contentcloud.domaingenerator.Generators;
import be.heydari.contentcloud.domaingenerator.generators.StringGenerator;
import be.heydari.contentcloud.domaingenerator.generators.ValueGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class Provisioner {
    private BrokerRepository brokerRepository;
    private AccountStateRepository accountStateRepository;
    private AccountStateAttributeRepository accountStateAttributeRepository;

    @Autowired
    public Provisioner(BrokerRepository brokerRepository, AccountStateRepository accountStateRepository, AccountStateAttributeRepository accountStateAttributeRepository) {
        this.brokerRepository = brokerRepository;
        this.accountStateRepository = accountStateRepository;
        this.accountStateAttributeRepository = accountStateAttributeRepository;
    }

    public void provision() {
        Generators generators = Generators.Example1();

        StringGenerator brokerGen = (StringGenerator) generators.getSingleGenerators().get("broker");
        List<Broker> brokers = brokerGen.getStrings().stream().map(broker -> {
            Broker b = new Broker();
            b.setName(broker);
            brokerRepository.save(b);
            return b;
        }).collect(Collectors.toList());

        for (int i = 0; i != 100; i++) {
            AccountState state = new AccountState();

            state.setBroker(
                brokers.get(
                    Generators.rand.nextInt(brokers.size())
                )
            );

            List<AccountStateAttribute> attributes = new ArrayList<>();

            Map<String, ValueGenerator> singles = generators.getSingleGenerators();
            for (String key: singles.keySet()) {

                if (key.equals("broker")) {
                    continue;
                }
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
