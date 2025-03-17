package com.picktory.domain.gift.repository;

import com.picktory.common.repository.JdbcBatchExecutor;
import com.picktory.domain.gift.entity.Gift;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.stereotype.Repository;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class GiftRepositoryImpl implements GiftRepositoryCustom {

    private final JdbcBatchExecutor jdbcBatchExecutor;

    @Override
    public List<Gift> bulkInsertGifts(List<Gift> gifts) {
        if (gifts == null || gifts.isEmpty()) {
            return new ArrayList<>();
        }

        String sql = "INSERT INTO gifts (bundle_id, name, message, purchase_url, created_at, is_responsed) " +
                "VALUES (?, ?, ?, ?, NOW(), ?)";

        List<Long> generatedIds = jdbcBatchExecutor.executeBatchAndGetKeys(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                Gift gift = gifts.get(i);
                ps.setLong(1, gift.getBundleId());
                ps.setString(2, gift.getName());
                ps.setString(3, gift.getMessage());
                ps.setString(4, gift.getPurchaseUrl());
                ps.setBoolean(5, false);
            }

            @Override
            public int getBatchSize() {
                return gifts.size();
            }
        });

        List<Gift> savedGifts = new ArrayList<>();
        for (int i = 0; i < gifts.size(); i++) {
            Gift gift = gifts.get(i);
            savedGifts.add(Gift.builder()
                    .id(generatedIds.get(i))
                    .bundleId(gift.getBundleId())
                    .name(gift.getName())
                    .message(gift.getMessage())
                    .purchaseUrl(gift.getPurchaseUrl())
                    .isResponsed(false)
                    .createdAt(LocalDateTime.now())
                    .build()
            );
        }
        return savedGifts;
    }
}

