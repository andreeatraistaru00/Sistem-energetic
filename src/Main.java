import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import data.*;
import entities.EnergyType;
import input.*;
import output.*;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;


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

            List<Consumatori> consumersOut = new ArrayList<>();
            List<Distributori> distributorsOut = new ArrayList<>();
            List<EnergyProducer> energyProducers = new ArrayList<>();

            List<Consumer> consumers = new ArrayList<>();
            List<Distributor> distributors = new ArrayList<>();
            List<Producer> producers = new ArrayList<>();

            /**
             * se adauga obiecte in lista de consumatori
             * noii consumatori sunt generati cu factory si contin informatiile din input
             */
            for (int i = 0; i < consumerInputs.size(); i++) {
                Entitati entitate = EntitatiFactory.createEntity(EntitatiFactory.EntityType.Consumer,
                        consumerInputs.get(i), distributorsInputs.get(0), producerInputs.get(0));
                consumers.add((Consumer) entitate);
            }
            /**
             * se adauga obiecte in lista de distribuitori
             * noii distribuitori sunt generati cu factory si contin informatiile din input
             */
            for (int i = 0; i < distributorsInputs.size(); i++) {
                Entitati entitate = EntitatiFactory.createEntity(EntitatiFactory.EntityType.Distributor,
                        consumerInputs.get(0), distributorsInputs.get(i), producerInputs.get(0));
                distributors.add((Distributor) entitate);
            }
        /**
         * se adauga obiecte in lista de producatori
         * noii producatori sunt generati cu factory si contin informatiile din input
         */
        for (int i = 0; i < producerInputs.size(); i++) {
            Entitati entitate = EntitatiFactory.createEntity(EntitatiFactory.EntityType.Producer,
                    consumerInputs.get(0), distributorsInputs.get(0), producerInputs.get(i));
            producers.add((Producer) entitate);
        }

            /**
             *  Se executa runda initiala
             */
            for (Distributor distributor : distributors) {
                distributor.chooseProducer(producers);
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
            List<MonthlyUpdatesInput> updates = new ArrayList<>(inputParser.getMonthlyUpdates());
            int month = 0;
            for (MonthlyUpdatesInput updatesInput : updates) {
                month++;
                // se executa update-urile pentru costuri
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
                // consumatorii isi primesc venitul lunar si platesc rata daca nu sunt deja falimentati
                for (Consumer consumer : consumers) {
                    if (!consumer.isBankrupt()) {
                        if (!consumer.getRestant()) {
                            if (consumer.changeBuget(consumer.getInitialBudget()) < 0) {
                                consumer.notEnoughMoney(consumer, distributors);
                            } else {
                                int changeBuget = consumer.changeBuget(consumer.getInitialBudget());
                                consumer.setInitialBudget(changeBuget);
                                consumer.setContractLength(consumer.getContractLength() - 1);
                            }

                        } else {
                            if (consumer.getPenalise() != 0) {
                                if (consumer.penalizare2() < 0) {
                                    consumer.faliment(consumer, distributors);
                                } else {
                                    consumer.setInitialBudget(consumer.penalizare2());
                                    consumer.setContractLength(consumer.getContractLength() - 1);

                                }
                            } else if (consumer.penalizare(consumer.getInitialBudget()) < 0) {
                                consumer.faliment(consumer, distributors);
                            } else {
                                int changedBuget = consumer.penalizare(consumer.getInitialBudget());
                                consumer.setInitialBudget(changedBuget);
                                consumer.setContractLength(consumer.getContractLength() - 1);
                            }
                        }
                    }
                }
                // distribuitorii isi platesc cheltuielile
                for (Distributor distributor : distributors) {
                    distributor.setNoContracts(distributor.getContracts().size());
                    if (!distributor.isBankrupt()) {
                        int monthlyCosts = distributor.costTotal(distributor.getInitialBudget());
                        distributor.setInitialBudget(monthlyCosts);
                    }
                    // daca dupa platirea costurilor distribuitorul nu mai are bani acesta
                    //da faliment, iar contractele consumatorilor vor fi eliminate
                    if (distributor.getInitialBudget() < 0 && !distributor.isBankrupt()) {
                        distributor.setBankrupt(true);
                        distributor.removeContract();
                        distributor.removeConsumers(consumers);
                    }
                }

                for (Producer producer : producers){
                    producer.getMonthlyStats().add(new MonthlyStats(month, producer.getDistributorsIds()));
                }
            }
            for (Producer producer : producers){
                energyProducers.add(new EnergyProducer(producer.getId(),
                        producer.getMaxDistributors(), producer.getPriceKW(),
                        producer.getEnergyType(), producer.getEnergyPerDistributor(), producer.getMonthlyStats()));
            }
            // se creaza clasele de output
            for (Consumer consumer : consumers) {
                consumersOut.add(new Consumatori(consumer.getId(),
                        consumer.isBankrupt(), consumer.getInitialBudget()));
            }
            for (Distributor distributor : distributors) {
                for (Contract contract : distributor.getContracts()) {
                    // se adauga in contracte lunile de plata ramase
                    int remainedMonths = consumers.get(contract.getConsumerId()).getContractLength();
                    contract.setRemainedContractMonths(remainedMonths);
                }
                distributorsOut.add(new Distributori(distributor.getId(), distributor.getEnergyNeededKW(),
                        distributor.contractPrice(),
                        distributor.getInitialBudget(), distributor.getProducerStrategy(),
                        distributor.isBankrupt(),
                        distributor.getContracts()));
            }
          Output output = Output.getInstance();
            output.init(consumersOut, distributorsOut, energyProducers);
           objectMapper.writeValue(new File(args[1]), output);
        //System.out.println(consumersOut);
      // System.out.println(distributorsOut);
       //System.out.println(energyProducers);


        }
    }
