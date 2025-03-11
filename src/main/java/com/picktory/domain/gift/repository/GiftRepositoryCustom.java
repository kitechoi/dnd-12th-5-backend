package com.picktory.domain.gift.repository;

import com.picktory.domain.gift.entity.Gift;
import java.util.List;

public interface GiftRepositoryCustom {
    List<Gift> bulkInsertGifts(List<Gift> gifts);
}
