package be.heydari.contentcloud.accountstateservice;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

import static javax.persistence.GenerationType.AUTO;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class AccountStateAttribute {
    @Id
    @GeneratedValue(strategy = AUTO)
    private Long id;

    @JoinColumn
    @ManyToOne
    private AccountState accountState;

//    @JoinColumn
//    @ManyToOne
//    private Broker broker;

    private String name;
    private String value;
}
