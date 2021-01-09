package input;

import java.util.ArrayList;
import java.util.List;

public final class InitialDataInput {
    private List<ConsumerInput> consumers = new ArrayList<>();
    private List<DistributorsInput> distributors = new ArrayList<>();
    private List<ProducerInput> producers = new ArrayList<>();

    public List<ProducerInput> getProducers() {
        return producers;
    }

    public void setProducers(List<ProducerInput> producers) {
        this.producers = producers;
    }

    public List<ConsumerInput> getConsumers() {
        return consumers;
    }

    public void setConsumers(final List<ConsumerInput> consumers) {
        this.consumers = consumers;
    }

    public List<DistributorsInput> getDistributors() {
        return distributors;
    }

    public void setDistributors(final List<DistributorsInput> distributors) {
        this.distributors = distributors;
    }
}
