package com.picktory.domain.gift.repository;

import com.picktory.domain.gift.entity.GiftImage;
import java.util.List;

public interface GiftImageRepositoryCustom {
    List<GiftImage> bulkInsertGiftImages(List<GiftImage> giftImages);
}
