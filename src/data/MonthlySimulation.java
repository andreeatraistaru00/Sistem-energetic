package data;

import input.*;
import output.Contract;

import java.util.List;

public class MonthlySimulation {
    private MonthlyUpdatesInput updatesInput;
    private List<Distributor> distributors;
    private List<Consumer> consumers;
    private List<Producer> producers;
    private List<DistributorsInput> distributorsInputs;
    private List<ProducerInput> producerInputs;

    public MonthlySimulation(MonthlyUpdatesInput updatesInput,
                             List<Distributor> distributors,
                             List<Consumer> consumers,
                             List<Producer> producers,
                             List<DistributorsInput> distributorsInputs,
                             List<ProducerInput> producerInputs) {
        this.updatesInput = updatesInput;
        this.distributors = distributors;
        this.consumers = consumers;
        this.producers = producers;
        this.distributorsInputs = distributorsInputs;
        this.producerInputs = producerInputs;
    }

    public void makeUpdates(int month) {

        if (updatesInput.getDistributorChanges().size() != 0) {
            for (DistributorChanges costChanges : updatesInput.getDistributorChanges()) {
                distributors.get(costChanges.getId()).
                        setInitialInfrastructureCost(costChanges.getInfrastructureCost());
            }
        }
        // se executa update- urile pentru consumatori
        if (updatesInput.getNewConsumers().size() != 0) {
            for (ConsumerInput consumer : updatesInput.getNewConsumers()) {
                Entitati entitate;
                entitate = EntitatiFactory.createEntity(EntitatiFactory.EntityType.Consumer,
                        consumer, distributorsInputs.get(0), producerInputs.get(0));
                consumers.add((Consumer) entitate);
            }
        }
        // se fac noile contracte si se sterg cele vechi
        for (Consumer consumer : consumers) {
            if (consumer.getContractLength() == 0) {
                int oldDistributor = consumer.getIdDistributor();
                consumer.findDistributor(distributors);
                List<Contract> contracts = distributors.get(oldDistributor).getContracts();
                contracts.removeIf(contract -> contract.getConsumerId() == consumer.getId());
                distributors.get(oldDistributor).setContracts(contracts);
                distributors.get(consumer.getIdDistributor()).addContract(consumer);
            }
        }
        PayDebts payDebts = new PayDebts(consumers, distributors);
        payDebts.pay();
       // System.out.println(producers.get(0).getDistributorsIds().get(month));
        for (Producer producer : producers){
            producer.getDistributorsIds().get(month).addAll(producer.getDistributorsIds().get(month-1));
        }
        //System.out.println(producers.get(0).getDistributorsIds().get(month));
        if (updatesInput.getProducerChanges().size() != 0){
            Subject subject = new Subject(distributors);
            for (ProducerChanges changes : updatesInput.getProducerChanges()){
                producers.get(changes.getId()).
                        setEnergyPerDistributor(changes.getEnergyPerDistributor());

            }

                subject.notifyDistributors(updatesInput.getProducerChanges(), month, producers);

        }

    }
}
