package com.skillbill.hbaserepository.repository;

import static org.hamcrest.CoreMatchers.is;

import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Assert;
import org.junit.Test;

import com.google.inject.Inject;
import com.skillbill.hbaserepository.Bean3;
import com.skillbill.hbaserepository.repository.HRepository.HbaseRowKey;

public class Bean3TestIT extends BasicTest {	

	@Inject
    private HRepository<Bean3> repository;

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
        
        final Bean3 item = buildSuppressionEvent(key);
		repository.save(item);
        
        final List<Bean3> result = repository.get(new HbaseRowKey(null, key.toString(), Bean3.class));
        
        Assert.assertNotNull(result);
        Assert.assertThat(result.size(), is(1));
        Assert.assertEquals(item, result.get(0));
    }
    
    @Test(expected = RuntimeException.class)
    public void saveAndRemove() {
    	final UUID key = UUID.randomUUID();
        
        final Bean3 item = buildSuppressionEvent(key);
		repository.save(item);
		
		repository.remove(item);
		
		final List<Bean3> result = repository.get(new HbaseRowKey(null, item.getRecipient(), Bean3.class));
        
        Assert.assertNotNull(result);
        Assert.assertThat(result.size(), is(0));		
    }
  
    @Test(expected = RuntimeException.class)
    public void onlyRemove() {
    	final UUID key = UUID.randomUUID();
        
        final Bean3 item = buildSuppressionEvent(key);
		
		final Bean3 result = repository.remove(item);
        Assert.assertNull(result);		
    }
    
    @Test(expected = RuntimeException.class)
    public void saveAndUpdate() {
		final UUID key = UUID.randomUUID();
        final Bean3 item = buildSuppressionEvent(key);
        
        repository.save(item);
        final Bean3 suppressionEvent = repository.get(new HbaseRowKey(null, item.getRecipient(), Bean3.class)).get(0);
        
        suppressionEvent.setReason("new reason");
        
        repository.update(suppressionEvent);
        
        final Bean3 updatedEvent = repository.get(new HbaseRowKey(null, item.getRecipient(), Bean3.class)).get(0);        
        Assert.assertEquals(suppressionEvent, updatedEvent);        
    }
    
    @Test(expected = RuntimeException.class)
    public void saveAndUpdateKey() {
		final UUID key = UUID.randomUUID();
        final Bean3 item = buildSuppressionEvent(key);
        
        repository.save(item);
        final Bean3 saveResult = repository.get(new HbaseRowKey(null, item.getRecipient(), Bean3.class)).get(0);
                
        final UUID key2 = UUID.randomUUID();
        saveResult.setRecipient(key2.toString());
        repository.update(saveResult);
        
        final List<Bean3> updateResults = repository.get(new HbaseRowKey(null, key2.toString(), Bean3.class));
        Assert.assertEquals(1, updateResults.size());        
    }

    @Test(expected = RuntimeException.class)
    public void olyGet() {
    	final UUID key = UUID.randomUUID();
    	
        final List<Bean3> result = repository.get(new HbaseRowKey(null, key.toString(), Bean3.class));
        
        Assert.assertNotNull(result);
        Assert.assertThat(result.size(), is(0));    	
    }
    
    /*
     * no test under
     */
    
	private Bean3 buildSuppressionEvent(final UUID key) {
		return new Bean3(key.toString(), "reason-" + RandomStringUtils.randomNumeric(9));
	}    
    
}
