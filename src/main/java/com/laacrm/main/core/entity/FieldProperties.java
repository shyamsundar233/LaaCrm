package com.laacrm.main.core.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Data
public class FieldProperties {

    @Id
    @GeneratedValue(generator = "id-generator")
    @GenericGenerator(name = "id-generator", strategy = "com.laacrm.main.core.entity.PKGenerator")
    private Long propertyId;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "field_id")
    @JsonBackReference
    @ToString.Exclude
    private Field field;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "property_ref")
    @JsonManagedReference
    private FieldPropertiesRef property;

    private String propertyValue;

    public FieldProperties() {}

    public FieldProperties(Field field, FieldPropertiesRef property, String propertyValue) {
        this.field = field;
        this.property = property;
        this.propertyValue = propertyValue;
    }
}
