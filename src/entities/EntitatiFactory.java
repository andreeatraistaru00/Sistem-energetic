package entities;

import data.Consumer;
import data.Distributor;
import data.Producer;
import input.ConsumerInput;
import input.DistributorsInput;
import input.ProducerInput;

public final class EntitatiFactory {
    public enum EntityType {
        Consumer, Distributor, Producer
    }
    private static EntitatiFactory instance;
    private EntitatiFactory() {
    }

    /**
     * Singleton pattern
     * @return
     */
    public static EntitatiFactory getInstance() {
        if (instance == null) {
           instance = new EntitatiFactory();
        }

        return instance;
    }


    /**
     * @param entityType
     * @param consumer
     * @param distributor
     * @return
     */
    public static Entitati createEntity(final EntityType entityType,
                                        final ConsumerInput consumer,
                                        final DistributorsInput distributor,
                                        final ProducerInput producer) {
        return switch (entityType) {
            case Consumer -> new Consumer(consumer.getId(), consumer.getInitialBudget(),
                    consumer.getMonthlyIncome());
            case Distributor -> new Distributor(distributor.getId(),
                    distributor.getContractLength(),
                    distributor.getInitialBudget(),
                    distributor.getInitialInfrastructureCost(),
                    distributor.getEnergyNeededKW(),
                    distributor.getProducerStrategy());
            case Producer -> new Producer(producer.getId(), producer.getEnergyType(),
                        producer.getMaxDistributors(),
                        producer.getPriceKW(),
                        producer.getEnergyPerDistributor());
        };
    }
}
