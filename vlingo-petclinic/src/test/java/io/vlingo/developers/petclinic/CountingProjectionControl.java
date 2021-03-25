package io.vlingo.developers.petclinic;

import io.vlingo.actors.testkit.AccessSafely;
import io.vlingo.lattice.model.projection.ProjectionControl;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CountingProjectionControl implements ProjectionControl {
    private AccessSafely access;
    private final Map<String, Integer> confirmations = new ConcurrentHashMap<>();

    @Override
    public void confirmProjected(final String projectionId) {
        access.writeUsing("confirmations", projectionId);
    }

    public AccessSafely afterCompleting(final int times) {
        access = AccessSafely.afterCompleting(times);
        access.writingWith("confirmations", (String projectionId) -> {
            final int count = confirmations.getOrDefault(projectionId, 0);
            confirmations.put(projectionId, count + 1);
        });
        access.readingWith("confirmations", () -> confirmations);
        return access;
    }
}
