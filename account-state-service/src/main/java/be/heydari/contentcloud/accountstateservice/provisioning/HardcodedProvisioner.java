package be.heydari.contentcloud.accountstateservice.provisioning;

import be.heydari.contentcloud.accountstateservice.AccountState;
import be.heydari.contentcloud.accountstateservice.AccountStateRepository;
import be.heydari.contentcloud.accountstateservice.Broker;
import be.heydari.contentcloud.accountstateservice.BrokerRepository;
import be.heydari.contentcloud.domaingenerator.Generators;
import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class HardcodedProvisioner {
    private Logger LOGGER = Logger.getLogger(HardcodedProvisioner.class);

    private BrokerRepository brokerRepository;
    private AccountStateRepository accountStateRepository;

    @Autowired
    public HardcodedProvisioner(BrokerRepository brokerRepository, AccountStateRepository accountStateRepository) {
        this.brokerRepository = brokerRepository;
        this.accountStateRepository = accountStateRepository;
    }

    public void provision() {
        LOGGER.info("provisioning database");

        LOGGER.info("account state repository: " + accountStateRepository.count() + " entities present");
        LOGGER.info("broker repository: " + accountStateRepository.count() + " entities present");

//        if (System.getenv("DB_INIT") != null && System.getenv("DB_INIT").equals("clear")) {
            // clear database at start
            accountStateRepository.deleteAll();
            brokerRepository.deleteAll();

            LOGGER.info("cleared account state repository: " + accountStateRepository.count() + " entities remaining");
            LOGGER.info("cleared broker repository: " + accountStateRepository.count() + " entities remaining");
//        }

        Generators.HardcodedGenerator generator = new Generators.HardcodedGenerator();
        List<Broker> brokers = generator.getBroker().uniqueEntries().stream().map(broker -> {
            Broker b = new Broker();
            b.setName(broker);
            brokerRepository.save(b);
            return b;
        }).collect(Collectors.toList());

        for (int i = 0; i != 100; i ++) {
            AccountState state = new AccountState();
            Broker broker = brokers.get(Generators.rand.nextInt(brokers.size()));
            state.setBroker(broker);
            state.setBrokerName(broker.getName());

            state.setAccountState(generator.getAccountState().generate());
            state.setRequiredRole(generator.getRoles().generate());
            state.setClearanceLevel(generator.getClearanceLevel().generate());
            state.setProbation(generator.getProbation().generate());
            state.setAttribute0(generator.getAttribute().generate());
            state.setAttribute1(generator.getAttribute().generate());
            state.setAttribute2(generator.getAttribute().generate());
            state.setAttribute3(generator.getAttribute().generate());
            state.setAttribute4(generator.getAttribute().generate());
            state.setAttribute5(generator.getAttribute().generate());
            state.setAttribute6(generator.getAttribute().generate());
            state.setAttribute7(generator.getAttribute().generate());
            state.setAttribute8(generator.getAttribute().generate());
            state.setAttribute9(generator.getAttribute().generate());

            accountStateRepository.save(state);
        }
    }
}
