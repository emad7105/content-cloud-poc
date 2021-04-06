package be.heydari.contentcloud.accountstatehardcoded;

import java.util.List;
import java.util.Map;

public interface AccountStateRedactor {
    List<AccountState> query(Map<String, Object> token, int size);
}
