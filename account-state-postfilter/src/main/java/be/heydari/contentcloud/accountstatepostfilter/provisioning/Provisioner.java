package be.heydari.contentcloud.accountstatepostfilter.provisioning;

import be.heydari.contentcloud.accountstatepostfilter.AccountState;
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
    private int scale = 1;

    public void setScale(int scale) {
        this.scale = scale;
    }

    @Autowired
    public Provisioner(AccountStateRepository accountStateRepository) {
        this.accountStateRepository = accountStateRepository;
    }

    public void provision() {
        Generators.HardcodedGenerator generator = new Generators.HardcodedGenerator();

        List<String> brokers = generator.getBroker().uniqueEntries();

        LOGGER.info("account state repository: " + accountStateRepository.count() + " entities present");
//        if (System.getenv("DB_INIT").equals("clear")) {
            // clear database at start
            accountStateRepository.deleteAll();

            LOGGER.info("cleared account state repository: " + accountStateRepository.count() + " entities remaining");
//        }


        int totalRecords = 100 * scale;

        for (int i = 0; i != totalRecords; i ++) {
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

            state.setSelectivity1 (i < 1  * scale);
            state.setSelectivity10(i < 10 * scale);
            state.setSelectivity20(i < 20 * scale);
            state.setSelectivity40(i < 40 * scale);
            state.setSelectivity60(i < 60 * scale);
            state.setSelectivity80(i < 80 * scale);
            state.setSelectivity100(true);

            accountStateRepository.save(state);
        }

        LOGGER.info("finished provisioning: created " + totalRecords + " records");
    }
}
