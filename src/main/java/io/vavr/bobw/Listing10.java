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

import java.util.function.Function;

final class Listing10 {

    <T, U> void checkMonadLeftIdentity(T value, Function<T, Future<T>> cons, Function<T, Future<U>> f, Equals<Future<U>> equals) {
        equals.accept(cons.apply(value).flatMap(f), f.apply(value));
    }
    
    <T> void checkMonadRightIdentity(Future<T> future, Function<T, Future<T>> cons, Equals<Future<T>> equals) {
        equals.accept(future.flatMap(cons), future);
    }
    
    <T, U, V> void checkMonadAssociativity(Future<T> future, Function<T, Future<U>> f, Function<U, Future<V>> g, Equals<Future<V>> equals) {
        equals.accept(future.flatMap(f).flatMap(g), future.flatMap(value -> f.apply(value).flatMap(g)));
    }

}
