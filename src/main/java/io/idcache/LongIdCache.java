package io.idcache;


import io.idcache.increaser.Increaser;
import io.idcache.increaser.integer.SequentialIntIncreaser;

import java.util.concurrent.atomic.AtomicLong;



/**
 * Created by bong on 15. 4. 25.
 */
public class LongIdCache extends AbstractNumberIdCache<Long> {
	private final AtomicLong mCounter;
	
	
	
	public LongIdCache(Long initValue, Long minValue, Long maxValue, Increaser<? extends Number> increaser) {
		super(initValue, minValue, maxValue, increaser);
		
		this.mCounter = new AtomicLong(getInitial());
	}
	
	
	
	public LongIdCache(Long initValue, Long minValue, Increaser<? extends Number> increaser) {
		this(initValue, minValue, Long.MAX_VALUE, increaser);
	}
	
	
	
	public LongIdCache(Long maxValue, Increaser<? extends Number> increaser) {
		this(0L, 0L, maxValue, increaser);
	}
	
	
	
	public LongIdCache(Increaser<? extends Number> increaser) {
		this(0L, 0L, Long.MAX_VALUE, increaser);
	}
	
	
	
	public LongIdCache() {
		this(0L, 0L, Long.MAX_VALUE, new SequentialIntIncreaser());
	}
	
	
	
	@Override public Long current() {
		return mCounter.get();
	}
	
	
	
	@Override protected Long onNextImpl() throws LimitationReachedException {
		final Integer nextFactor = (Integer) getIncreaser().nextFactor();
		final Long newValue = mCounter.addAndGet(nextFactor);

		if (newValue>getMaximum() || newValue<getMinimum())
			throw new LimitationReachedException(String.format("maximum: %d, current:%d, factor:%d",
					getMaximum(), (current()-nextFactor), nextFactor));

		return newValue;
	}



	@Override protected void onSetImpl(Long newValue) {
		mCounter.set(newValue);
	}
}
