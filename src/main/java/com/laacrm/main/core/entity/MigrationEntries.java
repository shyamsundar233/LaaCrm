package com.laacrm.main.core.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Data
public class MigrationEntries {

    @Id
    @GeneratedValue(generator = "id-generator")
    @GenericGenerator(name = "id-generator", strategy = "com.laacrm.main.core.entity.PKGenerator")
    private Long entryId;

    private String migrationName;

    private Integer migrationStatus;

    private Integer failureCount;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime startTime;

    @UpdateTimestamp
    private LocalDateTime endTime;


}
