import java.util.Optional;

class State {
    private final Shop shop;
    private final PQ<Event> events;
    private final String eventLog;
    private final boolean done;
    private final Statistics stats;
    private final double lastArrivalTime;

    State(PQ<Event> events, Shop shop) {
        this(events, shop, false, "", new Statistics(), 0.0);
    }

    State(PQ<Event> events, Shop shop, boolean done, String eventLog,
          Statistics stats, double lastArrivalTime) {
        this.events = events;
        this.shop = shop;
        this.eventLog = eventLog;
        this.done = done;
        this.stats = stats;
        this.lastArrivalTime = lastArrivalTime;
    }

    State displayStatistics() {
        String updatedLog =
                this.eventLog + String.format("%s", this.stats.toString());
        return new State(this.events, this.shop, this.done, updatedLog,
                this.stats, this.lastArrivalTime);
    }

    State next() {
        if (events.isEmpty()) {
            return new State(events, shop, true, eventLog, stats,
                    lastArrivalTime);
        }

        Pair<Optional<Event>, PQ<Event>> poll = events.poll();
        Event currentEvent = poll.t().orElseThrow();
        PQ<Event> remaining = poll.u();

        Pair<Event, Shop> nextPair = currentEvent.next(shop);
        Event nextEvent = nextPair.t();
        Shop updatedShop = nextPair.u();

        PQ<Event> updatedEvents =
                nextEvent.shouldQueue() ? remaining.add(nextEvent) : remaining;
        String updatedLog = currentEvent.shouldLog()
                ? eventLog + currentEvent + "\n"
                : eventLog;
        boolean nextDone =
                updatedEvents.isEmpty() && currentEvent.isLastEvent();

        return new State(updatedEvents, updatedShop, nextDone, updatedLog,
                updatedShop.getStatistics(), this.lastArrivalTime);
    }

    boolean isEmpty() {
        return events.isEmpty() && done;
    }

    public String toString() {
        return eventLog.trim();
    }
}
