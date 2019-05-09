package io.hychou.entity.data;
import io.hychou.common.DataStructureTest;
import org.junit.Before;
import org.junit.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class IndexValueEntityTest extends DataStructureTest {

    private IndexValueEntity s1;
    private String s1ToString = "IndexValueEntity{index=1, value=2.0}";

    @Before
    public void setUp() {
        s1 = new IndexValueEntity();
        s1.setIndex(1);
        s1.setValue(2.0);
    }

    @Test
    @Override
    public void equals_givenSelf_thenTrueShouldBeFound() {
        // given
        IndexValueEntity s = s1;
        // when
        assertEqualsAndHaveSameHashCode(s1, s);
    }

    @Test
    @Override
    public void equals_givenSameObject_thenTrueShouldBeFound() {
        // given
        Object s = new IndexValueEntity();
        ((IndexValueEntity) s).setIndex(s1.getIndex());
        ((IndexValueEntity) s).setValue(s1.getValue());
        // when
        assertEqualsAndHaveSameHashCode(s1, s);
    }

    @Test
    @Override
    public void equals_givenSame_thenTrueShouldBeFound() {
        // given
        IndexValueEntity s = new IndexValueEntity();
        s.setIndex(s1.getIndex());
        s.setValue(s1.getValue());
        // when
        assertEqualsAndHaveSameHashCode(s1, s);
    }

    @Test
    @Override
    public void equals_givenDiff_thenFalseShouldBeFound() {
        // given
        IndexValueEntity s = new IndexValueEntity();
        s.setIndex(s1.getIndex());
        s.setValue(s1.getValue()+1);
        // when
        assertNotEqualAndHaveDifferentHashCode(s1, s);
    }

    @Test
    @Override
    public void equals_givenNull_thenFalseShouldBeFound() {
        // given
        IndexValueEntity n = null;
        // when
        assertNotEqualAndHaveDifferentHashCode(s1, n);
    }

    @Test
    @Override
    public void equals_givenAnotherObject_thenFalseShouldBeFound() {
        // given
        Integer n = new Integer(0);
        // when
        assertNotEqualAndHaveDifferentHashCode(s1, n);
    }

    @Test
    @Override
    public void toString_thenCorrectStringShouldBeFound() {
        String found = s1.toString();
        assertEquals(s1ToString, found, "toString");
    }
}