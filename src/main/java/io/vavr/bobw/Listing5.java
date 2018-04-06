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

import java.util.concurrent.Callable;
import java.util.function.Consumer;

final class Listing5 {

    private final String value1;

    Listing5(String value1) {
        this.value1 = value1;
    }

    <T> void async(Callable<T> callable, Consumer<Try<T>> callback) {
        new Thread(() -> callback.accept( Try.of(callable) )).start();
    }

    void example(Consumer<Boolean> resultHandler, Consumer<Exception> errorHandler) {
        async(this::computeValue1, result1 -> result1
                .onSuccess(value1 ->
                        async(() -> computeValue2(value1), result2 -> result2
                                .onSuccess(value2 ->
                                        async(() -> computeValue3(value2), result3 -> result3
                                                .onSuccess(resultHandler)
                                                .onFailure(errorHandler)
                                        )
                                )
                                .onFailure(errorHandler)
                        )
                )
                .onFailure(errorHandler)
        );
    }

    private String computeValue1() {
        return value1;
    }

    private int computeValue2(String s) {
        return s.length();
    }

    private boolean computeValue3(int i) {
        return i % 2 == 0;
    }

}
