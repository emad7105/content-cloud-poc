package be.heydari.contentcloud.accountstatepostfilter;

import java.util.List;

public interface Postfilter {
    List<AccountState> filter(String token, int size);
}
