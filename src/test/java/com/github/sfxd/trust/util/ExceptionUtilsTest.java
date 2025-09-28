package com.github.sfxd.trust.util;

import org.junit.jupiter.api.Test;

import static com.github.sfxd.trust.util.ExceptionUtils.uncheck;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ExceptionUtilsTest {

    @Test
    public void uncheckShouldWrapInUncheckedWhenMatchedInRunnable() {
        assertThrows(UncheckedException.class, () -> uncheck(ExceptionUtilsTest::throwingRunnable));
    }

    private static void throwingRunnable() throws TestException {
        throw new TestException();
    }

    private static class TestException extends Exception {}

    @Test
    public void rethrowsAnyRuntimeException() {
        assertThrows(
            IllegalArgumentException.class,
            () -> uncheck(() -> { throw new IllegalArgumentException(":("); })
        );
    }

    @Test
    @SuppressWarnings("unused")
    public void uncheckShouldWrapInUncheckedWhenMatchedInSupplier() {
        assertThrows(UncheckedException.class, () -> {
            int i = uncheck(ExceptionUtilsTest::throwingSupp);
        });
    }

    private static int throwingSupp() throws TestException {
        throwingRunnable();
        return -1;
    }
}
