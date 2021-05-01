package be.heydari.contentcloud.accountstatepostfilter.provisioning;

import be.heydari.contentcloud.accountstatepostfilter.AccountState;
import be.heydari.contentcloud.accountstatepostfilter.AccountStateRepository;
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

    public void provision(int recordCount, int brokerCount, boolean reset) {
        Generators.HardcodedGenerator generator = new Generators.HardcodedGenerator(brokerCount);

        List<String> brokers = generator.getBroker().uniqueEntries();

        LOGGER.info("account state repository: " + accountStateRepository.count() + " entities present");

        if (!reset && accountStateRepository.count() == recordCount) {
            LOGGER.info("keeping old records, desired count count: " + recordCount);
            return;
        }

        accountStateRepository.deleteAll();

        LOGGER.info("cleared account state repository: " + accountStateRepository.count() + " entities remaining");

        Random random = new Random();
        for (int i = 0; i != recordCount; i ++) {
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
            state.setSelectivity0_01((i % 10_000) == 0);
            state.setSelectivity0_1((i % 1_000) == 0);
            state.setSelectivity1((i % 100) == 0);
            state.setSelectivity10((i % 10) == 0);
            // fields 20 to 80 will be left out of the final experiments
            // as they do not really provide interesting insights
            state.setSelectivity20(random.nextDouble() < 0.2);
            state.setSelectivity40(random.nextDouble() < 0.4);
            state.setSelectivity60(random.nextDouble() < 0.6);
            state.setSelectivity80(random.nextDouble() < 0.8);
            state.setSelectivity100(true);

//            state.setSelectivity0_01(random.nextDouble() < 0.0001); // 0.01%
//            state.setSelectivity0_1(random.nextDouble() < 0.001); // 0.1%
//            state.setSelectivity1 (random.nextDouble() < 0.01); // 1%
//            state.setSelectivity10(random.nextDouble() < 0.1); // 10%
//            state.setSelectivity20(random.nextDouble() < 0.2); // 20%
//            state.setSelectivity40(random.nextDouble() < 0.4); // 40%
//            state.setSelectivity60(random.nextDouble() < 0.6); // 60%
//            state.setSelectivity80(random.nextDouble() < 0.8); // 80%
//            state.setSelectivity100(true); // 100%


//            // generate different selectivities
//            state.setSelectivity1 ((i % 100) == 0); // 1/ 100
//            state.setSelectivity10((i % 10) == 0); // 1/10 of all records
//            state.setSelectivity20((i % 5) == 0); // 1/5 (20%)
//            state.setSelectivity40((i * 2) % 5 == 0); //
//            state.setSelectivity60((i * 3) % 5 == 0);
//            state.setSelectivity80((i * 4) % 5 == 0);
//            state.setSelectivity100(true);

//            state.setSelectivity1 (i < 1  * scale);
//            state.setSelectivity10(i < 10 * scale);
//            state.setSelectivity20(i < 20 * scale);
//            state.setSelectivity40(i < 40 * scale);
//            state.setSelectivity60(i < 60 * scale);
//            state.setSelectivity80(i < 80 * scale);
//            state.setSelectivity100(true);

            accountStateRepository.save(state);
        }

        LOGGER.info("finished provisioning: created " + recordCount + " records");
    }
}
