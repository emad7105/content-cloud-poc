package be.heyadri.contentcloud.accountstatepostfilter;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static javax.persistence.GenerationType.AUTO;

@Entity
@NoArgsConstructor
@Data
public class AccountStateAttribute {
    @Id
    @GeneratedValue(strategy = AUTO)
    @JsonIgnore
    private Long id;

    @JoinColumn
    @ManyToOne
    @JsonIgnore
    private AccountState accountState;

    private String name;
    private String value;
}
