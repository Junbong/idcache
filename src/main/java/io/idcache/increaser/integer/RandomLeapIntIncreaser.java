package io.idcache.increaser.integer;


import io.idcache.increaser.AbstractRandomLeapNumberIncreaser;



/**
 * Created by bong on 15. 4. 23.
 */
public class RandomLeapIntIncreaser extends AbstractRandomLeapNumberIncreaser<Integer> {
	public RandomLeapIntIncreaser(Integer minIncreaseAmount, Integer maxIncreaseAmount) {
		super(Math.max(1, minIncreaseAmount), maxIncreaseAmount);
	}



	public RandomLeapIntIncreaser() {
		this(1, 9);
	}



	@Override public Integer nextFactor() {
		return getNextInt(getMaxIncreaseAmount()) + getMinIncreaseAmount();
	}
}
