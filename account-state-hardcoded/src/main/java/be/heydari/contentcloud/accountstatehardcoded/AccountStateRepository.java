package be.heydari.contentcloud.accountstatehardcoded;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AccountStateRepository extends JpaRepository<AccountState, Long> {

    @Query("select a from AccountState a where a.brokerName = :brokerName")
    List<AccountState> policies(@Param("brokerName") String brokerName, Pageable pageable);

    @Query("select a from AccountState a where a.selectivity0_01 = :selectivity0_01")
    List<AccountState> selector_0_01(@Param("selectivity0_01") boolean selectivity0_01, Pageable pageable);

    @Query("select a from AccountState a where a.selectivity0_1 = :selectivity0_1")
    List<AccountState> selector_0_1(@Param("selectivity0_1") boolean selectivity0_1, Pageable pageable);

    @Query("select a from AccountState a where a.selectivity1 = :selectivity1")
    List<AccountState> selector_1(@Param("selectivity1") boolean selectivity1, Pageable pageable);

    @Query("select a from AccountState a where a.selectivity10 = :selectivity10")
    List<AccountState> selector_10(@Param("selectivity10") boolean selectivity10, Pageable pageable);

    @Query("select a from AccountState a where a.selectivity20 = :selectivity20")
    List<AccountState> selector_20(@Param("selectivity20") boolean selectivity20, Pageable pageable);

    @Query("select a from AccountState a where a.selectivity40 = :selectivity40")
    List<AccountState> selector_40(@Param("selectivity40") boolean selectivity40, Pageable pageable);

    @Query("select a from AccountState a where a.selectivity60 = :selectivity60")
    List<AccountState> selector_60(@Param("selectivity60") boolean selectivity60, Pageable pageable);

    @Query("select a from AccountState a where a.selectivity80 = :selectivity80")
    List<AccountState> selector_80(@Param("selectivity80") boolean selectivity80, Pageable pageable);

    @Query("select a from AccountState a where a.selectivity100 = :selectivity100")
    List<AccountState> selector_100(@Param("selectivity100") boolean selectivity100, Pageable pageable);

    @Query("select a from AccountState a where a.attribute0 = :attribute0")
    List<AccountState> attr_count_1(@Param("attribute0") String attribute0);

    @Query("select a from AccountState a " +
        "where a.attribute0 = :attribute0 " +
        "and a.attribute1 = :attribute1 " +
        "and a.attribute2 = :attribute2 " +
        "and a.attribute3 = :attribute3 " +
        "and a.attribute4 = :attribute4"
    )
    List<AccountState> attr_count_5(
        @Param("attribute0") String attribute0,
        @Param("attribute1") String attribute1,
        @Param("attribute2") String attribute2,
        @Param("attribute3") String attribute3,
        @Param("attribute4") String attribute4
    );
    @Query("select a from AccountState a " +
        "where a.attribute0 = :attribute0 " +
        "and a.attribute1 = :attribute1 " +
        "and a.attribute2 = :attribute2 " +
        "and a.attribute3 = :attribute3 " +
        "and a.attribute4 = :attribute4 " +
        "and a.attribute5 = :attribute5 " +
        "and a.attribute6 = :attribute6 " +
        "and a.attribute7 = :attribute7 " +
        "and a.attribute8 = :attribute8 " +
        "and a.attribute9 = :attribute9 "
    )
    List<AccountState> attr_count_10 (
        @Param("attribute0") String attribute0,
        @Param("attribute1") String attribute1,
        @Param("attribute2") String attribute2,
        @Param("attribute3") String attribute3,
        @Param("attribute4") String attribute4,
        @Param("attribute5") String attribute5,
        @Param("attribute6") String attribute6,
        @Param("attribute7") String attribute7,
        @Param("attribute8") String attribute8,
        @Param("attribute9") String attribute9

    );

    @Query("select a from AccountState a " +
        "where a.attribute0 = :attribute0 " +
        "and a.attribute1 = :attribute1 " +
        "and a.attribute2 = :attribute2 " +
        "and a.attribute3 = :attribute3 " +
        "and a.attribute4 = :attribute4 " +
        "and a.attribute5 = :attribute5 " +
        "and a.attribute6 = :attribute6 " +
        "and a.attribute7 = :attribute7 " +
        "and a.attribute8 = :attribute8 " +
        "and a.attribute9 = :attribute9 " +
        "and a.attribute10 = :attribute10 " +
        "and a.attribute11 = :attribute11 " +
        "and a.attribute12 = :attribute12 " +
        "and a.attribute13 = :attribute13 " +
        "and a.attribute14 = :attribute14 "
    )
    List<AccountState> attr_count_15 (
        @Param("attribute0") String attribute0,
        @Param("attribute1") String attribute1,
        @Param("attribute2") String attribute2,
        @Param("attribute3") String attribute3,
        @Param("attribute4") String attribute4,
        @Param("attribute5") String attribute5,
        @Param("attribute6") String attribute6,
        @Param("attribute7") String attribute7,
        @Param("attribute8") String attribute8,
        @Param("attribute9") String attribute9,
        @Param("attribute10") String attribute10,
        @Param("attribute11") String attribute11,
        @Param("attribute12") String attribute12,
        @Param("attribute13") String attribute13,
        @Param("attribute14") String attribute14
    );

    @Query("select a from AccountState a " +
        "where a.attribute0 = :attribute0 " +
        "and a.attribute1 = :attribute1 " +
        "and a.attribute2 = :attribute2 " +
        "and a.attribute3 = :attribute3 " +
        "and a.attribute4 = :attribute4 " +
        "and a.attribute5 = :attribute5 " +
        "and a.attribute6 = :attribute6 " +
        "and a.attribute7 = :attribute7 " +
        "and a.attribute8 = :attribute8 " +
        "and a.attribute9 = :attribute9 " +
        "and a.attribute10 = :attribute10 " +
        "and a.attribute11 = :attribute11 " +
        "and a.attribute12 = :attribute12 " +
        "and a.attribute13 = :attribute13 " +
        "and a.attribute14 = :attribute14 " +
        "and a.attribute15 = :attribute15 " +
        "and a.attribute16 = :attribute16 " +
        "and a.attribute17 = :attribute17 " +
        "and a.attribute18 = :attribute18 " +
        "and a.attribute19 = :attribute19 "
    )
    List<AccountState> attr_count_20 (
        @Param("attribute0") String attribute0,
        @Param("attribute1") String attribute1,
        @Param("attribute2") String attribute2,
        @Param("attribute3") String attribute3,
        @Param("attribute4") String attribute4,
        @Param("attribute5") String attribute5,
        @Param("attribute6") String attribute6,
        @Param("attribute7") String attribute7,
        @Param("attribute8") String attribute8,
        @Param("attribute9") String attribute9,
        @Param("attribute10") String attribute10,
        @Param("attribute11") String attribute11,
        @Param("attribute12") String attribute12,
        @Param("attribute13") String attribute13,
        @Param("attribute14") String attribute14,
        @Param("attribute15") String attribute15,
        @Param("attribute16") String attribute16,
        @Param("attribute17") String attribute17,
        @Param("attribute18") String attribute18,
        @Param("attribute19") String attribute19
    );



    @Query("select a from AccountState a " +
        "where a.attribute0 = :attribute0 " +
        "and a.attribute1 = :attribute1 " +
        "and a.attribute2 = :attribute2 " +
        "and a.attribute3 = :attribute3 " +
        "and a.attribute4 = :attribute4 " +
        "and a.attribute5 = :attribute5 " +
        "and a.attribute6 = :attribute6 " +
        "and a.attribute7 = :attribute7 " +
        "and a.attribute8 = :attribute8 " +
        "and a.attribute9 = :attribute9 " +
        "and a.attribute10 = :attribute10 " +
        "and a.attribute11 = :attribute11 " +
        "and a.attribute12 = :attribute12 " +
        "and a.attribute13 = :attribute13 " +
        "and a.attribute14 = :attribute14 " +
        "and a.attribute15 = :attribute15 " +
        "and a.attribute16 = :attribute16 " +
        "and a.attribute17 = :attribute17 " +
        "and a.attribute18 = :attribute18 " +
        "and a.attribute19 = :attribute19 " +
        "and a.attribute10 = :attribute20 " +
        "and a.attribute11 = :attribute21 " +
        "and a.attribute12 = :attribute22 " +
        "and a.attribute13 = :attribute23 " +
        "and a.attribute14 = :attribute24 "
    )
    List<AccountState> attr_count_25 (
        @Param("attribute0") String attribute0,
        @Param("attribute1") String attribute1,
        @Param("attribute2") String attribute2,
        @Param("attribute3") String attribute3,
        @Param("attribute4") String attribute4,
        @Param("attribute5") String attribute5,
        @Param("attribute6") String attribute6,
        @Param("attribute7") String attribute7,
        @Param("attribute8") String attribute8,
        @Param("attribute9") String attribute9,
        @Param("attribute10") String attribute10,
        @Param("attribute11") String attribute11,
        @Param("attribute12") String attribute12,
        @Param("attribute13") String attribute13,
        @Param("attribute14") String attribute14,
        @Param("attribute15") String attribute15,
        @Param("attribute16") String attribute16,
        @Param("attribute17") String attribute17,
        @Param("attribute18") String attribute18,
        @Param("attribute19") String attribute19,
        @Param("attribute20") String attribute20,
        @Param("attribute21") String attribute21,
        @Param("attribute22") String attribute22,
        @Param("attribute23") String attribute23,
        @Param("attribute24") String attribute24
    );

}
