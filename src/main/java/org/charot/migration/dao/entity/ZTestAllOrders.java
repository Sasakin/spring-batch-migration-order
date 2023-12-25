package org.charot.migration.dao.entity;

import lombok.Data;

import java.util.Date;

@Data
public class ZTestAllOrders {
    private Long id;
    private String acc;
    private Integer orderTypeId;
    private Date dateOrder;
    private String info;
}
