package io.idcache;


import io.idcache.increaser.integer.RandomLeapIntIncreaser;
import io.idcache.increaser.integer.SequentialIntIncreaser;
import org.junit.Test;
import static org.junit.Assert.*;



/**
 * Created by Bong on 2016-04-04.
 *
 * @author Bong
 * @version 1.0.0
 */
public class IntIdCacheTest {
	@Test public void initValueTest() {
		IntIdCache intIdCache = new IntIdCache();

		Integer initialValue = intIdCache.current();
		assertEquals(Integer.valueOf(0), initialValue);
	}



	@Test public void sequentialLeapTest() {
		IntIdCache intIdCache = new IntIdCache();

		Integer _nextValue = null;
		for (int i=0; i<=100; i++) _nextValue = intIdCache.next();
		assertNotNull(_nextValue);
		assertEquals(Integer.valueOf(100), _nextValue);
	}



	@Test public void customLeapTest() {
		IntIdCache intIdCache = new IntIdCache(new SequentialIntIncreaser(2));

		Integer _nextValue = null;
		for (int i=0; i<=100; i++) _nextValue = intIdCache.next();
		assertNotNull(_nextValue);
		assertEquals(Integer.valueOf(200), _nextValue);
	}

	@Test public void customLeapTest2() {
		IntIdCache intIdCache = new IntIdCache(200, 0, new SequentialIntIncreaser(-1));

		Integer _nextValue = null;
		for (int i=0; i<100; i++) _nextValue = intIdCache.next();
		assertNotNull(_nextValue);
		assertEquals(Integer.valueOf(100), _nextValue);
	}



	@Test public void minimumValueTest() {
		IntIdCache intIdCache = new IntIdCache(100, 0, new SequentialIntIncreaser(-1));

		Integer _nextValue = null;
		for (int i=0; i<200; i++) _nextValue = intIdCache.next();
		assertNotNull(_nextValue);
		assertEquals(Integer.valueOf(0), _nextValue);
	}



	@Test(expected = LimitationReachedException.class)
	public void limitationPolicyThrowTest() {
		IntIdCache intIdCache = new IntIdCache(0, 0, 10,
				new SequentialIntIncreaser(), LimitationPolicy.THROW_EXCEPTION_POLICY);

		for (int i=0; i<200; i++) intIdCache.next();
	}



	@Test public void randomLeapTest() {
		IntIdCache intIdCache = new IntIdCache(0, 0, new RandomLeapIntIncreaser());

		Integer _nextValue;
		_nextValue = intIdCache.next();
		_nextValue = intIdCache.next();
		assertNotNull(_nextValue);
		assertTrue(_nextValue > 0);
	}



	@Test public void builderTest() {
		IntIdCache intIdCache = IdCaches.newIntIdentifierCacheBuilder()
										.setInitialValue(0)
										.setMinimumValue(100)
										.setMaximumValue(150)
										.setIncreaser(new SequentialIntIncreaser(5))
										.setLimitationPolicy(LimitationPolicy.ROLL_TO_MINIMUM_POLICY)
										.build();

		assertEquals(SequentialIntIncreaser.class, intIdCache.getIncreaser().getClass());
		assertEquals(Integer.valueOf(100), intIdCache.current());
		assertEquals(Integer.valueOf(100), intIdCache.next());
		assertEquals(Integer.valueOf(105), intIdCache.next());

		Integer _nextValue = null;
		for (int i=0; i<=30; i++) _nextValue = intIdCache.next();
		assertEquals(Integer.valueOf(150), _nextValue);

		_nextValue = intIdCache.next();
		assertEquals(Integer.valueOf(100), _nextValue);
	}
}
