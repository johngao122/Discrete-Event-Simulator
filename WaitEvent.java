class WaitEvent implements Event {
    private final double eventTime;
    private final Customer customer;
    private final Server server;
    private static final int WAIT_PRIORITY = 3;
    private final boolean loggable;

    WaitEvent(Customer customer, double eventTime, Server server,
              boolean shouldLog) {
        this.eventTime = eventTime;
        this.customer = customer;
        this.server = server;
        this.loggable = shouldLog;
    }

    WaitEvent(Customer customer, double eventTime, Server server) {
        this.eventTime = eventTime;
        this.customer = customer;
        this.server = server;
        this.loggable = true;
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
        return WAIT_PRIORITY;
    }

    @Override
    public boolean isLastEvent() {
        return false;
    }

    @Override
    public boolean shouldLog() {
        return this.loggable;
    }

    @Override
    public boolean shouldQueue() {
        return true;
    }

    @Override
    public Pair<Event, Shop> next(Shop shop) {
        Server currentServer = shop.getServers().get(server.getId() - 1);

        boolean isAtFront =
            currentServer.getQueue()
                .peekFirst()
                .map(frontCustomer -> frontCustomer.getId() == customer.getId())
                .orElse(false);

        if (isAtFront) {
            Customer updatedCustomer =
                customer.updateServedTime(currentServer.getStartTime());
            return new Pair<>(new ServeEvent(updatedCustomer, currentServer,
                                             currentServer.getStartTime()),
                              shop);
        }

        return new Pair<>(new WaitEvent(customer, currentServer.getStartTime(),
                                        currentServer, false),
                          shop);
    }

    @Override
    public String toString() {
        return String.format("%.3f customer %d waits at server %d",
                             this.eventTime, customer.getId(), server.getId());
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
