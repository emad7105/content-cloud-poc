package be.heyadri.contentcloud.accountstatepostfilter;

import org.springframework.context.annotation.Bean;
import org.springframework.data.repository.CrudRepository;

public interface AccountStateRepository extends CrudRepository<AccountState, Long> { }