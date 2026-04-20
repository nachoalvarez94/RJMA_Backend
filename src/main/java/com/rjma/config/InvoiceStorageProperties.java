package com.rjma.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "app.invoices")
@Getter
@Setter
public class InvoiceStorageProperties {

    /**
     * Base directory where invoice PDFs are stored.
     * Override with environment variable APP_INVOICES_STORAGE_PATH.
     *
     * Example application.properties:
     *   app.invoices.storage-path=${APP_INVOICES_STORAGE_PATH:/data/distridulce/facturas}
     */
    private String storagePath = "/data/distridulce/facturas";
}
