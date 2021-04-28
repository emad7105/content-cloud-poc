package be.heydari.contentcloud.accountstatehardcoded.provisioning;


import be.heydari.contentcloud.accountstatehardcoded.AccountState;
import be.heydari.contentcloud.accountstatehardcoded.AccountStateRepository;
import be.heydari.contentcloud.domaingenerator.Generators;
import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Random;

@Component
public class Provisioner {
    private Logger LOGGER = Logger.getLogger(Provisioner.class);

    private AccountStateRepository accountStateRepository;

    @Autowired
    public Provisioner(AccountStateRepository accountStateRepository) {
        this.accountStateRepository = accountStateRepository;
    }

    public void provision(int count, boolean reset) {
        Generators.HardcodedGenerator generator = new Generators.HardcodedGenerator();

        List<String> brokers = generator.getBroker().uniqueEntries();

        LOGGER.info("account state repository: " + accountStateRepository.count() + " entities present");

        if (!reset && accountStateRepository.count() == count) {
            LOGGER.info("keeping old records, desired count count: " + count);
            return;
        }

        accountStateRepository.deleteAll();

        LOGGER.info("cleared account state repository: " + accountStateRepository.count() + " entities remaining");

        Random random = new Random();
        for (int i = 0; i != count; i ++) {
            AccountState state = new AccountState();
            String broker = brokers.get(Generators.rand.nextInt(brokers.size()));
            state.setBrokerName(broker);

            state.setAccountState(generator.getAccountState().generate());
            state.setRequiredRole(generator.getRoles().generate());
            state.setClearanceLevel(generator.getClearanceLevel().generate());
            state.setProbation(generator.getProbation().generate());

            state.setAttribute0(broker);
            state.setAttribute1(broker);
            state.setAttribute2(broker);
            state.setAttribute3(broker);
            state.setAttribute4(broker);
            state.setAttribute5(broker);
            state.setAttribute6(broker);
            state.setAttribute7(broker);
            state.setAttribute8(broker);
            state.setAttribute9(broker);
            state.setAttribute10(broker);
            state.setAttribute11(broker);
            state.setAttribute12(broker);
            state.setAttribute13(broker);
            state.setAttribute14(broker);
            state.setAttribute15(broker);
            state.setAttribute16(broker);
            state.setAttribute17(broker);
            state.setAttribute18(broker);
            state.setAttribute19(broker);
            state.setAttribute20(broker);
            state.setAttribute21(broker);
            state.setAttribute22(broker);
            state.setAttribute23(broker);
            state.setAttribute24(broker);

            // generate different selectivities
            state.setSelectivity0_01(random.nextDouble() < 0.0001); // 0.01%
            state.setSelectivity0_1(random.nextDouble() < 0.001); // 0.1 %
            state.setSelectivity1 (random.nextDouble() < 0.01); // 1%
            state.setSelectivity10(random.nextDouble() < 0.1); // 10%
            state.setSelectivity20(random.nextDouble() < 0.2); // 20%
            state.setSelectivity40(random.nextDouble() < 0.4); // 40%
            state.setSelectivity60(random.nextDouble() < 0.6); // 60%
            state.setSelectivity80((random.nextDouble() < 0.8)); // 80%
            state.setSelectivity100(true); // 100%

            accountStateRepository.save(state);
        }

        LOGGER.info("finished provisioning: created " + count + " records");
    }
}
