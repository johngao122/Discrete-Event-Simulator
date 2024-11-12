class DoneEvent implements Event {
    private final double eventTime;
    private final Customer customer;
    private final Server server;
    private static final int DONE_PRIORITY = 0;

    DoneEvent(Customer customer, Server server, double eventTime) {
        this.eventTime = eventTime;
        this.customer = customer;
        this.server = server;
    }

    @Override
    public Pair<Event, Shop> next(Shop shop) {
        Server currentServer = shop.getServers().get(server.getId() - 1);
        Server updatedServer =
                new Server(currentServer.getId(), currentServer.getStartTime(),
                        currentServer.getQueue(), false, 0.0);
        Shop updatedShop = shop.update(updatedServer);
        return new Pair<>(new EndEvent(customer, this.eventTime), updatedShop);
    }

    @Override
    public double getEventTime() {
        return this.eventTime;
    }

    @Override
    public int getCustomerId() {
        return this.customer.getId();
    }

    @Override
    public int getEventTypePriority() {
        return DONE_PRIORITY;
    }

    @Override
    public boolean isLastEvent() {
        return false;
    }

    @Override
    public boolean shouldQueue() {
        return true;
    }

    @Override
    public boolean shouldLog() {
        return true;
    }

    @Override
    public String toString() {
        return String.format("%.3f customer %d done", this.eventTime,
                             customer.getId());
    }

    @Override
    public int compareTo(Event other) {
        int timeComparison =
            Double.compare(this.getEventTime(), other.getEventTime());
        if (timeComparison != 0) {
            return timeComparison;
        }

        int customerComparison =
            Integer.compare(this.getCustomerId(), other.getCustomerId());
        if (customerComparison != 0) {
            return customerComparison;
        }

        return Integer.compare(this.getEventTypePriority(),
                               other.getEventTypePriority());
    }
}
