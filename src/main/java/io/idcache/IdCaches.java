package io.idcache;


import io.idcache.increaser.Increaser;
import io.idcache.increaser.integer.SequentialIntIncreaser;



/**
 * Created by bong on 15. 4. 23.
 */
public class IdCaches {
	public interface Builder<T> {
		/**
		 * Set initial value of this cache.
		 * Default value is zero.
		 *
		 * @param value initial value of this cache
		 * @return this Builder instance for chain call
		 */
		public Builder<T> setInitialValue(T value);

		/**
		 * Set minimal value of this cache.
		 * Default value is zero.
		 *
		 * @param value minimal value of this cache
		 * @return this Builder instance for chain call
		 */
		public Builder<T> setMinimumValue(T value);

		/**
		 * Set maximum value of this cache.
		 * Default value is depends on it`s type and implementation.
		 *
		 * @param value maximum value of this cache
		 * @return this Builder instance for chain call
		 */
		public Builder<T> setMaximumValue(T value);

		/**
		 * Set {@link Increaser} of this cache.
		 * @param increaser new instance of Increaser class of this cache
		 * @return this Builder instance for chain call
		 */
		public Builder<T> setIncreaser(Increaser<T> increaser);

		/**
		 * Set {@link LimitationPolicy} of this cache.
		 * This policy determines the way of dealing value
		 * when
		 * @param policy
		 * @return
		 */
		public Builder<T> setLimitationPolicy(LimitationPolicy policy);

		public IdCache build();
	}



	public static Builder newIntIdentifierCacheBuilder() {
		return new Builder<Integer>() {
			private Integer mInitValue = 0;
			private Integer mMinValue = 0;
			private Integer mMaxValue = Integer.MAX_VALUE;
			private Increaser<Integer> mIncreaser;
			private LimitationPolicy mLimitPolicy;



			@Override public Builder<Integer> setInitialValue(Integer value) {
				this.mInitValue = value;
				return this;
			}



			@Override public Builder<Integer> setMinimumValue(Integer value) {
				this.mMinValue = value;
				return this;
			}



			@Override public Builder<Integer> setMaximumValue(Integer value) {
				this.mMaxValue = value;
				return this;
			}



			@Override public Builder<Integer> setIncreaser(Increaser<Integer> increaser) {
				this.mIncreaser = increaser;
				return this;
			}



			@Override public Builder<Integer> setLimitationPolicy(LimitationPolicy policy) {
				this.mLimitPolicy = policy;
				return this;
			}



			@Override public IdCache build() {
				// Set default increaser when increaser not provided
				if (mIncreaser == null)
					mIncreaser = new SequentialIntIncreaser(1);

				// Create new cache for integer type
				IntIdCache cache = new IntIdCache(mInitValue, mMinValue, mMaxValue, mIncreaser);

				// Set limitation policy
				if (mLimitPolicy != null)
					cache.setLimitationPolicy(mLimitPolicy);
				return cache;
			}
		};
	}

}
