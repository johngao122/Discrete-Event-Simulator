class Statistics {
    private final double totalWaitTime;
    private final int totalServed;
    private final int totalLeft;

    Statistics() {
        this.totalWaitTime = 0;
        this.totalServed = 0;
        this.totalLeft = 0;
    }

    private Statistics(double totalWaitTime, int totalServed, int totalLeft) {
        this.totalWaitTime = totalWaitTime;
        this.totalServed = totalServed;
        this.totalLeft = totalLeft;
    }

    Statistics addWaitTime(double waitTime) {
        return new Statistics(this.totalWaitTime + waitTime, this.totalServed,
                              this.totalLeft);
    }

    Statistics incrementServed() {
        return new Statistics(this.totalWaitTime, this.totalServed + 1,
                              this.totalLeft);
    }

    Statistics incrementLeft() {
        return new Statistics(this.totalWaitTime, this.totalServed,
                              this.totalLeft + 1);
    }

    @Override
    public String toString() {
        double averageWaitingTime =
            totalServed == 0 ? 0 : totalWaitTime / totalServed;
        return String.format("[%.3f %d %d]", averageWaitingTime, totalServed,
                             totalLeft);
    }
}
