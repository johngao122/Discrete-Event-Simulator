class EndEvent implements Event {
    private final double eventTime;
    private final Customer customer;

    EndEvent(Customer customer, double eventTime) {
        this.eventTime = eventTime;
        this.customer = customer;
    }

    @Override
    public Pair<Event, Shop> next(Shop shop) {
        return new Pair<>(this, shop);
    }

    @Override
    public double getEventTime() {
        return this.eventTime;
    }

    @Override
    public boolean shouldQueue() {
        return false;
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
    public boolean shouldLog() {
        return false;
    }

    @Override
    public boolean isLastEvent() {
        return true;
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
