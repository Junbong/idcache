package io.idcache.increaser;


import java.util.function.Function;



/**
 * Created by bong on 15. 4. 23.
 */
public abstract class AbstractFunctionalNumberIncreaser<T> implements Increaser<T> {
	private final Function<T, T> mFunction;
	private final T mInitFactor;



	/* Hidden */
	private AbstractFunctionalNumberIncreaser(T initFactor, Function<T, T> function) {
		this.mInitFactor = initFactor;
		this.mFunction = function;
	}



	public T getInitialFactor() {
		return this.mInitFactor;
	}



	@Override public abstract T nextFactor();



	protected void onApply(T t) {
		if (this.mFunction != null) this.mFunction.apply(t);
	}
}
