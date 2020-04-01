package it.bz.davinci.innovationscoreboard.utils.es;

import org.junit.Test;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.*;

public class EsIndexNameTest {

    @Test
    public void emptyPrefix_shouldReturnJustIndexName() {
        EsIndexName esIndexName = new EsIndexName("");

        final String prefixedIndexName = esIndexName.getPrefixedIndexName("test");

        assertThat(prefixedIndexName, equalTo("test"));
    }

    @Test
    public void prefixWithDashAtTheEnd_shouldReturnPrefixWithIndexName() {
        EsIndexName esIndexName = new EsIndexName("prefix-");

        final String prefixedIndexName = esIndexName.getPrefixedIndexName("test");

        assertThat(prefixedIndexName, equalTo("prefix-test"));
    }

    @Test
    public void prefixWithoutDashAtTheEnd_shouldReturnPrefixWithIndexName() {
        EsIndexName esIndexName = new EsIndexName("prefix");

        final String prefixedIndexName = esIndexName.getPrefixedIndexName("test");

        assertThat(prefixedIndexName, equalTo("prefix-test"));
    }
}