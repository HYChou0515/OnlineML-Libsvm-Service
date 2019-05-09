package io.hychou.entity.data;

import io.hychou.common.datastructure.AbstractDataStructure;
import io.hychou.common.Constant;
import io.hychou.common.SignificantField;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class DataPointEntity extends AbstractDataStructure {
    private Double y;
    private List<IndexValueEntity> x;

    public DataPointEntity() {
    }

    public DataPointEntity(Double y, List<IndexValueEntity> x) {
        this.y = y;
        this.x = x;
    }

    public Double getY() {
        return y;
    }

    public void setY(Double y) {
        this.y = y;
    }

    public List<IndexValueEntity> getX() {
        return x;
    }

    public void setX(List<IndexValueEntity> x) {
        this.x = x;
    }

    public static DataPointEntity parseDataPoint(String line) {
        if (line == null) {
            throw new NullPointerException("Unable to parse null string");
        }

        // split space and ignore duplicate spaces
        String[] tokens = line.split("\\s+");
        Double y = Double.parseDouble(tokens[0]);
        List<IndexValueEntity> x = new ArrayList<>(tokens.length - 1);
        int lastIndex = -1;
        for (int i = 1; i < tokens.length; i++) {
            String[] keyvalue = tokens[i].split(":");
            int index = Integer.parseInt(keyvalue[0]);
            if (index < 0) {
                throw new NumberFormatException(String.format(
                        "Index should not be negative but found %d", index));
            }
            if (lastIndex >= index) {
                throw new NumberFormatException(String.format(
                        "Index should be strongly increasing but found this index is %d but last index is %d",
                        index, lastIndex));
            }
            lastIndex = index;
            IndexValueEntity indexValueEntity = new IndexValueEntity(index, Double.parseDouble(keyvalue[1]));
            x.add(indexValueEntity);
        }
        return new DataPointEntity(y, x);
    }

    @Override
    public List<SignificantField> significantFields() {
        List<SignificantField> fields = new ArrayList<>();
        fields.add(new SignificantField("y", y));
        fields.add(new SignificantField("x", x));
        return fields;
    }

    public static List<DataPointEntity> listOf(byte[] dataByteArray) throws IOException {
        InputStream inputStream = new ByteArrayInputStream(dataByteArray);
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        List<DataPointEntity> data = new ArrayList<>();
        while(reader.ready()) {
            String line = reader.readLine().trim();
            if( !line.equals(Constant.EMPTY_STRING)) {
                data.add(DataPointEntity.parseDataPoint(line));
            }
        }
        return data;
    }
}