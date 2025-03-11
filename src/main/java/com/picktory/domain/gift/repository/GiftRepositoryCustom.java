package com.picktory.domain.gift.repository;

import com.picktory.domain.gift.entity.Gift;
import java.util.List;

public interface GiftRepositoryCustom {
    void bulkInsertGifts(List<Gift> gifts);
}
