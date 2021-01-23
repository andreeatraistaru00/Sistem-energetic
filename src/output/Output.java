package output;

import java.util.List;

public final class Output {

    private List<Consumatori> consumers;

    private List<Distributori> distributors;

    private List<EnergyProducer> energyProducers;

    public Output(final List<Consumatori> consumatori, final List<Distributori> distributori,
                     final List<EnergyProducer> energyProducerList) {
        this.consumers = consumatori;
        this.distributors = distributori;
        this.energyProducers = energyProducerList;
    }

    public List<Consumatori> getConsumers() {
        return consumers;
    }

    public void setConsumers(final List<Consumatori> consumers) {
        this.consumers = consumers;
    }

    public List<Distributori> getDistributors() {
        return distributors;
    }

    public void setDistributors(final List<Distributori> distributors) {
        this.distributors = distributors;
    }

    public List<EnergyProducer> getEnergyProducers() {
        return energyProducers;
    }

    public void setEnergyProducers(List<EnergyProducer> energyProducers) {
        this.energyProducers = energyProducers;
    }
}
