package io.idcache;


import io.idcache.increaser.Increaser;

import java.util.concurrent.atomic.AtomicBoolean;



/**
 * Basically implemented, general-purpose idCache class.
 * It is better then {@code implements} {@link IdCache} {@code interface},
 * {@code extends} this class instead because it almost implemented already.
 */
public abstract class AbstractGenericIdCache<T> implements IdCache<T> {
	private final Increaser<?> mIncreaser;
	private final AtomicBoolean mHasRolled;
	private final T mInitialValue;
	private final T mMinimumValue;
	private final T mMaximumValue;

	private LimitationPolicy mLimitPolicy = LimitationPolicy.ROLL_TO_MINIMUM_POLICY;



	/**
	 * Default constructor of this cache class.
	 * @param initValue Initial value of this cache. If {@code null} value provided,
	 * {@link #getInitial()} returns value of {@link #getMinimum()}.
	 * @param minValue Not {@code null}. Minimum value of this cache.
	 * If limitation policy set as {@link LimitationPolicy#ROLL_TO_MINIMUM_POLICY},
	 * value of cache counter would be set to this value.
	 * @param maxValue Not {@code null}. Maximum value of this cache.
	 * If {@link #next()} >= maxValue then an action performed by following
	 * limitation policy.
	 * @param increaser increaser object. Increaser generates a factor in
	 * every moment when {@link #next()} method called, so idCache compounds
	 * this new factor with value which retrieved from {@link #current()} method.
	 */
	public AbstractGenericIdCache(T initValue, T minValue, T maxValue, Increaser<?> increaser) {
		if (minValue == null)
			throw new IllegalArgumentException("Minimum value cannot be null");
		if (maxValue == null)
			throw new IllegalArgumentException("Maximum value cannot be null");

		this.mInitialValue = initValue;
		this.mMinimumValue = minValue;
		this.mMaximumValue = maxValue;
		this.mIncreaser = increaser;

		this.mHasRolled = new AtomicBoolean(!getInitial().equals(getMinimum())
				&& !getInitial().equals(getMaximum()));
	}



	@Override
	public T getInitial() {
		return (this.mInitialValue!=null)?
				this.mInitialValue : getMinimum();
	}



	@Override
	public T getMinimum() {
		return this.mMinimumValue;
	}



	@Override
	public T getMaximum() {
		return this.mMaximumValue;
	}



	protected void setRolled(boolean rolled) {
		this.mHasRolled.set(rolled);
	}



	public boolean hasRolled() {
		return this.mHasRolled.get();
	}



	/**
	 * <P>Retrieve the current(last generated) value.
	 * </P>
	 * Implementation:
	 * <UL>
	 *     <LI>when cache has initial state, which means
	 *     it does not 'rolled' yet, the initial value must be retrieved</LI>
	 *     <LI>when already 'rolled', last generated value must be retrieved</LI>
	 *     <LI>Do not make any side-effects for calling this method</LI>
	 * </UL>
	 * @return the current value
	 */
	public abstract T current();



	/**
	 * <P>Returns newly generated value.
	 * If not 'rolled' yet,
	 * </P>
	 * <P>This method is declared to <I>final</I>. So you can override
	 * {@link #onNextImpl()} method if your own implementation needed.
	 * </P>
	 * @return newly generated identifier
	 */
	public final T next() {
		T value;

		try {
			// Return null when increaser is not available
			if (getIncreaser() == null) return null;

			// If not 'rolled' yet, initial value would be returned
			else if (!hasRolled()) {
				value = getInitial();
				setRolled(true);
			}

			// Compute value on impl method
			else {
				if ((value=onNextImpl()) != null) setRolled(true);
			}

		// Exception would be occurred when new value is over then maximum.
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



	/**
	 * <P>Replace this cache`s current value with given value.
	 * If given value is {@code null},
	 * In ordinary case, do not call this method directly.
	 * </P>
	 * <P>This method is declared to <I>final</I>. So you can override
	 * {@link #onSetImpl(Object)} method if your own implementation needed.
	 * </P>
	 * @param newValue the value to replace current one
	 * @return given value passed
	 */
	@Override public final T set(T newValue) {
		if (newValue != null) {
			onSetImpl(newValue);
			setRolled(true);
		}

		return newValue;
	}



	/**
	 * Implementation:
	 * <UL>
	 *     <LI>Replace the current value with given value</LI>
	 *     <LI>This task must be performed atomically</LI>
	 * </UL>
	 * @param newValue
	 */
	protected abstract void onSetImpl(T newValue);



	@Override
	public Increaser<?> getIncreaser() {
		return this.mIncreaser;
	}



	public void setLimitationPolicy(LimitationPolicy policy) {
		this.mLimitPolicy = policy;
	}



	public LimitationPolicy getLimitationPolicy() {
		return mLimitPolicy;
	}
}
