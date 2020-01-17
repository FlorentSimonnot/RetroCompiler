package fr.project.detection.observers;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FeaturesManagerTest {
    private final FeaturesObserverFactory observersFactory = new FeaturesObserverFactory();

    @BeforeEach
    void create(){
        observersFactory.register("try-with-resources", new TryWithResourcesObserver());
        observersFactory.register("nestMember", new NestMemberObserver());
        observersFactory.register("concatenation", new ConcatenationObserver());
        observersFactory.register("lambda", new LambdaObserver());
        observersFactory.register("record", new RecordObserver());
    }

    @Test
    void shouldCreateAllOfObserversWhenFeatureOptionIsAbsent(){
        var observers = FeaturesManager.createObservers("", observersFactory);
        assertAll(
                () -> assertEquals(5, observers.size())
        );
    }

    @Test
    void shouldCreateFeatureAccordingOptionWhenUserGetFeatures(){
        var observers = FeaturesManager.createObservers("record, lambda", observersFactory);
        var observers2 = FeaturesManager.createObservers("record, try-with-resources, concatenation ", observersFactory);
        assertAll(
                () -> assertEquals(2, observers.size()),
                () -> assertEquals(3, observers2.size())
        );
    }

}
