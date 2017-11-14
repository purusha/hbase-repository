package com.skillbill.hbaserepository.repository;

import static org.hamcrest.CoreMatchers.is;

import java.util.List;
import java.util.UUID;

import org.junit.Assert;
import org.junit.Test;

import com.google.inject.Inject;
import com.skillbill.hbaserepository.Bean5;
import com.skillbill.hbaserepository.repository.HRepository.HbaseRowKey;

public class Bean5TestIT extends BasicTest {

    @Inject
    private HRepository<Bean5> repository;

    @Test
    public void saveAndGetTb5() {
        final UUID key = UUID.randomUUID();

        final Bean5 item = new Bean5(key, 1);
        repository.save(item);

        final List<Bean5> result = repository.get(
            new HbaseRowKey(null, key, Bean5.class)
        );

        Assert.assertNotNull(result);
        Assert.assertThat(result.size(), is(1));
        Assert.assertEquals(item, result.get(0));
    }
}
