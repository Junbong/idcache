package io.idcache.increaser;


import java.util.Random;



/**
 * Created by bong on 15. 4. 23.
 */
public abstract class AbstractRandomLeapNumberIncreaser<T> extends AbstractNumberIncreaser<T> {
	private final Random mRandom = new Random();



	public AbstractRandomLeapNumberIncreaser(T minIncreaseAmount, T maxIncreaseAmount) {
		super(minIncreaseAmount, maxIncreaseAmount);
	}



	@Override public abstract T nextFactor();



	protected int getNextInt(int bound) {
		return this.mRandom.nextInt(bound);
	}
}
