package io.idcache;


import io.idcache.increaser.Increaser;
import io.idcache.increaser.integer.SequentialIntIncreaser;

import java.util.concurrent.atomic.AtomicInteger;



/**
 * Created by bong on 15. 4. 23.
 *
 * @since 0.0.1
 * @version 1.1.0
 * @author Junbong
 */
public class IntIdCache extends AbstractNumberIdCache<Integer> {
	private final AtomicInteger mCounter;



	/**
	 * Constructs new integer identifier cache with specified parameters.
	 *
	 * @param initValue        initial value of this cache. When given value is less than minimum value, minimum value will be used
	 * @param minValue         minimum value of this cache
	 * @param maxValue         maximum value of this cache
	 * @param increaser        increaser of this cache
	 * @param limitationPolicy limitation policy of this cache
	 * @since IntIdCache 1.1.0
	 */
	public IntIdCache(Integer initValue, Integer minValue, Integer maxValue, Increaser<? extends Number> increaser, LimitationPolicy limitationPolicy) {
		super(Math.max(initValue, minValue), minValue,
				(maxValue!=null)? maxValue : Integer.MAX_VALUE,
				increaser, limitationPolicy);
		this.mCounter = new AtomicInteger(getInitial());
	}



	public IntIdCache(Integer initValue, Integer minValue, Integer maxValue, Increaser<? extends Number> increaser) {
		this(initValue, minValue, maxValue, increaser, null);
	}



	public IntIdCache(Integer initValue, Integer minValue, Increaser<? extends Number> increaser) {
		this(initValue, minValue, null, increaser, null);
	}



	public IntIdCache(Integer maxValue, Increaser<? extends Number> increaser) {
		this(0, 0, maxValue, increaser, null);
	}



	public IntIdCache(Increaser<? extends Number> increaser) {
		this(0, 0, null, increaser, null);
	}



	public IntIdCache() {
		this(0, 0, null, new SequentialIntIncreaser(), null);
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
