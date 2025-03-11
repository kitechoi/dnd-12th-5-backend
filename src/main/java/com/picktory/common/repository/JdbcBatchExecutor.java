package com.picktory.common.repository;

import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JdbcBatchExecutor {

    private final JdbcTemplate jdbcTemplate;

    public void executeBatch(String sql, List<Object[]> batchArgs) {
        jdbcTemplate.batchUpdate(sql, batchArgs);
    }

    public void executeBatch(String sql, BatchPreparedStatementSetter setter) {
        jdbcTemplate.batchUpdate(sql, setter);
    }
}
