package com.picktory.domain.gift.repository;

import com.picktory.common.repository.JdbcBatchExecutor;
import com.picktory.domain.gift.entity.GiftImage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Repository
@RequiredArgsConstructor
public class GiftImageRepositoryImpl implements GiftImageRepositoryCustom {

    private final JdbcBatchExecutor jdbcBatchExecutor;

    @Override
    public List<GiftImage> bulkInsertGiftImages(List<GiftImage> giftImages) {
        if (giftImages == null || giftImages.isEmpty()) {
            return new ArrayList<>();
        }

        String sql = "INSERT INTO gift_images (gift_id, image_url, is_primary, uploaded_at) VALUES (?, ?, ?, NOW())";

        List<Long> generatedIds = jdbcBatchExecutor.executeBatchAndGetKeys(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                GiftImage giftImage = giftImages.get(i);
                ps.setLong(1, giftImage.getGift().getId());
                ps.setString(2, giftImage.getImageUrl());
                ps.setBoolean(3, giftImage.getIsPrimary());
            }

            @Override
            public int getBatchSize() {
                return giftImages.size();
            }
        });

        List<GiftImage> savedGiftImages = new ArrayList<>();
        for (int i = 0; i < giftImages.size(); i++) {
            GiftImage giftImage = giftImages.get(i);
            savedGiftImages.add(GiftImage.builder()
                    .id(generatedIds.get(i))
                    .gift(giftImage.getGift())
                    .imageUrl(giftImage.getImageUrl())
                    .isPrimary(giftImage.getIsPrimary())
                    .uploadedAt(LocalDateTime.now())
                    .build()
            );
        }

        log.info("배치 INSERT로 {}개의 선물 이미지를 저장했습니다.", savedGiftImages.size());
        return savedGiftImages;
    }
}
