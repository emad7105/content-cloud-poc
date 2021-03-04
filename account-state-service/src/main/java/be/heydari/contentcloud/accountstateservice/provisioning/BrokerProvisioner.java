package be.heydari.contentcloud.accountstateservice.provisioning;

import be.heydari.contentcloud.accountstateservice.Broker;
import be.heydari.contentcloud.*;
import be.heydari.contentcloud.accountstateservice.BrokerRepository;
import be.heydari.contentcloud.domaingenerator.Generators;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BrokerProvisioner {

    private BrokerRepository brokerRepository;

    @Autowired
    public BrokerProvisioner(BrokerRepository brokerRepository) {
        this.brokerRepository = brokerRepository;
    }

    public void provision() {
        Broker broker = new Broker();
        broker.setId(5l);
        broker.setName("Bert the Broker");
        brokerRepository.save(broker);
    }

}
