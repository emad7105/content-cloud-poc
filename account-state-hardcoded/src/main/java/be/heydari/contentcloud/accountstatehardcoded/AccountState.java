package be.heydari.contentcloud.accountstatehardcoded;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static javax.persistence.GenerationType.AUTO;

@Entity
@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AccountState {

    @Id
    @GeneratedValue(strategy = AUTO)
    private Long id;

    private String name;
    private String type;

    private int accountState;
    private String brokerName;
    private String requiredRole;
    private String clearanceLevel;
    private boolean probation;

    private boolean selectivity1;
    private boolean selectivity10;
    private boolean selectivity20;
    private boolean selectivity40;
    private boolean selectivity60;
    private boolean selectivity80;
    private boolean selectivity100;

    private String attribute0;
    private String attribute1;
    private String attribute2;
    private String attribute3;
    private String attribute4;
    private String attribute5;
    private String attribute6;
    private String attribute7;
    private String attribute8;
    private String attribute9;
    private String attribute10;
    private String attribute11;
    private String attribute12;
    private String attribute13;
    private String attribute14;
    private String attribute15;
    private String attribute16;
    private String attribute17;
    private String attribute18;
    private String attribute19;
    private String attribute20;
    private String attribute21;
    private String attribute22;
    private String attribute23;
    private String attribute24;
}
