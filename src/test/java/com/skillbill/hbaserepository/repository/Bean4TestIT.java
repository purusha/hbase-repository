package com.skillbill.hbaserepository.repository;

import static org.hamcrest.CoreMatchers.is;

import java.util.List;
import java.util.UUID;

import org.junit.Assert;
import org.junit.Test;

import com.google.inject.Inject;
import com.skillbill.hbaserepository.Bean4;
import com.skillbill.hbaserepository.repository.HRepository.HbaseRowKey;

public class Bean4TestIT extends BasicTest {

	@Inject
    private HRepository<Bean4> repository;

    @Test
    public void saveAndGetTb4() {
        final UUID key = UUID.randomUUID();
        
        final Bean4 item = new Bean4(key.toString(), 1, 1L, "1", UUID.randomUUID());
        repository.save(item);
        
        final List<Bean4> result = repository.get(new HbaseRowKey(null, key.toString(), Bean4.class));
        
        Assert.assertNotNull(result);
        Assert.assertThat(result.size(), is(1));
        Assert.assertEquals(item, result.get(0));
    }
}
