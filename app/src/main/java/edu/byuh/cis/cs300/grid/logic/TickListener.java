package edu.byuh.cis.cs300.grid.logic;

/**
 * An interface that defines a listener for a ticking event.
 * Classes that implement this interface should provide an
 * implementation for the onTick method, which will be called
 * at each tick (e.g., in a timed loop or event).
 */
public interface TickListener {

    /**
     * This method is called at each tick of a clock or timer.
     * Implementing classes should define the behavior that
     * should happen when this event is triggered.
     */
    void onTick();
}
