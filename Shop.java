import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.IntStream;

class Shop {
    private final int numberOfServers;
    private final List<Server> servers;
    private final Supplier<Double> serviceTime;
    private final int qmax;
    private final Statistics currentStats;
    private final Double currentServiceTime;

    Shop(int numberOfServers, double serviceTime, int qmax) {
        this(numberOfServers, () -> serviceTime, qmax);
    }

    Shop(int numberOfServers, Supplier<Double> serviceTime, int qmax) {
        this.numberOfServers = numberOfServers;
        this.serviceTime = serviceTime;
        servers = IntStream.rangeClosed(1, numberOfServers)
                      .boxed()
                      .map(x -> new Server(x, qmax))
                      .toList();
        this.qmax = qmax;
        this.currentStats = new Statistics();
        this.currentServiceTime = 0.0;
    }

    Shop(List<Server> servers, Supplier<Double> serviceTime, int qmax,
         Statistics stats) {
        this(servers, serviceTime, qmax, stats, serviceTime.get());
    }

    private Shop(List<Server> servers, Supplier<Double> serviceTime, int qmax,
                 Statistics stats, Double currentServiceTime) {
        this.numberOfServers = servers.size();
        this.servers = servers;
        this.serviceTime = serviceTime;
        this.qmax = qmax;
        this.currentStats = stats;
        this.currentServiceTime = currentServiceTime;
    }

    Optional<Server> findServer(Customer customer) {
        return servers.stream()
            .filter(server -> server.canServe(customer))
            .findFirst();
    }

    Pair<Server, Shop> handleServing(Customer customer, Server server) {

        double generatedServiceTime = this.serviceTime.get();
        Server updatedServer = server.serve(customer, generatedServiceTime);

        Shop updatedShop = new Shop(
            servers.stream()
                .map(s -> s.isSameServer(updatedServer) ? updatedServer : s)
                .toList(),
            serviceTime, qmax, this.currentStats, generatedServiceTime);

        return new Pair<>(updatedServer, updatedShop);
    }

    Pair<Optional<Server>, Shop> handleWaiting(Customer customer) {
        return servers.stream()
            .filter(server -> server.canQueue())
            .findFirst()
            .map(server -> {
                Queue updatedQueue = server.getQueue().enqueue(customer);
                Server updatedServer =
                    new Server(server.getId(), server.getStartTime(),
                               updatedQueue, server.isServing(), 0.0);
                return new Pair<>(Optional.of(updatedServer),
                                  update(updatedServer));
            })
            .orElse(new Pair<>(Optional.empty(), this));
    }

    Shop update(Server updatedServer) {
        List<Server> updatedServers =
            servers.stream()
                .map(server
                     -> server.isSameServer(updatedServer) ? updatedServer
                                                           : server)
                .toList();
        return new Shop(updatedServers, serviceTime, qmax, this.currentStats,
                        this.currentServiceTime);
    }

    Shop updateStatistics(Statistics newStats) {
        return new Shop(servers, serviceTime, qmax, newStats,
                        this.currentServiceTime);
    }

    Statistics getStatistics() {
        return this.currentStats;
    }

    List<Server> getServers() {
        return this.servers;
    }

    Double getCurrentServiceTime() {
        return this.currentServiceTime;
    }

    @Override
    public String toString() {
        return servers.toString();
    }
}
