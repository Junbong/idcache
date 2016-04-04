package io.idcache;


import io.idcache.increaser.Increaser;

import java.util.concurrent.atomic.AtomicBoolean;



/**
 * Basically implemented, general-purpose idCache class.
 * It is better then {@code implements} {@link IdCache} {@code interface},
 * {@code extends} this class instead because it almost implemented already.
 *
 * @since 0.0.1
 * @version 2.0.0
 * @author Junbong
 */
public abstract class AbstractGenericIdCache<T> implements IdCache<T> {
	private final Increaser<?> mIncreaser;
	private final AtomicBoolean mHasRolled;
	private final T mInitialValue;
	private final T mMinimumValue;
	private final T mMaximumValue;
	private final LimitationPolicy mLimitPolicy;



	/**
	 * Constructs new cache with specified values, increaser and default limitation policy.
	 *
	 * @param initValue        Initial value of this cache. If {@code null} value provided,
	 *                         {@link #getInitial()} returns value of {@link #getMinimum()}.
	 * @param minValue         Not {@code null}. Minimum value of this cache.
	 *                         If limitation policy set as {@link LimitationPolicy#ROLL_TO_MINIMUM_POLICY},
	 *                         value of cache counter would be set to this value.
	 * @param maxValue         Not {@code null}. Maximum value of this cache.
	 *                         If {@link #next()} >= maxValue then an action performed by following
	 *                         limitation policy.
	 * @param increaser        increaser object. Increaser generates a factor in
	 *                         every moment when {@link #next()} method called, so idCache compounds
	 *                         this new factor with value which retrieved from {@link #current()} method.
	 */
	public AbstractGenericIdCache(T initValue, T minValue, T maxValue, Increaser<?> increaser) {
		this(initValue, minValue, maxValue, increaser, null);
	}



	/**
	 * Constructs new cache with specified values, increaser and limitation policy.
	 *
	 * @param initValue        Initial value of this cache. If {@code null} value provided,
	 *                         {@link #getInitial()} returns value of {@link #getMinimum()}.
	 * @param minValue         Not {@code null}. Minimum value of this cache.
	 *                         If limitation policy set as {@link LimitationPolicy#ROLL_TO_MINIMUM_POLICY},
	 *                         value of cache counter would be set to this value.
	 * @param maxValue         Not {@code null}. Maximum value of this cache.
	 *                         If {@link #next()} >= maxValue then an action performed by following
	 *                         limitation policy.
	 * @param increaser        increaser object. Increaser generates a factor in
	 *                         every moment when {@link #next()} method called, so idCache compounds
	 *                         this new factor with value which retrieved from {@link #current()} method.
	 * @param limitationPolicy limitation policy of this cache. The limitation event triggered according to this policy
	 *                         when maximum value reached
	 */
	public AbstractGenericIdCache(T initValue, T minValue, T maxValue, Increaser<?> increaser, LimitationPolicy limitationPolicy) {
		if (minValue == null)  throw new IllegalArgumentException("minimum value cannot be null");
		if (maxValue == null)  throw new IllegalArgumentException("maximum value cannot be null");
		if (increaser == null) throw new IllegalArgumentException("increaser cannot be null");

		this.mInitialValue = initValue;
		this.mMinimumValue = minValue;
		this.mMaximumValue = maxValue;
		this.mIncreaser = increaser;
		this.mHasRolled = new AtomicBoolean(!getInitial().equals(getMinimum())
				&& !getInitial().equals(getMaximum()));

		this.mLimitPolicy = (limitationPolicy!=null)?
							limitationPolicy : LimitationPolicy.THROW_EXCEPTION_POLICY;
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



	@Override
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



	@Override
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

		// Exception will be occurred when new value is over then maximum.
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
	 * Replace the current value of this cache with specified value.
	 *
	 * @param newValue value to replace
	 * @return specified new value be passed
	 * @throws NullPointerException {@code null} passed
	 */
	@Override public final T set(T newValue) {
		if (newValue == null) throw new NullPointerException();

		onSetImpl(newValue);
		setRolled(true);

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



	/**
	 * Set new limitation policy of this cache.
	 *
	 * @param policy new limitation policy
	 * @deprecated nothing happened with this method
	 */
	@Deprecated
	@SuppressWarnings("all")
	public void setLimitationPolicy(LimitationPolicy policy) {
		/*this.mLimitPolicy = policy;*/
	}



	public LimitationPolicy getLimitationPolicy() {
		return mLimitPolicy;
	}
}
