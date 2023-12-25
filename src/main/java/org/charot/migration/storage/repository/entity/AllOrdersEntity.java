package org.charot.migration.storage.repository.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Data
@Entity
@Table(name = "all_orders", schema = "storage")
public class AllOrdersEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "acc", nullable = false)
    private String acc;
    @Column(name = "order_type_id", nullable = false)
    private Integer orderTypeId;
    @Column(name = "date_order", nullable = false)
    private Date dateOrder;
    @Column(name = "info")
    private String info;
}