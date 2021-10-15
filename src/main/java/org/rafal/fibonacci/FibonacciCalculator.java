package org.rafal.fibonacci;

import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
class FibonacciCalculator {

    // that size won't affect memory so much. The number 100_000 needs additional ~~0,5GB memory size.
    private final static long LIMIT_OF_LONG_TERM_CACHE = 10_000L;

    private final Map<Long, BigInteger> cache = new ConcurrentHashMap<>();

    FibonacciCalculator() {
        cache.put(1L, BigInteger.ONE);
        cache.put(2L, BigInteger.ONE);
        for (long i = 3L; i <= LIMIT_OF_LONG_TERM_CACHE; i++) {
            cache.put(i, cache.get(i - 1).add(cache.get(i - 2)));
        }
    }

    BigInteger getFibonacci(Long n) {
        validate(n);
        if (cache.containsKey(n)) {
            return cache.get(n);
        }
        return getFibonacciForHigherNumber(n);
    }

    private BigInteger getFibonacciForHigherNumber(long n) {
        Map<Long, BigInteger> tmpHigherCache = new HashMap<>();
        tmpHigherCache.put(LIMIT_OF_LONG_TERM_CACHE + 1, cache.get(LIMIT_OF_LONG_TERM_CACHE - 1).add(cache.get(LIMIT_OF_LONG_TERM_CACHE - 2)));
        tmpHigherCache.put(LIMIT_OF_LONG_TERM_CACHE + 2, cache.get(LIMIT_OF_LONG_TERM_CACHE).add(cache.get(LIMIT_OF_LONG_TERM_CACHE - 1)));
        for (long i = LIMIT_OF_LONG_TERM_CACHE + 3; i <= n; i++) {
            tmpHigherCache.put(i, tmpHigherCache.get(i - 1).add(tmpHigherCache.get(i - 2)));
        }
        return tmpHigherCache.get(n);
    }

    private void validate(Long n) {
        if (n == null) {
            throw new WrongFibonacciArgument("Argument must be provided.");
        }
        if (n < 1) {
            throw new WrongFibonacciArgument("Argument must be positive, but is: " + n);
        }
    }

}
