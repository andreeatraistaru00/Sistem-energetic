import com.fasterxml.jackson.databind.ObjectMapper;
import data.Consumer;
import data.Distributor;
import entities.Entitati;
import entities.EntitatiFactory;
import data.MonthlySimulation;
import data.Producer;
import input.ConsumerInput;
import input.DistributorsInput;
import input.InputParser;
import input.MonthlyUpdatesInput;
import input.ProducerInput;
import output.Consumatori;
import output.Contract;
import output.Distributori;
import output.EnergyProducer;
import output.MonthlyStats;
import output.Output;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Entry point to the simulation
 */
public final class Main {

    private Main() { }

    /**
     * Main function which reads the input file and starts simulation
     *
     * @param args input and output files
     * @throws Exception might error when reading/writing/opening files, parsing JSON
     */
    public static void main(final String[] args) throws Exception {

        ObjectMapper objectMapper = new ObjectMapper();
        InputParser inputParser = objectMapper.readValue(new File(args[0]), InputParser.class);

        List<ConsumerInput> consumerInputs = inputParser.getInitialData().getConsumers();
        List<DistributorsInput> distributorsInputs = inputParser.getInitialData().getDistributors();
        List<ProducerInput> producerInputs = inputParser.getInitialData().getProducers();
        List<MonthlyUpdatesInput> updates = new ArrayList<>(inputParser.getMonthlyUpdates());

        List<Consumatori> consumersOut = new ArrayList<>();
        List<Distributori> distributorsOut = new ArrayList<>();
        List<EnergyProducer> energyProducers = new ArrayList<>();

        List<Consumer> consumers = new ArrayList<>();
        List<Distributor> distributors = new ArrayList<>();
        List<Producer> producers = new ArrayList<>();

        EntitatiFactory factory = EntitatiFactory.getInstance();
        /**
         * se adauga obiecte in lista de consumatori
         * noii consumatori sunt generati cu factory si contin informatiile din input
         */
        for (int i = 0; i < consumerInputs.size(); i++) {
            Entitati entitate = factory.createEntity(EntitatiFactory.EntityType.Consumer,
                    consumerInputs.get(i), distributorsInputs.get(0), producerInputs.get(0));
            consumers.add((Consumer) entitate);
        }
        /**
         * se adauga obiecte in lista de distribuitori
         * noii distribuitori sunt generati cu factory si contin informatiile din input
         */
        for (int i = 0; i < distributorsInputs.size(); i++) {
            Entitati entitate = factory.createEntity(EntitatiFactory.EntityType.Distributor,
                    consumerInputs.get(0), distributorsInputs.get(i), producerInputs.get(0));
            distributors.add((Distributor) entitate);
        }
        /**
         * se adauga obiecte in lista de producatori
         * noii producatori sunt generati cu factory si contin informatiile din input
         */
        for (int i = 0; i < producerInputs.size(); i++) {
            Entitati entitate = factory.createEntity(EntitatiFactory.EntityType.Producer,
                    consumerInputs.get(0), distributorsInputs.get(0), producerInputs.get(i));
            producers.add((Producer) entitate);
            ((Producer) entitate).addDistributorsIds(updates.size());
        }


        /**
         *  Se executa runda initiala
         */
        for (Distributor distributor : distributors) {
            while (!distributor.getEnergy()) {
                distributor.chooseProducer(producers, 0);
            }
            distributor.setProductionCost();
        }

        for (Consumer consumer : consumers) {
            consumer.findDistributor(distributors);
            distributors.get(consumer.getIdDistributor()).addContract(consumer);

            if (consumer.changeBuget(consumer.getInitialBudget()) < 0) {
                consumer.notEnoughMoney(consumer, distributors);

            } else {
                int changedBuget = consumer.changeBuget(consumer.getInitialBudget());
                consumer.setInitialBudget(changedBuget);
                consumer.setContractLength(consumer.getContractLength() - 1);
            }
        }

        for (Distributor distributor : distributors) {
            distributor.setNoContracts(distributor.getContracts().size());
            distributor.setInitialBudget(distributor.costTotal(distributor.getInitialBudget()));
        }
        // se executa update-urile
        int month = 0;
        for (MonthlyUpdatesInput updatesInput : updates) {
            month++;
            MonthlySimulation monthlyUpdates;
            monthlyUpdates = new MonthlySimulation(updatesInput,
                    distributors,
                    consumers,
                    producers,
                    distributorsInputs,
                    producerInputs);
            monthlyUpdates.makeUpdates(month);
        // se introduc monthly states
            for (Producer producer : producers) {
                Collections.sort(producer.getDistributorsIds().get(month));
                producer.getMonthlyStats().add(new MonthlyStats(month,
                        producer.getDistributorsIds().get(month)));
            }
        }
        // se creaza clasele de output
        for (Producer producer : producers) {
            energyProducers.add(new EnergyProducer(producer.getId(),
                    producer.getMaxDistributors(),
                    producer.getPriceKW(),
                    producer.getEnergyType(),
                    producer.getEnergyPerDistributor(),
                    producer.getMonthlyStats()));
        }
        for (Consumer consumer : consumers) {
            consumersOut.add(new Consumatori(consumer.getId(),
                    consumer.isBankrupt(),
                    consumer.getInitialBudget()));
        }
        for (Distributor distributor : distributors) {
            for (Contract contract : distributor.getContracts()) {
                // se adauga in contracte lunile de plata ramase
                int remainedMonths = consumers.get(contract.getConsumerId()).getContractLength();
                contract.setRemainedContractMonths(remainedMonths);
            }
            distributorsOut.add(new Distributori(distributor.getId(),
                    distributor.getEnergyNeededKW(),
                    distributor.getContractCost(),
                    distributor.getInitialBudget(),
                    distributor.getProducerStrategy(),
                    distributor.isBankrupt(),
                    distributor.getContracts()));
        }

        Output output = new Output(consumersOut, distributorsOut, energyProducers);
        objectMapper.writeValue(new File(args[1]), output);

    }
}
