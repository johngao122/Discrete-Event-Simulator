class ServeEvent implements Event {
    private final double eventTime;
    private final Customer customer;
    private final Server server;
    private static final int SERVE_PRIORITY = 2;

    ServeEvent(Customer customer, Server server, double eventTime) {
        this.eventTime = eventTime;
        this.customer = customer;
        this.server = server;
    }

    @Override
    public Pair<Event, Shop> next(Shop shop) {
        Server currentServer = shop.getServers().get(this.server.getId() - 1);

        double waitTime = 0.0;
        Queue updatedQueue = currentServer.getQueue();

        if (!currentServer.getQueue().isEmpty()) {
            Pair<Customer, Queue> dequeueResult =
                    currentServer.getQueue().dequeue();
            updatedQueue = dequeueResult.u();
            waitTime = this.eventTime - customer.getarrivalTime();
        }

        Server serverAfterDequeue = new Server(
                currentServer.getId(), currentServer.getStartTime(), updatedQueue,
                true, currentServer.getCurrentServiceTime());

        Pair<Server, Shop> serveResult =
                shop.handleServing(customer, serverAfterDequeue);

        Server updatedServer = serveResult.t();
        Shop updatedShop = serveResult.u();
        Customer updatedCustomer =
                customer.updateServedTime(updatedServer.getStartTime());

        Statistics updatedStats =
                shop.getStatistics().incrementServed().addWaitTime(waitTime);
        updatedShop = updatedShop.updateStatistics(updatedStats);

        double serviceTime = updatedShop.getCurrentServiceTime();

        return new Pair<>(new DoneEvent(updatedCustomer, updatedServer,
                this.eventTime + serviceTime),
                updatedShop);
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
        return SERVE_PRIORITY;
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
        return String.format("%.3f customer %d serves by server %d",
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
