package io.idcache;


import io.idcache.increaser.Increaser;
import io.idcache.increaser.integer.SequentialIntIncreaser;



/**
 * Created by bong on 15. 4. 23.
 */
public class IdCaches {
	/**
	 * Convenient builder class for build new cache.
	 *
	 * @param <T> type for new cache
	 * @author Junbong
	 * @version 2.0.0
	 * @since 0.0.1
	 */
	public interface Builder<T, R extends IdCache> {
		/**
		 * Set initial value of this cache.
		 * Default value is zero.
		 *
		 * @param value initial value of this cache
		 * @return this Builder instance for chain call
		 */
		Builder<T, R> setInitialValue(T value);

		/**
		 * Set minimal value of this cache.
		 * Default value is zero.
		 *
		 * @param value minimal value of this cache
		 * @return this Builder instance for chain call
		 */
		Builder<T, R> setMinimumValue(T value);

		/**
		 * Set maximum value of this cache.
		 * Default value is depends on it`s type and implementation.
		 *
		 * @param value maximum value of this cache
		 * @return this Builder instance for chain call
		 */
		Builder<T, R> setMaximumValue(T value);

		/**
		 * Set {@link Increaser} of this cache.
		 * @param increaser new instance of Increaser class of this cache
		 * @return this Builder instance for chain call
		 */
		Builder<T, R> setIncreaser(Increaser<T> increaser);

		/**
		 * Set {@link LimitationPolicy} of this cache.
		 * This policy determines the way of dealing value
		 * when
		 * @param policy limitation policy
		 * @return this Builder instance for chain call
		 */
		Builder<T, R> setLimitationPolicy(LimitationPolicy policy);

		/**
		 * Finally build new cache with specified values.
		 * @return new cache built from specified values
		 */
		R build();
	}



	public static Builder<Integer, IntIdCache> newIntIdentifierCacheBuilder() {
		return new Builder<Integer, IntIdCache>() {
			private Integer mInitValue = 0;
			private Integer mMinValue = 0;
			private Integer mMaxValue = Integer.MAX_VALUE;
			private Increaser<Integer> mIncreaser;
			private LimitationPolicy mLimitPolicy;



			@Override public Builder<Integer, IntIdCache> setInitialValue(Integer value) {
				this.mInitValue = value;
				return this;
			}



			@Override public Builder<Integer, IntIdCache> setMinimumValue(Integer value) {
				this.mMinValue = value;
				return this;
			}



			@Override public Builder<Integer, IntIdCache> setMaximumValue(Integer value) {
				this.mMaxValue = value;
				return this;
			}



			@Override public Builder<Integer, IntIdCache> setIncreaser(Increaser<Integer> increaser) {
				this.mIncreaser = increaser;
				return this;
			}



			@Override public Builder<Integer, IntIdCache> setLimitationPolicy(LimitationPolicy policy) {
				this.mLimitPolicy = policy;
				return this;
			}



			@Override public IntIdCache build() {
				// Set default increaser when increaser not provided
				if (mIncreaser == null)
					mIncreaser = new SequentialIntIncreaser(1);

				// Create new cache for integer type
				IntIdCache cache = new IntIdCache(mInitValue, mMinValue, mMaxValue, mIncreaser, mLimitPolicy);

				// Set limitation policy
				// --> setLimitationPolicy() method was deprecated
				/*
				if (mLimitPolicy != null)
					cache.setLimitationPolicy(mLimitPolicy);
				*/

				return cache;
			}
		};
	}

}
