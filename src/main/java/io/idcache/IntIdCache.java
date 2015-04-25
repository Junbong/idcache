package io.idcache;


import io.idcache.increaser.Increaser;
import io.idcache.increaser.integer.SequentialIntIncreaser;

import java.util.concurrent.atomic.AtomicInteger;



/**
 * Created by bong on 15. 4. 23.
 */
public class IntIdCache extends AbstractNumberIdCache<Integer> {
	private final AtomicInteger mCounter;



	public IntIdCache(Integer initValue, Integer minValue, Integer maxValue, Increaser<? extends Number> increaser) {
		super(initValue, minValue, maxValue, increaser);

		this.mCounter = new AtomicInteger(getInitial());
	}



	public IntIdCache(Integer initValue, Integer minValue, Increaser<? extends Number> increaser) {
		this(initValue, minValue, Integer.MAX_VALUE, increaser);
	}



	public IntIdCache(Integer maxValue, Increaser<? extends Number> increaser) {
		this(0, 0, maxValue, increaser);
	}



	public IntIdCache(Increaser<? extends Number> increaser) {
		this(0, 0, Integer.MAX_VALUE, increaser);
	}



	public IntIdCache() {
		this(0, 0, Integer.MAX_VALUE, new SequentialIntIncreaser());
	}



	@Override public Integer current() {
		return mCounter.get();
	}



	@Override protected Integer onNextImpl() throws LimitationReachedException {
		final Integer nextFactor = (Integer) getIncreaser().nextFactor();
		final Integer newValue = mCounter.addAndGet(nextFactor);

		if (newValue>getMaximum() || newValue<getMinimum())
			throw new LimitationReachedException(String.format("maximum: %d, current:%d, factor:%d",
					getMaximum(), (current()-nextFactor), nextFactor));

		return newValue;
	}



	@Override protected void onSetImpl(Integer newValue) {
		mCounter.set(newValue);
	}
}
