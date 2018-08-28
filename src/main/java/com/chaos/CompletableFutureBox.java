package com.chaos;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeoutException;
import java.util.function.Consumer;
import java.util.function.Function;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

/**
 * Created by chaos on 2018/8/27.
 * uncompleted
 */
public class CompletableFutureBox<T> {
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1, new ThreadFactoryBuilder().setDaemon(true).setNameFormat("timeout-%d").build());

    private CompletableFuture<T> future;
    private CompletableFuture<T> timeoutFuture;
    private Duration duration;

    private CompletableFutureBox(CompletableFuture<T> future) {
        this.future = future;
    }

    public static <T> CompletableFutureBox<T> create(CompletableFuture<T> future) {
        return new CompletableFutureBox<>(future);
    }

    public CompletableFutureBox<T> timeout(Duration duration) {
        this.duration = duration;
        this.timeoutFuture = timeoutAfter(this.duration);
        return this;
    }

    public CompletableFutureBox<T> timeout(Duration duration, Function<Duration, CompletableFuture<T>> function) {
        this.duration = duration;
        this.timeoutFuture = function.apply(duration);
        return this;
    }

    public <U> CompletableFuture<U> then(Function<T, U> function) {
        return future.applyToEither(timeoutFuture, Function.identity())
                     .thenApply(function);
    }

    public CompletableFuture<Void> then(Consumer<T> consumer) {
        return future.applyToEither(timeoutFuture, Function.identity())
                     .thenAccept(consumer);
    }

    private CompletableFuture<T> timeoutAfter(Duration duration) {
        final CompletableFuture<T> promise = new CompletableFuture<>();
        scheduler.schedule(() -> {
            final TimeoutException ex = new TimeoutException("Timeout after " + duration);
            return promise.completeExceptionally(ex);
        }, duration.toMillis(), MILLISECONDS);
        return promise;
    }
}
