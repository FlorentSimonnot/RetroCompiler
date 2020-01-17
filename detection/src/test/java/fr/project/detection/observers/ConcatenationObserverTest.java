package fr.project.detection.observers;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ConcatenationObserverTest {
    private final ConcatenationObserver observer = new ConcatenationObserver();

    @Test
    void shouldThrowNullPointerExceptionWhenMessageOrTokenIsNull(){
        assertAll(
                () -> assertThrows(NullPointerException.class, () -> observer.onFeatureDetected(null, "concatenation")),
                () -> assertThrows(NullPointerException.class, () -> observer.onFeatureDetected("Concat", null)),
                () -> assertThrows(NullPointerException.class, () -> observer.onFeatureDetected(null, null))
        );
    }
}
