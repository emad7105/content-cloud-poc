package be.heydari.contentcloud.accountstatepostfilter.provisioning;

import be.heydari.contentcloud.accountstatepostfilter.AccountStatePostfilter;
import be.heydari.contentcloud.accountstatepostfilter.AccountStateRepository;
import be.heydari.contentcloud.domaingenerator.Generators;
import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class Provisioner {
    private Logger LOGGER = Logger.getLogger(Provisioner.class);

    private AccountStateRepository accountStateRepository;

    @Autowired
    public Provisioner(AccountStateRepository accountStateRepository) {
        this.accountStateRepository = accountStateRepository;
    }

    public void provision() {
        Generators.HardcodedGenerator generator = new Generators.HardcodedGenerator();

        List<String> brokers = generator.getBroker().uniqueEntries();

        LOGGER.info("account state repository: " + accountStateRepository.count() + " entities present");
        if (System.getenv("DB_INIT").equals("clear")) {
            // clear database at start
            accountStateRepository.deleteAll();

            LOGGER.info("cleared account state repository: " + accountStateRepository.count() + " entities remaining");
        }

        for (int i = 0; i != 100; i ++) {
            AccountStatePostfilter state = new AccountStatePostfilter();
            String broker = brokers.get(Generators.rand.nextInt(brokers.size()));
            state.setBrokerName(broker);

            System.out.println("setting broker with name: " + broker);

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
