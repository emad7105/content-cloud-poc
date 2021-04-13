package be.heydari.contentcloud.accountstatehardcoded;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AccountStateRepository extends JpaRepository<AccountState, Long> {

    @Query("select a from AccountState a where a.brokerName = :brokerName")
    List<AccountState> policies(@Param("brokerName") String brokerName);

    @Query("select a from AccountState a where a.selectivity1 = :selectivity1")
    List<AccountState> selector_1(@Param("selectivity1") boolean selectivity1);

    @Query("select a from AccountState a where a.selectivity10 = :selectivity10")
    List<AccountState> selector_10(@Param("selectivity10") boolean selectivity10);

    @Query("select a from AccountState a where a.selectivity20 = :selectivity20")
    List<AccountState> selector_20(@Param("selectivity20") boolean selectivity20);

    @Query("select a from AccountState a where a.selectivity40 = :selectivity40")
    List<AccountState> selector_40(@Param("selectivity40") boolean selectivity40);

    @Query("select a from AccountState a where a.selectivity60 = :selectivity60")
    List<AccountState> selector_60(@Param("selectivity60") boolean selectivity60);

    @Query("select a from AccountState a where a.selectivity80 = :selectivity80")
    List<AccountState> selector_80(@Param("selectivity80") boolean selectivity80);

    @Query("select a from AccountState a where a.selectivity100 = :selectivity100")
    List<AccountState> selector_100(@Param("selectivity100") boolean selectivity100);
}
