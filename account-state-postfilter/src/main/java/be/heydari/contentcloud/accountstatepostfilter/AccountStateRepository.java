package be.heydari.contentcloud.accountstatepostfilter;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface AccountStateRepository extends PagingAndSortingRepository<AccountState, Long> {
    @Query("select a from AccountState a")
    Slice<AccountState> findAllSlice(Pageable pageable);
}