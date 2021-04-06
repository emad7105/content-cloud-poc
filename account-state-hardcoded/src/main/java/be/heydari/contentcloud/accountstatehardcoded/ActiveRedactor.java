package be.heydari.contentcloud.accountstatehardcoded;

public class ActiveRedactor {
    // todo set default redactor
    private AccountStateRedactor redactor = null;
    private String redactorName = "default";

    synchronized String getName() {
        return redactorName;
    }

    synchronized AccountStateRedactor get() {
        return redactor;
    }

    synchronized void set(String name, AccountStateRedactor redactor) {
        this.redactorName = name;
        this.redactor = redactor;
    }
}
