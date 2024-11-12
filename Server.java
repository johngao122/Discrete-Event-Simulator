class Server {
    private final int id;
    private final double startServiceTime;
    private final Queue customerQueue;
    private final boolean serving;
    private final double currentServiceTime;

    Server(int id, int qmax) {
        this.id = id;
        this.startServiceTime = 0;
        this.customerQueue = new Queue(qmax);
        this.serving = false;
        this.currentServiceTime = 0;
    }

    Server(int id, double startServiceTime, Queue customerQueue,
           boolean serving, double currentServiceTime) {
        this.id = id;
        this.startServiceTime = startServiceTime;
        this.customerQueue = customerQueue;
        this.serving = serving;
        this.currentServiceTime = currentServiceTime;
    }

    Server serve(Customer customer, double serviceTime) {
        return new Server(id, customer.getServedTime() + serviceTime,
                          customerQueue, true, serviceTime);
    }

    boolean canServe(Customer customer) {
        return !serving && customerQueue.isEmpty() &&
                startServiceTime <= customer.getarrivalTime();
    }

    boolean isSameServer(Server other) {
        return this.id == other.id;
    }

    boolean canQueue() {
        return !customerQueue.isFull();
    }

    Queue getQueue() {
        return this.customerQueue;
    }

    double getStartTime() {
        return this.startServiceTime;
    }

    boolean isServing() {
        return this.serving;
    }

    int getId() {
        return this.id;
    }

    double getCurrentServiceTime() {
        return this.currentServiceTime;
    }

    @Override
    public String toString() {
        return String.format("server %d", id);
    }
}
