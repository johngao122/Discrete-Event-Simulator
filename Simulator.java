import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Stream;

class Simulator {
    private final int numOfServers;
    private final int qmax;
    private final int numOfCustomers;
    private final Supplier<Double> serviceTime;
    private final List<Pair<Integer, Double>> arrivals;
    private final PQ<Event> events;

    Simulator(int numOfServers, int qmax, Supplier<Double> serviceTime,
              int numOfCustomers, List<Pair<Integer, Double>> arrivals) {
        this.numOfServers = numOfServers;
        this.qmax = qmax;
        this.serviceTime = serviceTime;
        this.numOfCustomers = numOfCustomers;
        this.arrivals = arrivals;
        this.events = makePQ();
    }

    State run() {
        Shop initialShop = new Shop(numOfServers, serviceTime, qmax);
        State initialState = new State(events, initialShop);

        return Stream
            .iterate(initialState,
                     state -> !state.isEmpty(), state -> state.next())
            .reduce((first, second) -> second)
            .orElse(initialState)
            .displayStatistics();
    }

    private PQ<Event> makePQ() {
        PQ<Event> pq = new PQ<>();
        for (Pair<Integer, Double> arrival : arrivals) {
            int id = arrival.t();
            double arrivalTime = arrival.u();
            Customer customer = new Customer(id, arrivalTime, serviceTime, 0);
            Event event = new ArriveEvent(customer, arrivalTime);
            pq = pq.add(event);
        }
        return pq;
    }

    @Override
    public String toString() {
        return "Simulator created";
    }
}
