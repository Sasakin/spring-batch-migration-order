package org.charot.migration.common.properties.helperClasses;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ZTestDB {
    protected String user;
    protected String pass;
    protected String url;
    protected Boolean integratedSecurity;
    protected Boolean trustServerCertificate;
    protected Boolean encrypt;
    protected String trustStore;
    protected String trustStorePassword;
    protected String authenticationScheme;

    public String getFullURL() {
        return url
                + ";" + "integratedSecurity=" + integratedSecurity.toString()
                + ";" + "trustServerCertificate=" + trustServerCertificate.toString()
                + ";" + "encrypt=" + encrypt.toString()
                + ";" + "trustStore=" + trustStore
                + ";" + "trustStorePassword=" + trustStorePassword
                + ";" + "authenticationScheme=" + authenticationScheme
                ;
    }
}
