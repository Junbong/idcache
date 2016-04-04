package io.idcache;


import io.idcache.increaser.Increaser;
import io.idcache.increaser.integer.SequentialIntIncreaser;



/**
 * Created by bong on 15. 4. 23.
 *
 * @since 0.0.1
 * @version 1.1.0
 * @author Junbong
 */
public abstract class AbstractNumberIdCache<T extends Number> extends AbstractGenericIdCache<T> {
	/**
	 * Constructs new cache with specified parameters.
	 *
	 * @param initValue initial value of this cache
	 * @param minValue minimum value of this cache
	 * @param maxValue maximum value of this cache
	 * @param increaser increaser of this cache
	 * @param limitationPolicy limitation policy of this cache
	 * @since AbstractNumberIdCache 1.1.0
	 */
	public AbstractNumberIdCache(T initValue, T minValue, T maxValue, Increaser<?> increaser, LimitationPolicy limitationPolicy) {
		super(initValue, minValue, maxValue, (increaser != null) ? increaser : new SequentialIntIncreaser(), limitationPolicy);
	}



	/**
	 * Constructs new cache with specified parameters.
	 *
	 * @param initValue initial value of this cache
	 * @param minValue minimum value of this cache
	 * @param maxValue maximum value of this cache
	 * @param increaser increaser of this cache
	 */
	public AbstractNumberIdCache(T initValue, T minValue, T maxValue, Increaser<?> increaser) {
		this(initValue, minValue, maxValue, increaser, null);
	}
}
