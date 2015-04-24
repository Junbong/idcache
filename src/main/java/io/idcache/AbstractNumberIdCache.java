package io.idcache;


import io.idcache.increaser.Increaser;



/**
 * Created by bong on 15. 4. 23.
 */
public abstract class AbstractNumberIdCache<T> implements IdCache<T> {
	private final Increaser<T> mIncreaser;
	private final T mInitialValue;
	private final T mMinimumValue;
	private final T mMaximumValue;

	private LimitationPolicy mLimitPolicy = LimitationPolicy.ROLL_TO_MINIMUM_POLICY;



	public AbstractNumberIdCache(T initValue, T minValue, T maxValue, Increaser<T> increaser) {
		this.mInitialValue = initValue;
		this.mMinimumValue = minValue;
		this.mMaximumValue = maxValue;
		this.mIncreaser = increaser;
	}



	public T getInitial() {
		return this.mInitialValue;
	}



	public T getMinimum() {
		return this.mMinimumValue;
	}



	public T getMaximum() {
		return this.mMaximumValue;
	}



	@Override public abstract T current();



	@Override public final T next() {
		T value;

		try {
			value = onNextImpl();

		} catch (LimitationReachedException e) {
			switch (getLimitationPolicy()) {
				case INITIALIZE_POLICY:
					return set(getInitial());

				case ROLL_TO_MINIMUM_POLICY:
					return set(getMinimum());

				case ROLL_TO_MAXIMUM_POLICY:
					return set(getMaximum());

				case THROW_EXCEPTION_POLICY:
				default:
					throw new LimitationReachedException(e);
			}
		}

		return value;
	}



	protected abstract T onNextImpl() throws LimitationReachedException;



	public Increaser<T> getIncreaser() {
		return this.mIncreaser;
	}



	public void setLimitationPolicy(LimitationPolicy policy) {
		this.mLimitPolicy = policy;
	}



	public LimitationPolicy getLimitationPolicy() {
		return mLimitPolicy;
	}
}
