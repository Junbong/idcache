package io.idcache.old;


import io.idcache.increaser.Increaser;



/**
 * Created by bong on 15. 4. 25.
 */
public interface IdCacheTest<T> {
	public int test(T initValue, T minValue, T maxValue, Increaser<?> increaser);
}
