package io.idcache;


import io.idcache.increaser.Increaser;



/**
 * Created by bong on 15. 4. 23.
 */
public interface IdCache<T> {
	public T getInitial();

	public T getMinimum();

	public T getMaximum();

	public T current();

	public T next();

	public T set(T newValue);

	public Increaser<?> getIncreaser();
}
