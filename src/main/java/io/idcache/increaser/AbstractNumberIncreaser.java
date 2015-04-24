package io.idcache.increaser;


/**
 * Created by bong on 15. 4. 23.
 */
public abstract class AbstractNumberIncreaser<T> implements Increaser<T> {
	private final T mMinIncreaseAmount;
	private final T mMaxIncreaseAmount;



	public AbstractNumberIncreaser(T minIncreaseAmount, T maxIncreaseAmount) {
		this.mMinIncreaseAmount = minIncreaseAmount;
		this.mMaxIncreaseAmount = maxIncreaseAmount;
	}



	@Override public abstract T nextFactor();



	public T getMaxIncreaseAmount() {
		return this.mMaxIncreaseAmount;
	}



	public T getMinIncreaseAmount() {
		return this.mMinIncreaseAmount;
	}
}
