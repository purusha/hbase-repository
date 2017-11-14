package com.skillbill.hbaserepository.repository;

import static org.hamcrest.CoreMatchers.is;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.junit.Assert;
import org.junit.Test;

import com.google.inject.Inject;
import com.skillbill.hbaserepository.Bean6;
import com.skillbill.hbaserepository.repository.HRepository.HbaseRowKey;

public class Bean6TestIT extends BasicTest {

	@Inject
    private HRepository<Bean6> repository;

    @Test(expected = RuntimeException.class)
    public void save() {
    	final UUID key = UUID.randomUUID();
    	
        repository.save(
    		buildSuppressionEvent(key)
		);
    }

    @Test(expected = RuntimeException.class)
    public void saveAndGet() {
        final UUID key = UUID.randomUUID();

        final Bean6 item = buildSuppressionEvent(key);
		repository.save(item);

        final List<Bean6> result = repository.get(new HbaseRowKey(null, key.toString(), Bean6.class));

        Assert.assertNotNull(result);
        Assert.assertThat(result.size(), is(0));
    }

    @Test(expected = RuntimeException.class)
    public void saveAndRemove() {
    	final UUID key = UUID.randomUUID();

        final Bean6 item = buildSuppressionEvent(key);
		repository.save(item);

		repository.remove(item);

		final List<Bean6> result = repository.get(new HbaseRowKey(null, item.getRecipient(), Bean6.class));

        Assert.assertNotNull(result);
        Assert.assertThat(result.size(), is(0));
    }


    @Test(expected = RuntimeException.class)
    public void onlyRemove() {
    	final UUID key = UUID.randomUUID();

        final Bean6 item = buildSuppressionEvent(key);

		final Bean6 result = repository.remove(item);
        Assert.assertNull(result);
    }

    @Test(expected = RuntimeException.class)
    public void olyGet() {
    	final UUID key = UUID.randomUUID();

        final List<Bean6> result = repository.get(new HbaseRowKey(null, key.toString(), Bean6.class));

        Assert.assertNotNull(result);
        Assert.assertThat(result.size(), is(0));
    }
    
    /*
     * no test under
     */
    
	private Bean6 buildSuppressionEvent(final UUID key) {
        final Map<String, Integer> hashMap = new HashMap<String, Integer>();
        hashMap.put("key1", 1);
        
        return new Bean6(key.toString(), hashMap);
	}    
    
}
