package input;

import java.util.ArrayList;
import java.util.List;

public final class MonthlyUpdatesInput {
    private List<ConsumerInput> newConsumers = new ArrayList<>();

    private List<DistributorChanges> distributorChanges = new ArrayList<>();

    private List<ProducerChanges> producerChanges = new ArrayList<>();

    public List<ConsumerInput> getNewConsumers() {
        return newConsumers;
    }

    public void setNewConsumers(final List<ConsumerInput> newConsumers) {
        this.newConsumers = newConsumers;
    }

    public List<DistributorChanges> getDistributorChanges() {
        return distributorChanges;
    }

    public void setDistributorChanges(List<DistributorChanges> distributorChanges) {
        this.distributorChanges = distributorChanges;
    }

    public List<ProducerChanges> getProducerChanges() {
        return producerChanges;
    }

    public void setProducerChanges(List<ProducerChanges> producerChanges) {
        this.producerChanges = producerChanges;
    }

    @Override
    public String toString() {
        return "MonthlyUpdatesInput{"
                + "newConsumers=" + newConsumers
                + ", distributorChanges=" + distributorChanges
                + ", producerChanges=" + producerChanges
                + '}';
    }
}
