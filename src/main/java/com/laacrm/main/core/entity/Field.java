package com.laacrm.main.core.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

import java.util.ArrayList;
import java.util.List;

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

    @OneToMany(mappedBy = "field")
    @JsonManagedReference
    private List<FieldProperties> fieldProperties = new ArrayList<>();

    public Field() {}

    public Field(String fieldName, Integer fieldType, List<FieldProperties> fieldProperties) {
        this.fieldName = fieldName;
        this.fieldType = fieldType;
        this.fieldProperties = fieldProperties;
    }

}
