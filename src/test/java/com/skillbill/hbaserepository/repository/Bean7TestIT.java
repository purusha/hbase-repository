package com.skillbill.hbaserepository.repository;

import static org.hamcrest.CoreMatchers.is;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.junit.Assert;
import org.junit.Test;

import com.google.inject.Inject;
import com.skillbill.hbaserepository.Bean7;

public class Bean7TestIT extends BasicTest {

    private static final Date KEY = new Date();
    
    @Inject
    private HRepository<Bean7> repository;

    @Test(expected = RuntimeException.class)
    public void save() {

        repository.save(
    		buildSuppressionEvent()
		);
    }

    @Test(expected = RuntimeException.class)
    public void saveAndGet() {
        final UUID key = UUID.randomUUID();

        final Bean7 item = buildSuppressionEvent();
		repository.save(item);

        final List<Bean7> result = repository.get(new HRepository.HbaseRowKey(null, key.toString(), Bean7.class));

        Assert.assertNotNull(result);
        Assert.assertThat(result.size(), is(0));
    }

    @Test(expected = RuntimeException.class)
    public void saveAndRemove() {

        final Bean7 item = buildSuppressionEvent();
		repository.save(item);

		repository.remove(item);

		final List<Bean7> result = repository.get(new HRepository.HbaseRowKey(null, item.getRecipient(), Bean7.class));

        Assert.assertNotNull(result);
        Assert.assertThat(result.size(), is(0));
    }

    @Test(expected = RuntimeException.class)
    public void onlyRemove() {

        final Bean7 item = buildSuppressionEvent();

		final Bean7 result = repository.remove(item);
        Assert.assertNull(result);
    }

    /*
     * no test under
     */
    @Test(expected = RuntimeException.class)
    public void olyGet() {

        final List<Bean7> result = repository.get(new HRepository.HbaseRowKey(null, KEY, Bean7.class));

        Assert.assertNotNull(result);
        Assert.assertThat(result.size(), is(0));
    }


    private Bean7 buildSuppressionEvent() {
        return new Bean7(KEY, "reason");
	}    
    
}
