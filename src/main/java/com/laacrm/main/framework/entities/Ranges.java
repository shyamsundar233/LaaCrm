package com.laacrm.main.framework.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

@Entity
@Data
public class Ranges {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long rangeId;

    private Long startRange;

    private Long endRange;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "tenant_id")
    @JsonIgnore
    @ToString.Exclude
    private Tenant tenant;

}
