interface Event extends Comparable<Event> {

    double getEventTime();

    Pair<Event, Shop> next(Shop shop);

    int getEventTypePriority();

    int getCustomerId();

    boolean isLastEvent();

    boolean shouldQueue();

    boolean shouldLog();

}
