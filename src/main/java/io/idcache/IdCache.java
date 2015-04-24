package io.idcache;


/**
 * Created by bong on 15. 4. 23.
 */
public interface IdCache<T> {
	public T current();
	public T next();
	public T set(T newValue);
}
