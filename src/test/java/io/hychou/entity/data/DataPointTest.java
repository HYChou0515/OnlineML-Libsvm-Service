package io.hychou.entity.data;

import io.hychou.common.DataStructureTest;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.StringJoiner;

import static org.junit.jupiter.api.Assertions.*;

public class DataPointTest extends DataStructureTest {

    private DataPointEntity d1;
    private String d1ToString = "DataPointEntity{y=1.0, x=[IndexValueEntity{index=2, value=3.0}, IndexValueEntity{index=5, value=6.0}]}";
    
    @Before
    public void setUp() {
        d1 = new DataPointEntity();
        d1.setY(1.0);
        d1.setX(Arrays.asList(new IndexValueEntity(2, 3.0), new IndexValueEntity(5, 6.0)));
    }
    
    @Test
    @Override
    public void equals_givenSelf_thenTrueShouldBeFound() {
        // given
        DataPointEntity d = d1;
        // when
        assertEqualsAndHaveSameHashCode(d1, d);
    }
    
    @Test
    @Override
    public void equals_givenSameObject_thenTrueShouldBeFound() {
        // given
        Object d = new DataPointEntity();
        ((DataPointEntity) d).setX(d1.getX());
        ((DataPointEntity) d).setY(d1.getY());
        // when
        assertEqualsAndHaveSameHashCode(d1, d);
    }
    
    @Test
    @Override
    public void equals_givenSame_thenTrueShouldBeFound() {
        // given
        DataPointEntity d = new DataPointEntity();
        d.setX(d1.getX());
        d.setY(d1.getY());
        // when
        assertEqualsAndHaveSameHashCode(d1, d);
    }
    
    @Test
    @Override
    public void equals_givenDiff_thenFalseShouldBeFound() {
        // given
        DataPointEntity d = new DataPointEntity();
        d.setX(d1.getX());
        d.setY(d1.getY()+1);
        // when
        assertNotEqualAndHaveDifferentHashCode(d1, d);
    }
    
    @Test
    @Override
    public void equals_givenNull_thenFalseShouldBeFound() {
        // given
        DataPointEntity n = null;
        // when
        assertNotEqualAndHaveDifferentHashCode(d1, n);
    }
    
    @Test
    @Override
    public void equals_givenAnotherObject_thenFalseShouldBeFound() {
        // given
        Integer n = new Integer(0);
        // when
        assertNotEqualAndHaveDifferentHashCode(d1, n);
    }
    
    @Test
    @Override
    public void toString_thenCorrectStringShouldBeFound() {
        String found = d1.toString();
        assertEquals(d1ToString, found, "toString");
    }
    
    @Test
    public void whenParseDataPoint_givenValidString_thenReturnDataPoint() {
        // given
        Double y = 1.0;
        List<IndexValueEntity> x = Arrays.asList(
                new IndexValueEntity(2, 3.0),
                new IndexValueEntity(4, 5.0),
                new IndexValueEntity(6, 2.0)
        );
        String str = String.format("%f %d:%f %d:%f %d:%f", y,
                x.get(0).getIndex(), x.get(0).getValue(),
                x.get(1).getIndex(), x.get(1).getValue(),
                x.get(2).getIndex(), x.get(2).getValue());
    
        // when
        DataPointEntity d = DataPointEntity.parseDataPoint(str);
    
        // verify
        assertAll(
                () -> assertEquals(y, d.getY(), "getY"),
                () -> assertEquals(x, d.getX(), "getX")
        );
    }
    
    @Test
    public void whenParseDataPoint_givenNullString_thenThrowNullPointerException() {
        assertThrows(NullPointerException.class, () -> DataPointEntity.parseDataPoint(null));
    }
    
    @Test
    public void whenParseDataPoint_givenInvalidFormat_thenThrowNumberFormatException() {
        assertThrows(NumberFormatException.class, () -> DataPointEntity.parseDataPoint("1a 2:3 4:5 -1:2"));
    }
    
    @Test
    public void whenParseDataPoint_givenNegativeIndex_thenThrowNumberFormatException() {
        assertThrows(NumberFormatException.class, () -> DataPointEntity.parseDataPoint("1 2:3 4:5 -1:2"));
    }
    
    @Test
    public void whenParseDataPoint_givenDecreasingIndex_thenThrowNumberFormatException() {
        assertThrows(NumberFormatException.class, () -> DataPointEntity.parseDataPoint("1 2:3 4:5 1:2"));
    }
    @Test
    public void whenByteArrayToData_givenValidString_thenCorrectStringShouldBeFound() throws IOException {
        // given
        StringJoiner stringJoiner = new StringJoiner(System.lineSeparator());
        stringJoiner.add(" 1  2:1   3:-1  9:1");
        stringJoiner.add("   ");
        stringJoiner.add(" -1  3:12  7:1  ");

        DataPointEntity d1 = new DataPointEntity(1.0, Arrays.asList(
                new IndexValueEntity(2, 1.0),
                new IndexValueEntity(3, -1.0),
                new IndexValueEntity(9, 1.0)));
        DataPointEntity d2 = new DataPointEntity(-1.0, Arrays.asList(
                new IndexValueEntity(3, 12.0),
                new IndexValueEntity(7, 1.0)));

        // when
        List<DataPointEntity> data = DataPointEntity.listOf(stringJoiner.toString().getBytes());
    
        // verify
        assertEquals(Arrays.asList(d1, d2), data);
    }
}
