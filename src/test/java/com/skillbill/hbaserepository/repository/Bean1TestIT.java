package com.skillbill.hbaserepository.repository;

import static org.hamcrest.CoreMatchers.is;

import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Assert;
import org.junit.Test;

import com.google.inject.Inject;
import com.skillbill.hbaserepository.Bean1;
import com.skillbill.hbaserepository.repository.HRepository.HbaseRowKey;

public class Bean1TestIT extends BasicTest {	

	private static final Long CUSTOMER_ID = 3000147L;
    
	@Inject
    private HRepository<Bean1> repository;

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
        
        final Bean1 item = buildSuppressionEvent(key);
		repository.save(item);
        
        final List<Bean1> result = repository.get(new HbaseRowKey(CUSTOMER_ID, key.toString(), Bean1.class));
        
        Assert.assertNotNull(result);
        Assert.assertThat(result.size(), is(1));
        Assert.assertEquals(item, result.get(0));
    }
    
    @Test
    public void saveAndRemove() {
    	final UUID key = UUID.randomUUID();
        
        final Bean1 item = buildSuppressionEvent(key);
		repository.save(item);
		
		repository.remove(item);
		
		final List<Bean1> result = repository.get(new HbaseRowKey(item.getNamespaceId(), item.getRecipient(), Bean1.class));
        
        Assert.assertNotNull(result);
        Assert.assertThat(result.size(), is(0));		
    }
  
    @Test
    public void onlyRemove() {
    	final UUID key = UUID.randomUUID();
        
        final Bean1 item = buildSuppressionEvent(key);
		
		final Bean1 result = repository.remove(item);
        Assert.assertNull(result);		
    }
    
    @Test
    public void saveAndUpdate() {
		final UUID key = UUID.randomUUID();
        final Bean1 item = buildSuppressionEvent(key);
        
        repository.save(item);
        final Bean1 suppressionEvent = repository.get(new HbaseRowKey(item.getNamespaceId(), item.getRecipient(), Bean1.class)).get(0);
        
        suppressionEvent.setReason("new reason");
        
        repository.update(suppressionEvent);
        
        final Bean1 updatedEvent = repository.get(new HbaseRowKey(item.getNamespaceId(), item.getRecipient(), Bean1.class)).get(0);        
        Assert.assertEquals(suppressionEvent, updatedEvent);        
    }
    
    @Test
    public void saveAndUpdateKey() {
		final UUID key = UUID.randomUUID();
        final Bean1 item = buildSuppressionEvent(key);
        
        repository.save(item);
        final Bean1 saveResult = repository.get(new HbaseRowKey(item.getNamespaceId(), item.getRecipient(), Bean1.class)).get(0);
                
        final UUID key2 = UUID.randomUUID();
        saveResult.setRecipient(key2.toString());
        repository.update(saveResult);
        
        final List<Bean1> updateResults = repository.get(new HbaseRowKey(item.getNamespaceId(), key2.toString(), Bean1.class));
        Assert.assertEquals(1, updateResults.size());        
    }

    @Test
    public void olyGet() {
    	final UUID key = UUID.randomUUID();
    	
        final List<Bean1> result = repository.get(new HbaseRowKey(CUSTOMER_ID, key.toString(), Bean1.class));
        
        Assert.assertNotNull(result);
        Assert.assertThat(result.size(), is(0));    	
    }
    
    /*
     * no test under
     */
    
	private Bean1 buildSuppressionEvent(final UUID key) {
		return new Bean1(CUSTOMER_ID, key.toString(), "reason-" + RandomStringUtils.randomNumeric(9));
	}    
    
}
