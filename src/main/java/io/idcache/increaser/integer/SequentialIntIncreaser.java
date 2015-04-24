package io.idcache.increaser.integer;


import io.idcache.increaser.AbstractNumberIncreaser;



/**
 * Created by bong on 15. 4. 23.
 */
public class SequentialIntIncreaser extends AbstractNumberIncreaser<Integer> {
	public SequentialIntIncreaser(int amount) {
		super(amount, amount);
	}



	public SequentialIntIncreaser() {
		super(1, 1);
	}



	@Override public Integer nextFactor() {
		return getMaxIncreaseAmount();
	}
}
