package org.charot.migration.dao.mapper;

import org.charot.migration.dao.entity.ZTestAllOrders;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ZTestAllOrdersRowMapper implements RowMapper<ZTestAllOrders> {
    @Override
    public ZTestAllOrders mapRow(ResultSet rs, int rowNum) throws SQLException {
        ZTestAllOrders allBlocks = new ZTestAllOrders();
        allBlocks.setId(rs.getLong("id"));
        allBlocks.setAcc(rs.getString("acc"));
        allBlocks.setOrderTypeId(rs.getInt("order_type_id"));
        allBlocks.setInfo(rs.getString("info"));
        return allBlocks;
    }
}
