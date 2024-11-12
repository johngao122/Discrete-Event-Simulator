import java.util.Optional;

class ArriveEvent implements Event {
    private final double eventTime;
    private final Customer customer;
    private static final int ARRIVE_PRIORITY = 1;

    ArriveEvent(Customer customer, double eventTime) {
        this.eventTime = eventTime;
        this.customer = customer;
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
        return ARRIVE_PRIORITY;
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
    public Pair<Event, Shop> next(Shop shop) {
        return shop.findServer(customer)
            .map(server -> {
                Customer updatedCustomer =
                    customer.updateServedTime(this.eventTime);
                return new Pair<Event, Shop>(
                    new ServeEvent(updatedCustomer, server, this.eventTime),
                    shop);
            })
            .orElseGet(() -> {
                Pair<Optional<Server>, Shop> waitResult =
                    shop.handleWaiting(customer);

                Shop updatedShop = waitResult.u();

                return waitResult.t()
                    .map(server
                         -> new Pair<Event, Shop>(
                             new WaitEvent(customer, this.eventTime, server),
                             updatedShop))
                    .orElse(new Pair<Event, Shop>(
                        new LeaveEvent(customer, this.eventTime), updatedShop));
            });
    }

    @Override
    public String toString() {
        return String.format("%.3f customer %d arrives", this.eventTime,
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

        int priorityComparison = Integer.compare(this.getEventTypePriority(),
                                                 other.getEventTypePriority());
        if (priorityComparison != 0) {
            return priorityComparison;
        }

        return Integer.compare(this.getCustomerId(), other.getCustomerId());
    }
}
