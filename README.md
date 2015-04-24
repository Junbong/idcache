# idCache
The *idCache* is an policy based identifier generator with cache library written in Java. It is good for item store to giving a unique identifier value to each items. For example, you would like to set SID-stuff identifier- to each items for classify it. In this case, idCache is a good choice. It provides you highly abstracted value generator which take incremental policy.

## License
Released under the permissive [MIT License][].

## Usage
### Create simple incremental value generator
```java
// This idCache provides value with incrementing previous by 1
IntIdCache cache = (IntIdCache) IdCaches.newIntIdentifierCacheBuilder()
        // Set initial value - it begins with this value
		.setInitialValue(1000000000)
		// Set maximum value - an action performed when value reached
		.setMaximumValue(1999999999)
		// Set increasing policy
		.setIncreaser(new SequentialIntIncreaser(1))
		// Determine an action when maximum value reached
		.setLimitationPolicy(LimitationPolicy.THROW_EXCEPTION_POLICY)
		.build();

// 1000000000, 1000000001, 1000000002, 10000000003…, 1999999999
// an exception occurred when value reached 2000000000

// Or simply,
// (in this case, max value is INTEGER.MAX_VALUE)
IntIdCache simpleCache = new IntIdCache(1000000000, new SequentialIntIncreaser(1));

// Or,
IntIdCache simplestCache = new IntIdCache();
```

### Create random-range incremental value generator
```java
// This idCache provides value with incrementing previous by RANDOM adder
IntIdCache randomCache = new IntIdCache(10000000000, new RandomLeapIntIncreaser(1, 9));

// 1000000000, 1000000005, 1000000009, 1000000012… 1999999998
```

[MIT License]: https://github.com/Junbong/idcache/blob/master/LICENSE
