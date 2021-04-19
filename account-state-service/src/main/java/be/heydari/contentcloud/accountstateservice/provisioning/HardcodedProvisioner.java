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
    private int scale = 1;

    @Autowired
    public HardcodedProvisioner(BrokerRepository brokerRepository, AccountStateRepository accountStateRepository) {
        this.brokerRepository = brokerRepository;
        this.accountStateRepository = accountStateRepository;
    }
    
    public void setScale(int scale) {
        this.scale = scale;
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

        int totalRecords = 100 * scale;

        for (int i = 0; i != totalRecords; i ++) {
            AccountState state = new AccountState();
            Broker broker = brokers.get(Generators.rand.nextInt(brokers.size()));
            state.setBroker(broker);
            state.setBrokerName(broker.getName());

            state.setAccountState(generator.getAccountState().generate());
            state.setRequiredRole(generator.getRoles().generate());
            state.setClearanceLevel(generator.getClearanceLevel().generate());
            state.setProbation(generator.getProbation().generate());

            state.setAttribute0(broker.getName());
            state.setAttribute1(broker.getName());
            state.setAttribute2(broker.getName());
            state.setAttribute3(broker.getName());
            state.setAttribute4(broker.getName());
            state.setAttribute5(broker.getName());
            state.setAttribute6(broker.getName());
            state.setAttribute7(broker.getName());
            state.setAttribute8(broker.getName());
            state.setAttribute9(broker.getName());
            state.setAttribute10(broker.getName());
            state.setAttribute11(broker.getName());
            state.setAttribute12(broker.getName());
            state.setAttribute13(broker.getName());
            state.setAttribute14(broker.getName());
            state.setAttribute15(broker.getName());
            state.setAttribute16(broker.getName());
            state.setAttribute17(broker.getName());
            state.setAttribute18(broker.getName());
            state.setAttribute19(broker.getName());
            state.setAttribute20(broker.getName());
            state.setAttribute21(broker.getName());
            state.setAttribute22(broker.getName());
            state.setAttribute23(broker.getName());
            state.setAttribute24(broker.getName());

            state.setSelectivity1 (i < 1  * scale);
            state.setSelectivity10(i < 10 * scale);
            state.setSelectivity20(i < 20 * scale);
            state.setSelectivity40(i < 40 * scale);
            state.setSelectivity60(i < 60 * scale);
            state.setSelectivity80(i < 80 * scale);
            state.setSelectivity100(true);

//            state.setAttribute25(generator.getAttribute().generate());
//            state.setAttribute26(generator.getAttribute().generate());
//            state.setAttribute27(generator.getAttribute().generate());
//            state.setAttribute28(generator.getAttribute().generate());
//            state.setAttribute29(generator.getAttribute().generate());
            accountStateRepository.save(state);
        }

        LOGGER.info("finished provisioning: created " + totalRecords + " records");
    }
}
