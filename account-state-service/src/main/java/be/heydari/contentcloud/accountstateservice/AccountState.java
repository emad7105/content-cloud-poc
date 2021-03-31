package be.heydari.contentcloud.accountstateservice;

import static javax.persistence.GenerationType.AUTO;

import javax.persistence.*;

import org.springframework.content.commons.annotations.ContentId;
import org.springframework.content.commons.annotations.ContentLength;
import org.springframework.content.commons.annotations.MimeType;
import org.springframework.versions.AncestorId;
import org.springframework.versions.AncestorRootId;
import org.springframework.versions.LockOwner;
import org.springframework.versions.SuccessorId;
import org.springframework.versions.VersionLabel;
import org.springframework.versions.VersionNumber;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class AccountState {

    public AccountState(AccountState state) {
        this.contentId = state.getContentId();
        this.contentLength = state.getContentLength();
        this.mimeType = state.getMimeType();
        this.broker = state.getBroker();
    }

    @Id
    @GeneratedValue(strategy = AUTO)
    private Long id;

    @ContentId
    private String contentId;

    @ContentLength
    private Long contentLength;

    @MimeType
    private String mimeType;

    private String name;
    private String type;

    @Version
    private Long vstamp;

    @LockOwner
    private String lockOwner;

    @AncestorId
    private Long ancestorId;

    @AncestorRootId
    private Long ancestralRootId;

    @SuccessorId
    private Long successorId;

    @VersionNumber
    private String version;

    @VersionLabel
    private String label;

    @JoinColumn
    @ManyToOne
    private Broker broker;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch=FetchType.EAGER)
    private List<AccountStateAttribute> attributes;

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
//    private String Attribute25;
//    private String Attribute26;
//    private String Attribute27;
//    private String Attribute28;
//    private String Attribute29;
}
