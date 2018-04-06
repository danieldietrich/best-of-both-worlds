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

import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.function.Consumer;
import java.util.function.Function;

/*
 * A 'sealed' sum-type with exactly two implementations, Success and Failure
 *
 * See https://alvinalexander.com/scala/benefits-of-sealed-traits-in-scala-java-enums
 * See https://www.quora.com/What-is-a-sum-type
 */
public abstract class Try<T> {

    public static <T> Try<T> of(Callable<T> computation) {
        try {
            return success(computation.call());
        } catch(Exception x) {
            return failure(x);
        }
    }

    public static <T> Try<T> success(T t) {
        return new Success<>(t);
    }

    public static <T> Try<T> failure(Exception x) {
        return new Failure<>(x);
    }

    // use Try.of(Callable) instead
    private Try() {}

    public abstract <U> Try<U> flatMap(Function<T, Try<U>> f);

    public abstract <U> Try<U> map(Function<T, U> f);

    public abstract Try<T> onSuccess(Consumer<? super T> handler);

    public abstract Try<T> onFailure(Consumer<? super Exception> handler);

    // Internal implementation of a successful `Try`
    private static final class Success<T> extends Try<T> {

        private final T value;

        private Success(T value) {
            this.value = value;
        }

        @Override
        public <U> Try<U> flatMap(Function<T, Try<U>> f) {
            try {
                return f.apply(value);
            } catch(Exception x) {
                return new Failure<>(x);
            }
        }
        
        @Override
        public <U> Try<U> map(Function<T, U> f) {
            return Try.of(() -> f.apply(value));
        }

        @Override
        public Try<T> onSuccess(Consumer<? super T> handler) {
            handler.accept(value);
            return this;
        }

        @Override
        public Try<T> onFailure(Consumer<? super Exception> handler) {
            return this;
        }

        @Override
        public boolean equals(Object o) {
            return (o == this) || (o instanceof Success && Objects.equals(value, ((Success) o).value));
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(value);
        }

        @Override
        public String toString() {
            return "Success(" + value + ")";
        }
    }

    // Internal implementation of a failed `Try`.
    private static final class Failure<T> extends Try<T> {

        private final Exception cause;

        private Failure(Exception cause) {
            this.cause = cause;
        }

        @SuppressWarnings("unchecked")
        @Override
        public <U> Try<U> flatMap(Function<T, Try<U>> f) {
            return (Try<U>) this;
        }

        @SuppressWarnings("unchecked")
        @Override
        public <U> Try<U> map(Function<T, U> f) {
            return (Try<U>) this;
        }

        @Override
        public Try<T> onSuccess(Consumer<? super T> handler) {
            return this;
        }

        @Override
        public Try<T> onFailure(Consumer<? super Exception> handler) {
            handler.accept(cause);
            return this;
        }

        @Override
        public boolean equals(Object o) {
            return (o == this) || (o instanceof Failure && Arrays.deepEquals(cause.getStackTrace(), ((Failure<?>) o).cause.getStackTrace()));
        }

        @Override
        public int hashCode() {
            return Arrays.hashCode(cause.getStackTrace());
        }

        @Override
        public String toString() {
            return "Failure(" + cause + ")";
        }
    }

}
