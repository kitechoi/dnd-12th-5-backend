package com.picktory.common.repository;

import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class JdbcBatchExecutor {
    private final JdbcTemplate jdbcTemplate;

    public List<Long> executeBatchAndGetKeys(String sql, BatchPreparedStatementSetter setter) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.batchUpdate(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            return ps;
        }, setter, keyHolder);

        List<Long> generatedIds = new ArrayList<>();
        for (Map<String, Object> key : keyHolder.getKeyList()) {
            generatedIds.add(((Number) key.get("GENERATED_KEY")).longValue());
        }
        return generatedIds;
    }
}

