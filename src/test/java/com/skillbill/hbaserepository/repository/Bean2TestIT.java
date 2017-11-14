package com.skillbill.hbaserepository.repository;

import static org.hamcrest.CoreMatchers.is;

import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Assert;
import org.junit.Test;

import com.google.inject.Inject;
import com.skillbill.hbaserepository.Bean2;
import com.skillbill.hbaserepository.repository.HRepository.HbaseRowKey;

public class Bean2TestIT extends BasicTest {	

	@Inject
    private HRepository<Bean2> repository;

    @Test
    public void save() {
    	final UUID key = UUID.randomUUID();
    	
        repository.save(
    		buildSuppressionEvent(key)
		);
    }

    @Test
    public void saveAndGet() {
        final UUID key = UUID.randomUUID();
        
        final Bean2 item = buildSuppressionEvent(key);
		repository.save(item);
        
        final List<Bean2> result = repository.get(new HbaseRowKey(null, key.toString(), Bean2.class));
        
        Assert.assertNotNull(result);
        Assert.assertThat(result.size(), is(1));
        Assert.assertEquals(item, result.get(0));
    }
    
    @Test
    public void saveAndRemove() {
    	final UUID key = UUID.randomUUID();
        
        final Bean2 item = buildSuppressionEvent(key);
		repository.save(item);
		
		repository.remove(item);
		
		final List<Bean2> result = repository.get(new HbaseRowKey(null, item.getRecipient(), Bean2.class));
        
        Assert.assertNotNull(result);
        Assert.assertThat(result.size(), is(0));		
    }
  
    @Test
    public void onlyRemove() {
    	final UUID key = UUID.randomUUID();
        
        final Bean2 item = buildSuppressionEvent(key);
		
		final Bean2 result = repository.remove(item);
        Assert.assertNull(result);		
    }
    
    @Test
    public void saveAndUpdate() {
		final UUID key = UUID.randomUUID();
        final Bean2 item = buildSuppressionEvent(key);
        
        repository.save(item);
        final Bean2 suppressionEvent = repository.get(new HbaseRowKey(null, item.getRecipient(), Bean2.class)).get(0);
        
        suppressionEvent.setReason("new reason");
        
        repository.update(suppressionEvent);
        
        final Bean2 updatedEvent = repository.get(new HbaseRowKey(null, item.getRecipient(), Bean2.class)).get(0);        
        Assert.assertEquals(suppressionEvent, updatedEvent);        
    }
    
    @Test
    public void saveAndUpdateKey() {
		final UUID key = UUID.randomUUID();
        final Bean2 item = buildSuppressionEvent(key);
        
        repository.save(item);
        final Bean2 saveResult = repository.get(new HbaseRowKey(null, item.getRecipient(), Bean2.class)).get(0);
                
        final UUID key2 = UUID.randomUUID();
        saveResult.setRecipient(key2.toString());
        repository.update(saveResult);
        
        final List<Bean2> updateResults = repository.get(new HbaseRowKey(null, key2.toString(), Bean2.class));
        Assert.assertEquals(1, updateResults.size());        
    }

    @Test
    public void olyGet() {
    	final UUID key = UUID.randomUUID();
    	
        final List<Bean2> result = repository.get(new HbaseRowKey(null, key.toString(), Bean2.class));
        
        Assert.assertNotNull(result);
        Assert.assertThat(result.size(), is(0));    	
    }
    
    /*
     * no test under
     */
    
	private Bean2 buildSuppressionEvent(final UUID key) {
		return new Bean2(key.toString(), "reason-" + RandomStringUtils.randomNumeric(9));
	}    
    
}
