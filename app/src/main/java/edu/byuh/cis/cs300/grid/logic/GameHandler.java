package edu.byuh.cis.cs300.grid.logic;

import android.os.Handler;
import android.os.Message;

import java.util.ArrayList;
import java.util.List;

/**
 * GameHandler is responsible for managing a list of TickListener objects
 * and sending periodic messages to invoke their onTick method.
 */
public class GameHandler extends Handler {
    private List<TickListener> listeners;

    /**
     * Constructor for GameHandler.
     * Initializes the list of listeners and starts the periodic message sending.
     */
    public GameHandler() {
        listeners = new ArrayList<>();
        sendMessageDelayed(obtainMessage(), 100);
    }

    /**
     * Registers a TickListener to the list of listeners.
     *
     * @param listener The TickListener to be added to the list.
     */
    public void registerListener(TickListener listener) {
        listeners.add(listener);
    }

    /**
     * Deregisters a TickListener from the list of listeners.
     *
     * @param listener The TickListener to be removed from the list.
     */
    public void deregisterListener(TickListener listener) {
        listeners.remove(listener);
    }

    /**
     * Handles incoming messages. Calls the onTick method of each registered listener.
     * Schedules the next message to be sent after a delay of 100 milliseconds.
     *
     * @param m The message received by the handler.
     */
    @Override
    public void handleMessage(Message m) {
        for (TickListener listener : listeners) {
            listener.onTick();
        }
        sendMessageDelayed(obtainMessage(), 100);
    }
}
