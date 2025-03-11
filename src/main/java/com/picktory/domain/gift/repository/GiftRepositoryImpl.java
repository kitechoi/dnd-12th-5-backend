package com.picktory.domain.gift.repository;

import com.picktory.common.repository.JdbcBatchExecutor;
import com.picktory.domain.gift.entity.Gift;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.stereotype.Repository;
import org.springframework.util.StopWatch;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class GiftRepositoryImpl implements GiftRepositoryCustom {

    private final JdbcBatchExecutor jdbcBatchExecutor;

    @Override
    public void bulkInsertGifts(List<Gift> gifts) {
        if (gifts == null || gifts.isEmpty()) {
            return;
        }
        String sql = "INSERT INTO gifts (bundle_id, name, message, purchase_url, created_at, is_responsed) " +
                "VALUES (?, ?, ?, ?, NOW(), ?)";

        jdbcBatchExecutor.executeBatch(sql, new BatchPreparedStatementSetter() {
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
    }
}
