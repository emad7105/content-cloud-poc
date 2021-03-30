package be.heydari.contentcloud.accountstatepostfilter;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

import static javax.persistence.GenerationType.AUTO;

@Entity
@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AccountStatePostfilter {

    @Id
    @GeneratedValue(strategy = AUTO)
    private Long id;

    private String name;
    private String type;

//    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
//    private List<AccountStateAttribute> attributes;

    private int accountState;
    private String brokerName;
    private String requiredRole;
    private String clearanceLevel;
    private boolean probation;
    private String Attribute0;
    private String Attribute1;
    private String Attribute2;
    private String Attribute3;
    private String Attribute4;
    private String Attribute5;
    private String Attribute6;
    private String Attribute7;
    private String Attribute8;
    private String Attribute9;
}
