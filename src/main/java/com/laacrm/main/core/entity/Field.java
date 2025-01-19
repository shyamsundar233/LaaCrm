package com.laacrm.main.core.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Data
public class Field {

    @Id
    @GeneratedValue(generator = "id-generator")
    @GenericGenerator(name = "id-generator", strategy = "com.laacrm.main.core.entity.PKGenerator")
    private Long fieldId;

    @ManyToOne
    @JoinColumn(name = "module_id", nullable = false)
    @JsonBackReference
    @ToString.Exclude
    private Module module;

    private String fieldName;

    private Integer fieldType;

    private String fieldValue;

    public Field() {}

    public Field(String fieldName, Integer fieldType) {
        this.fieldName = fieldName;
        this.fieldType = fieldType;
    }

}
