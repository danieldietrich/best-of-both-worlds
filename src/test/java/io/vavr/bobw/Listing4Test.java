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
import java.util.concurrent.Callable;
import java.util.concurrent.LinkedBlockingDeque;

import static io.vavr.bobw.Async.waitFor;

public class Listing4Test {

    @Test
    public void shouldHandleSuccessOfAsyncComputation() {

        final BlockingQueue<String> queue = new LinkedBlockingDeque<>();
        final Callable<Integer> callable = Listing4Test::computeValue;
        
        testee.async(callable, result -> result
                .onSuccess(value -> queue.add("Success: " + value))
                .onFailure(error -> queue.add("Failure: " + error.getMessage()))
        );

        waitFor(queue, "Success: 1");

    }

    @Test
    public void shouldHandleFailureOfAsyncComputation() {

        final BlockingQueue<String> queue = new LinkedBlockingDeque<>();
        final Callable<Integer> callable = Listing4Test::computeValueExceptional;

        testee.async(callable, result -> result
                .onSuccess(value -> queue.add("Success: " + value))
                .onFailure(error -> queue.add("Failure: " + error.getMessage()))
        );

        waitFor(queue, "Failure: Error computing value");

    }

    private static Listing4 testee = new Listing4();

    private static int computeValue() throws Exception {
        Thread.sleep(250);
        return 1;
    }

    private static int computeValueExceptional() throws Exception {
        Thread.sleep(250);
        throw new Exception("Error computing value");
    }

}
