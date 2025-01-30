package com.laacrm.main.core.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

import java.util.List;

@Entity
@Data
public class Layout {

    @Id
    @GeneratedValue(generator = "id-generator")
    @GenericGenerator(name = "id-generator", strategy = "com.laacrm.main.core.entity.PKGenerator")
    private Long layoutId;

    private String layoutName;

    @ManyToOne
    @JoinColumn(name = "module_id", nullable = false)
    @JsonBackReference
    @ToString.Exclude
    private Module module;

    @OneToMany(mappedBy = "layout", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Field> fields;

    private boolean isDefault;

    public Layout() {}

    public Layout(String layoutName, boolean isDefault) {
        this.layoutName = layoutName;
        this.isDefault = isDefault;
    }

    public Layout(Long layoutId, String layoutName, List<Field> fields, boolean isDefault) {
        this.layoutId = layoutId;
        this.layoutName = layoutName;
        this.fields = fields;
        this.isDefault = isDefault;
    }
}
