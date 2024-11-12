import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

class Queue {
    private final int maxSlots;
    private final int availableSlots;
    private final List<Customer> customers;

    Queue(int maxSlots) {
        this.maxSlots = maxSlots;
        this.availableSlots = maxSlots;
        this.customers = List.of();
    }

    Queue(int maxSlots, List<Customer> customers, int availableSlots) {
        this.maxSlots = maxSlots;
        this.customers = customers;
        this.availableSlots = availableSlots;
    }

    Queue enqueue(Customer customer) {
        if (availableSlots <= 0) {
            return this;
        }
        return new Queue(
            maxSlots,
            Stream.concat(customers.stream(), Stream.of(customer)).toList(),
            availableSlots - 1);
    }

    Pair<Customer, Queue> dequeue() {
        Customer nextCustomer = customers.getFirst();
        List<Customer> remainingCustomers = customers.stream().skip(1).toList();

        return new Pair<>(nextCustomer, new Queue(maxSlots, remainingCustomers,
                                                  availableSlots + 1));
    }

    Optional<Customer> peekFirst() {
        return customers.isEmpty() ? Optional.empty()
                                   : Optional.of(customers.getFirst());
    }

    boolean isEmpty() {
        return customers.isEmpty();
    }

    boolean isFull() {
        return availableSlots == 0;
    }

    int getQueueLength() {
        return maxSlots - availableSlots;
    }

    @Override
    public String toString() {
        return String.format("Queue[%d/%d]", this.getQueueLength(),
                             this.maxSlots);
    }
}
