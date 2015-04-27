# idCache
The *idCache* is an policy based identifier generator with cache library written in Java. It is good for item store to giving a unique identifier value to each items. For example, you would like to set SID-stuff identifier- to each items for classify it. In this case, idCache is a good choice. It provides you highly abstracted value generator which take incremental policy.

## License
Released under the permissive [MIT License][].

## Main Goals
* Policy based
* Non-duplicated, unique values
* Concurrency
* Stability

## Usage
### Create simple incremental value generator
```java
// This idCache provides value with incrementing previous by 1
IntIdCache cache = (IntIdCache) IdCaches.newIntIdentifierCacheBuilder()
        // Set initial value - it begins with this value
		.setInitialValue(10000000)
		// Set maximum value - an action performed when value reached
		.setMaximumValue(19999999)
		// Set increasing policy
		.setIncreaser(new SequentialIntIncreaser(1))
		// Determine an action when maximum value reached
		.setLimitationPolicy(LimitationPolicy.THROW_EXCEPTION_POLICY)
		.build();

// 10000000, 10000001, 10000002, 10000003…, 19999999
// an exception occurred when value reached 20000000

// Or simply,
// (in this case, id range is 0 to 10000000)
IntIdCache simpleCache = new IntIdCache(10000000, new SequentialIntIncreaser());

// Or,
// (id range 0 to Integer.MAX_VALUE)
IntIdCache simplestCache = new IntIdCache();
```

### Create random-range incremental value generator
```java
// This idCache provides value with incrementing previous by RANDOM adder
IntIdCache randomCache = new IntIdCache(10000000, 20000000, new RandomLeapIntIncreaser(1, 9));

// 10000000, 10000005, 10000009, 10000012… 19999998
```

[MIT License]: https://github.com/Junbong/idcache/blob/master/LICENSE
