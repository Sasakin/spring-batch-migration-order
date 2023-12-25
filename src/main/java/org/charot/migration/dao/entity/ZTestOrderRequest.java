package org.charot.migration.dao.entity;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ZTestOrderRequest {
        
        private String msgId;

        private String accNumber;

        private String externalDocId;

        private BigDecimal amount;

        private Integer cnt;
}
