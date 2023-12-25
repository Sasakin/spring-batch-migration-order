package org.charot.migration.order.repository.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;

@Data
@Entity
@Table(name = "order", schema = "orders")
public class OrderEntity {

        @Id
        @Column(name = "msg_id", nullable = false)
        private String msgId;

        @Column(name = "acc_number")
        private String accNumber;

        @Column(name = "external_doc_id")
        private String externalDocId;

        @Column(name = "amount")
        private BigDecimal amount;

        @Column(name = "cnt")
        private Integer cnt;

        @Column(name = "status")
        private String status;
}
