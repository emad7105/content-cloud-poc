package be.heydari.contentcloud.accountstateservice;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AccountStateAttributeRepository extends CrudRepository<AccountStateAttribute, Long> {
}
