package org.rafal.fibonacci;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigInteger;

@RestController
@RequestMapping(value = "/fib")
class FibonacciHandler {

    private final FibonacciCalculator calculator;

    FibonacciHandler(FibonacciCalculator calculator) {
        this.calculator = calculator;
    }

    @GetMapping
    BigInteger getFibonacci(@RequestParam(name = "n", required = false) Long n) {
       return calculator.getFibonacci(n);
    }
}
