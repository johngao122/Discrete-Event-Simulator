import java.util.function.Supplier;

class Customer implements Comparable<Customer> {
    private final int id;
    private final double arrivalTime;
    private final double servedTime;
    private final Supplier<Double> serviceTime;

    Customer(int id, double arrivalTime, Supplier<Double> serviceTime,
             double servedTime) {
        this.id = id;
        this.arrivalTime = arrivalTime;
        this.serviceTime = serviceTime;
        this.servedTime = servedTime;
    }

    Customer(int id, double arrivalTime) {
        this.id = id;
        this.arrivalTime = arrivalTime;
        this.serviceTime = new DefaultServiceTime();
        this.servedTime = 0;
    }

    Customer updateServedTime(double servedTime) {
        return new Customer(id, arrivalTime, serviceTime, servedTime);
    }

    public String toString() {
        return String.format("customer %d", id);
    }

    double getarrivalTime() {
        return this.arrivalTime;
    }

    double getServedTime() {
        return this.servedTime;
    }

    int getId() {
        return this.id;
    }

    public int compareTo(Customer other) {
        return Double.compare(this.arrivalTime, other.getarrivalTime());
    }
}
