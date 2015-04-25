package io.idcache;


import io.idcache.increaser.Increaser;
import io.idcache.increaser.integer.SequentialIntIncreaser;



/**
 * Created by bong on 15. 4. 23.
 */
public abstract class AbstractNumberIdCache<T extends Number> extends AbstractGenericIdCache<T> {
	public AbstractNumberIdCache(T initValue, T minValue, T maxValue, Increaser<?> increaser) {
		super(initValue, minValue, maxValue, (increaser != null) ? increaser : new SequentialIntIncreaser());
	}
}
