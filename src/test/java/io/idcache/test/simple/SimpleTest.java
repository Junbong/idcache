package io.idcache.test.simple;


import io.idcache.IntIdCache;
import io.idcache.LimitationPolicy;
import io.idcache.LimitationReachedException;
import io.idcache.LongIdCache;
import io.idcache.increaser.Increaser;
import io.idcache.increaser.integer.SequentialIntIncreaser;
import io.idcache.test.IdCacheTest;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;



/**
 * Created by bong on 15. 4. 25.
 */
public class SimpleTest {
	public static void main(String[] args) {
		doIntTest(0, 0, 1000);
		doIntTest(0, 0, 10000);
		doIntTest(0, 500, 10000);
		doIntTest(1000, 500, 100000);
		doIntTest(1000, 0, 10000);

		System.out.println("\n\n");

		doLongTest(0L, 0L, 100000L);
		doLongTest(0L, 0L, 1000000L);
		doLongTest(0L, 5000L, 100000L);
		doLongTest(10000L, 5000L, 100000L);
		doLongTest(10000L, 0L, 100000L);
	}



	public static void doIntTest(Integer initValue, Integer minValue, Integer maxValue) {
		IntSequencialTester tester = new IntSequencialTester();
		System.out.println("------------------------------------------------");
		System.out.println(
				String.format("IntSequencialTester Test\n -init:%d\n -min:%d\n -max:%d",
						initValue, minValue, maxValue));
		System.out.println(
				String.format("Test Failed: %d",
						tester.test(initValue, minValue, maxValue, new SequentialIntIncreaser())));
	}



	private static class IntSequencialTester implements IdCacheTest<Integer> {
		public int test(Integer initValue, Integer minValue, Integer maxValue, Increaser<?> increaser) {
			final int availableCores = Runtime.getRuntime().availableProcessors();
			final int taskCount = maxValue - Math.max(initValue, minValue);
			final AtomicInteger resultCounter = new AtomicInteger();

			final ConcurrentSkipListSet<Integer> setResult = new ConcurrentSkipListSet<Integer>();

			final IntIdCache cache = new IntIdCache(initValue, minValue, maxValue, (Increaser<Integer>) increaser);
			cache.setLimitationPolicy(LimitationPolicy.THROW_EXCEPTION_POLICY);
			final ExecutorService executor = Executors.newFixedThreadPool(availableCores*2);

			final List<Callable<Boolean>> tasks = new ArrayList<Callable<Boolean>>(taskCount);
			for (int i=0; i<taskCount; i++) {
				tasks.add(new Callable<Boolean>() {
					public Boolean call() throws Exception {
						Integer newId;

						try {
							newId = cache.next();
							//System.out.println(newId);
							boolean success =  setResult.add(newId);
							if (!success) System.out.println(String.format("! %d", newId));
							return success;
						} catch (LimitationReachedException e) {
							newId = -1;
							return true;
						}
					}
				});
			}

			try {
				List<Future<Boolean>> taskResult = executor.invokeAll(tasks);

				for (Future<Boolean> f: taskResult) {
					try {
						if (f.get() == false) resultCounter.incrementAndGet();
					} catch (ExecutionException e) {
						resultCounter.incrementAndGet();
					}
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			executor.shutdown();
			return resultCounter.get();
		}
	}



	public static void doLongTest(Long initValue, Long minValue, Long maxValue) {
		LongSequencialTester tester = new LongSequencialTester();
		System.out.println("------------------------------------------------");
		System.out.println(
				String.format("LongSequencialTester Test\n -init:%d\n -min:%d\n -max:%d",
						initValue, minValue, maxValue));
		System.out.println(
				String.format("Test Failed: %d",
						tester.test(initValue, minValue, maxValue, new SequentialIntIncreaser())));
	}



	private static class LongSequencialTester implements IdCacheTest<Long> {
		public int test(Long initValue, Long minValue, Long maxValue, Increaser<?> increaser) {
			final int availableCores = Runtime.getRuntime().availableProcessors();
			final long taskCount = maxValue - Math.max(initValue, minValue);
			final AtomicInteger resultCounter = new AtomicInteger();

			final ConcurrentSkipListSet<Long> setResult = new ConcurrentSkipListSet<Long>();

			final LongIdCache cache = new LongIdCache(initValue, minValue, maxValue, (Increaser<Integer>) increaser);
			cache.setLimitationPolicy(LimitationPolicy.THROW_EXCEPTION_POLICY);
			final ExecutorService executor = Executors.newFixedThreadPool(availableCores*2);

			final List<Callable<Boolean>> tasks = new ArrayList<Callable<Boolean>>();
			for (int i=0; i<taskCount; i++) {
				tasks.add(new Callable<Boolean>() {
					public Boolean call() throws Exception {
						Long newId;

						try {
							newId = cache.next();
							//System.out.println(newId);
							boolean success =  setResult.add(newId);
							if (!success) System.out.println(String.format("! %d", newId));
							return success;
						} catch (LimitationReachedException e) {
							newId = -1L;
							return true;
						}
					}
				});
			}

			try {
				List<Future<Boolean>> taskResult = executor.invokeAll(tasks);

				for (Future<Boolean> f: taskResult) {
					try {
						if (f.get() == false) resultCounter.incrementAndGet();
					} catch (ExecutionException e) {
						resultCounter.incrementAndGet();
					}
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			executor.shutdown();
			return resultCounter.get();
		}
	}
}
