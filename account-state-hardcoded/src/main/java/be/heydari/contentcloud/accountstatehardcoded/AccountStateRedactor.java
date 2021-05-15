package be.heydari.contentcloud.accountstatehardcoded;

import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

public interface AccountStateRedactor {
    Page<AccountState> query(Map<String, Object> token, int size);
}
