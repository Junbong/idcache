package io.idcache;


import io.idcache.increaser.Increaser;



/**
 * <p>
 *     Common interface of cache.
 * </p>
 * <p>
 *     Created by Junbong on 15. 4. 23.
 * </p>
 *
 * @since idCache 0.0.1
 * @version 1.1.0
 * @author Junbong
 */
public interface IdCache<T> {
	/**
	 * <p>
	 *     Retrieve the starting value of this cache.
	 * </p>
	 * <p>
	 *     <i>Note that</i>, this value is like a rolling status of this cache
	 *     so initial value and minimum value may not be the same.
	 * </p>
	 *
	 * @return the initial value of this cache
	 */
	T getInitial();



	/**
	 * <p>
	 *     Retrieve the minimum boundary value of this cache.
	 * </p>
	 * <p>
	 *     The value of {@link #next()} method never less than this minimum value.
	 *     So this cache set by this value and starts again
	 *     when maximum value reached with <i>rolling policy</i>.
	 * </p>
	 *
	 * @return the minimum boundary value of this cache
	 */
	T getMinimum();



	/**
	 * <p>
	 *     Retrieve the maximum boundary value of this cache.
	 * </p>
	 * <p>
	 *     The rolling event triggered when value of {@link #next()}
	 *     is bigger than maximum value according to the specified rolling policy.
	 * </p>
	 *
	 * @return the maximum boundary value of this cache
	 */
	T getMaximum();



	/**
	 * <p>
	 *     Returns the rolling status of this cache.
	 * </p>
	 * <p>
	 *     If this method returns {@code false},
	 *     {@link #next()} method never be called.
	 * </p>
	 *
	 * @return the rolling status of this cache
	 */
	boolean hasRolled();



	/**
	 * Retrieve the current value of this cahce.
	 *
	 * @return the current value of this cahce
	 */
	T current();



	/**
	 * <P>
	 *     Returns next value of this cache.
	 *     The value will be current value + next incremental factor of <i>increaser</i>.
	 *     By the way, the value of {@link #getInitial()} would be returned
	 *     when {@link #hasRolled()} is {@code false}.
	 * </p>
	 *
	 * @return next value of this cache
	 */
	T next();



	/**
	 * Replace the current value of this cache with specified value.
	 *
	 * @param newValue value to replace
	 * @return specified new value be passed
	 */
	T set(T newValue);



	/**
	 * Retrieve the increaser of this cache.
	 *
	 * @return the increaser of this cache
	 */
	Increaser<?> getIncreaser();
}
