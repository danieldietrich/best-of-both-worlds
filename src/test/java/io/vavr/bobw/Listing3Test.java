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
import java.util.function.Consumer;

import static io.vavr.bobw.Async.waitFor;

public class Listing3Test {

    @Test
    public void shouldHandleSuccessOfAsyncComputation() {

        final BlockingQueue<String> queue = new LinkedBlockingDeque<>();
        final Callable<Integer> callable = Listing3Test::computeValue;
        final Consumer<Integer> onSuccess = value -> queue.add("Success: " + value);
        final Consumer<Exception> onFailure = x -> queue.add("Failure: " + x.getMessage());

        testee.async(callable, onSuccess, onFailure);

        waitFor(queue, "Success: 1");

    }

    @Test
    public void shouldHandleFailureOfAsyncComputation() {

        final BlockingQueue<String> queue = new LinkedBlockingDeque<>();
        final Callable<Integer> callable = Listing3Test::computeValueExceptional;
        final Consumer<Integer> onSuccess = value -> queue.add("Success: " + value);
        final Consumer<Exception> onFailure = x -> queue.add("Failure: " + x.getMessage());

        testee.async(callable, onSuccess, onFailure);

        waitFor(queue, "Failure: Error computing value");

    }

    private static Listing3 testee = new Listing3();

    private static int computeValue() throws Exception {
        Thread.sleep(250);
        return 1;
    }

    private static int computeValueExceptional() throws Exception {
        Thread.sleep(250);
        throw new Exception("Error computing value");
    }

}
