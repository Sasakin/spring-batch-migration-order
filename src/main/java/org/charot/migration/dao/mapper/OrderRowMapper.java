package org.charot.migration.dao.mapper;

import org.charot.migration.dao.entity.ZTestOrderRequest;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class OrderRowMapper implements RowMapper<ZTestOrderRequest> {
    @Override
    public ZTestOrderRequest mapRow(ResultSet rs, int rowNum) throws SQLException {
        ZTestOrderRequest request = new ZTestOrderRequest();
        request.setMsgId(rs.getString("msg_id"));
        request.setAccNumber(rs.getString("AccNumber"));
        request.setExternalDocId(rs.getString("ExternalDocID"));
        request.setAmount(rs.getBigDecimal("amount"));
        request.setCnt(rs.getInt("cnt"));
        return request;
    }
}
