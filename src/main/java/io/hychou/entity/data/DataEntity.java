package io.hychou.entity.data;

import io.hychou.common.datastructure.AbstractCrudDataStructure;
import io.hychou.common.Constant;
import io.hychou.common.SignificantField;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.ArrayList;
import java.util.List;

@Entity
public class DataEntity extends AbstractCrudDataStructure {
    @Id
    @Getter
    @Setter
    private String name;

    @Column(length= Constant.GB)
    @Getter
    @Setter
    private byte[] dataBytes;

    public DataEntity(){}

    public DataEntity(String name, byte[] dataBytes) {
        this.name = name;
        this.dataBytes = dataBytes;
    }

    @Override
    public List<SignificantField> significantFields() {
        List<SignificantField> fields = new ArrayList<>();
        fields.add(new SignificantField("name", name));
        return fields;
    }
}