package org.charot.migration.dao.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ZTestMigrationLog {
    private String msgid;
    private Date dateSent;
    private Date dateReceived;
    private String status;
    private String message;
}
