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

import java.util.concurrent.*;
import java.util.function.Consumer;
import java.util.function.Function;

interface Future<T> {

    static <T> Future<T> of(Callable<T> callable) {
        return of(ForkJoinPool.commonPool(), callable);
    }
    
    static <T> Future<T> of(Executor executor, Callable<T> callable) {
        final CompletableFuture<Try<T>> completableFuture = new CompletableFuture<>();
        executor.execute(() -> completableFuture.complete(Try.of(callable)));
        return new FutureImpl<>(completableFuture);
    }

    <U> Future<U> map(Function<T, U> f);

    <U> Future<U> flatMap(Function<T, Future<U>> f);

    Future<T> onComplete(Consumer<Try<T>> callback);

}

/*
 * This is a simple example implementation of the Future interface that relies on CompletableFuture.
 * Vavr includes a more mature implementation that does rely on threads only.
 * See https://github.com/vavr-io/vavr/blob/16e50d514b015464afee1547fdc686fef3c1fbdf/vavr/src/main/java/io/vavr/concurrent/FutureImpl.java
 */
final class FutureImpl<T> implements Future<T> {

    private final CompletableFuture<Try<T>> delegate;

    FutureImpl(CompletableFuture<Try<T>> delegate) {
        this.delegate = delegate;
    }
    
    @Override
    public <U> Future<U> map(Function<T, U> f) {
        return new FutureImpl<>(delegate.thenApplyAsync(result -> result.map(f)));
    }

    @Override
    public <U> Future<U> flatMap(Function<T, Future<U>> f) {
        final FutureImpl<U> flatMapped = new FutureImpl<>(new CompletableFuture<>());
        onComplete(result -> result.map(f)
                .onSuccess(future -> future.onComplete(flatMapped.delegate::complete))
                .onFailure(error -> flatMapped.delegate.complete(Try.failure(error)))
        );
        return flatMapped;
    }

    @Override
    public Future<T> onComplete(Consumer<Try<T>> callback) {
        delegate.thenAccept(callback);
        return this;
    }

    @Override
    public String toString() {
        Object result;
        try {
            result = delegate.isDone() ? delegate.get() : "?";
        } catch (InterruptedException | ExecutionException e) {
            result = "?";
        }
        return "Future(" + result + ")";
    }

}
