class LeaveEvent implements Event {
    private final double eventTime;
    private final Customer customer;

    LeaveEvent(Customer customer, double eventTime) {
        this.eventTime = eventTime;
        this.customer = customer;
    }

    @Override
    public Pair<Event, Shop> next(Shop shop) {
        return new Pair<>(
                new EndEvent(customer, this.eventTime),
                shop.updateStatistics(shop.getStatistics().incrementLeft()));
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
        return Integer.MAX_VALUE;
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
        return String.format("%.3f customer %d leaves", this.eventTime,
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
