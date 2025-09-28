package com.github.sfxd.trust.util;

public class ExceptionUtils {
    public static void uncheck(ThrowingRunnable r) {
        try {
            r.run();
        } catch (Exception ex) {
            handle(ex);
        }
    }

    @FunctionalInterface
    public interface ThrowingRunnable {
        void run() throws Exception;
    }

    private static void handle(Exception ex) {
        if (ex instanceof RuntimeException rt)
            throw rt;
        throw new UncheckedException(ex);
    }

    public static <T> T uncheck(ThrowingSupplier<T> supp) {
        try {
            return supp.get();
        } catch (Exception ex) {
            handle(ex);
        }

        // This is unreachable, but needs to be here to satisfy the compiler
        throw new IllegalStateException("Reached unreachable code?");
    }

    @FunctionalInterface
    public interface ThrowingSupplier<T> {
        T get() throws Exception;
    }
}
