package io.idcache;


import io.idcache.increaser.Increaser;
import io.idcache.increaser.integer.SequentialIntIncreaser;

import java.util.concurrent.atomic.AtomicInteger;



/**
 * Created by bong on 15. 4. 23.
 */
public class IntIdCache extends AbstractNumberIdCache<Integer> {
	private final AtomicInteger mCounter;



	public IntIdCache(Integer initValue, Integer minValue, Integer maxValue, Increaser<Integer> increaser) {
		super(initValue, minValue, maxValue, increaser);

		this.mCounter = new AtomicInteger(getInitial());
	}



	public IntIdCache(Integer initValue, Integer minValue, Increaser<Integer> increaser) {
		this(initValue, minValue, Integer.MAX_VALUE, increaser);
	}



	public IntIdCache(Integer initValue, Increaser<Integer> increaser) {
		this(initValue, 0, Integer.MAX_VALUE, increaser);
	}



	public IntIdCache(Increaser<Integer> increaser) {
		this(0, 0, Integer.MAX_VALUE, increaser);
	}



	public IntIdCache() {
		this(0, 0, Integer.MAX_VALUE, new SequentialIntIncreaser());
	}



	@Override public Integer set(Integer newValue) {
		mCounter.set(newValue);
		return newValue;
	}



	@Override public Integer current() {
		return mCounter.get();
	}



	@Override protected Integer onNextImpl() throws LimitationReachedException {
		final Integer nextFactor = getIncreaser().nextFactor().intValue();
		final Integer newValue = mCounter.addAndGet(nextFactor);

		if (newValue>getMaximum() || newValue<getMinimum())
			throw new LimitationReachedException(String.format("maximum: %d, current:%d, factor:%d",
					getMaximum(), (current()-nextFactor), nextFactor));

		return newValue;
	}
}
