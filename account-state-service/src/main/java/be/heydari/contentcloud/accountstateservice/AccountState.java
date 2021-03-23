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
