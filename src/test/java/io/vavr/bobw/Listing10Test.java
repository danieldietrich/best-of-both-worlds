/* ____  ______________  ________________________  __________
 * \   \/   /      \   \/   /   __/   /      \   \/   /      \
 *  \______/___/\___\______/___/_____/___/\___\______/___/\___\
 *
 * Copyright 2014-2018 Vavr, http://vavr.io
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package io.vavr.bobw;

import org.junit.Test;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.function.Function;

import static io.vavr.bobw.Async.waitFor;
import static io.vavr.bobw.Checks.checkEquality;

/*
 * These tests are meant to be examples, especially they are not proving the monad laws.
 * Property tests would suit better than plain unit tests.
 * However, it is not possible to proof the laws for all functions that exist.
 *
 * Good property tests would cover combinations of all corner cases (null values, exceptions, ...).
 *
 * See http://blog.xebia.com/property-based-testing-java-junit-quickcheck-part-1-basics/
 * See http://www.baeldung.com/vavr-property-testing
 */
public class Listing10Test {

    @Test
    public void futureShouldObeyMonadLeftIdentity() {

        final BlockingQueue<Boolean> queue = new LinkedBlockingDeque<>();
        final String value = "Ok";
        final Function<String, Future<String>> cons = s -> Future.of(() -> s);
        final Function<String, Future<Integer>> f = str -> Future.of(str::length);

        testee.checkMonadLeftIdentity(value, cons, f, checkEquality(queue::add));

        waitFor(queue, true);

    }

    @Test
    public void futureShouldObeyMonadRightIdentity() {

        final BlockingQueue<Boolean> queue = new LinkedBlockingDeque<>();
        final Future<String> future = Future.of(() -> "Ok");
        final Function<String, Future<String>> cons = s -> Future.of(() -> s);
        
        testee.checkMonadRightIdentity(future, cons, checkEquality(queue::add));

        waitFor(queue, true);

    }

    @Test
    public void futureShouldObeyMonadAssociativity() {

        final BlockingQueue<Boolean> queue = new LinkedBlockingDeque<>();
        final Future<String> future = Future.of(() -> "Ok");
        final Function<String, Future<Integer>> f = s -> Future.of(s::length);
        final Function<Integer, Future<Boolean>> g = i -> Future.of(() -> i % 2 == 0);

        testee.checkMonadAssociativity(future, f, g, checkEquality(queue::add));

        waitFor(queue, true);

    }

    private static Listing10 testee = new Listing10();

}
