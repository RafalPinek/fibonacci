package org.rafal.fibonacci;

public class WrongFibonacciArgument extends RuntimeException {

    public WrongFibonacciArgument(String msg) {
        super(msg);
    }
}
