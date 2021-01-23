package data;


import entities.Entitati;
import output.Contract;
import strategies.EnergyChoiceStrategyType;
import strategies.GreenStrategy;
import strategies.PriceStrategy;
import strategies.ProducerStrategy;
import strategies.QuantityStrategy;

import java.util.ArrayList;
import java.util.List;


public final class Distributor extends Entitati {
    /**
     * id-ul unui distribuitor
     */
    private int id;
    /**
     * lungimea contractului
     */
    private int contractLength;
    /**
     * bugetul initial al unui distribuitor
     */
    private int initialBudget;
    /**
     * costul initial al infrastructurii
     */
    private int initialInfrastructureCost;
    /**
     * cantitatea de energie necesara
     */
    private int energyNeededKW;
    /**
     * falimentat sau nu
     */
    private boolean isBankrupt = false;
    /**
     * Strategia
     */
    private EnergyChoiceStrategyType producerStrategy;
    /**
     * numarul de contracte existente
     */
    private int noContracts = 0;
    /**
     * lista constractelor
     */
    private List<Contract> contracts = new ArrayList<>();
    /**
     * costul de productie
     */
    private int productionCost;
    private int contractCost;
    private List<Producer> producerList = new ArrayList<>();
    private boolean flagUpdate = false;
    private static final int CONSTANT = 10;

    public boolean isFlagUpdate() {
        return flagUpdate;
    }

    public void setFlagUpdate(boolean flagUpdate) {
        this.flagUpdate = flagUpdate;
    }

    public int getContractCost() {
        return contractCost;
    }

    public void setContractCost(int contractCost) {
        this.contractCost = contractCost;
    }

    public List<Producer> getProducerList() {
        return producerList;
    }

    public void setProducerList(List<Producer> producerList) {
        this.producerList = producerList;
    }

    private static final double PROCENT_PROFIT = 0.2;
    private static final int MAX_BUGET = 9999;

    public int getNoContracts() {
        return noContracts;
    }

    public void setNoContracts(final int noContracts) {
        this.noContracts = noContracts;
    }

    public boolean isBankrupt() {
        return isBankrupt;
    }

    public void setBankrupt(final boolean bankrupt) {
        isBankrupt = bankrupt;
    }

    public int getEnergyNeededKW() {
        return energyNeededKW;
    }

    public void setEnergyNeededKW(int energyNeededKW) {
        this.energyNeededKW = energyNeededKW;
    }

    public EnergyChoiceStrategyType getProducerStrategy() {
        return producerStrategy;
    }

    public int getProductionCost() {
        return productionCost;
    }

    public void setProducerStrategy(EnergyChoiceStrategyType producerStrategy) {
        this.producerStrategy = producerStrategy;
    }

    public List<Contract> getContracts() {
        return contracts;
    }

    /**
     *
     * @param producers
     * @param month
     * metoda alege un producator conform unei strategii
     */
    public void chooseProducer(List<Producer> producers, int month) {
        List<Producer> producers1 = new ArrayList<>(producers);

        if (this.producerList.size() != 0) {
            producerList.stream().filter(producers1::contains).forEach(producers1::remove);
        }

        producers1.removeIf(producer ->
                producer.getDistributorsIds().get(month).size() >= producer.getMaxDistributors());

        if (this.getProducerStrategy().equals(EnergyChoiceStrategyType.GREEN)) {
            ProducerStrategy strategy = new GreenStrategy();
            Producer producer = strategy.chooseProducer(producers1);
            this.producerList.add(producer);
            producer.getDistributorsIds().get(month).add(id);

        } else if (this.getProducerStrategy().equals(EnergyChoiceStrategyType.PRICE)) {
            ProducerStrategy strategy = new PriceStrategy();
            Producer producer = strategy.chooseProducer(producers1);
            this.producerList.add(producer);
            producer.getDistributorsIds().get(month).add(id);
        } else {
            ProducerStrategy strategy = new QuantityStrategy();
            Producer producer = strategy.chooseProducer(producers1);
            this.producerList.add(producer);
            producer.getDistributorsIds().get(month).add(id);
        }
    }

    /**
     * distribuitorul este sters de la producatorii actuali
     * si apoi se se face update la lista de producatori
     * @param producers
     * @param month
     */
    public void update(List<Producer> producers, int month) {
        for (Producer producer : producers)  {
            if (producerList.contains(producer)) {
                int index = producer.getDistributorsIds().get(month).indexOf(id);
                producer.getDistributorsIds().get(month).remove(index);
            }
        }
        this.producerList.clear();

        while (!getEnergy()) {
            chooseProducer(producers, month);
        }
        setProductionCost();
        setFlagUpdate(false);
    }

    /**
     * verifica daca distribuitorul are cantitatea de energie lunara necesara
     * @return
     */
    public boolean getEnergy() {
        int sum = 0;
        for (Producer producer : producerList) {
            sum += producer.getEnergyPerDistributor();
        }
        return sum >= this.energyNeededKW;
    }

    /**
     * metoda calculeaza costul de productie
     */
    public void setProductionCost() {
        int cost = 0;
        for (Producer producer : this.producerList) {
            cost += producer.getEnergyPerDistributor() * producer.getPriceKW();
        }
        int production = (int) Math.round(Math.floor(cost / CONSTANT));
        this.productionCost = production;
    }
    /**
     * metoda adauga un contract nou in lista de contracte
     * daca nu exista deja un contract cu respectivul consumator
     * @param consumers
     */
    public void addContract(final Consumer consumers) {
        int flag = 0;
        for (Contract contract : contracts) {
            if (contract.getConsumerId() == consumers.getId()) {
                flag = 1;
            }
        }
        if (flag == 0) {
            this.contracts.add(new Contract(consumers.getId(),
                    consumers.getRata(), consumers.getContractLength()));
        }
    }

    /**
     *
     * @param contracts
     */
    public void setContracts(final List<Contract> contracts) {
        this.contracts = contracts;
    }

    /**
     * metoda returneaza indexul distribuitorului cu rata minima
     * dintr- o  lista de distribuitori data ca parametru
     * @param distributors
     */
    public int minRata(final List<Distributor> distributors) {
        int index = 0;
        int min = MAX_BUGET;
        for (Distributor distributor : distributors) {
                if (distributor.contractPrice() < min && !distributor.isBankrupt) {
                    min = distributor.contractPrice();
                    index = distributors.indexOf(distributor);
                }

        }
        return index;
    }

    /**
     * metoda seteaza la 0 lungimea contractelor consumatorilor
     * cu care distribuitorul are contracte
     * @param consumers
     */
    public void removeConsumers(final List<Consumer> consumers) {
        for (Consumer consumer : consumers) {
            for (Contract contract : this.contracts) {
                if (consumer.getId() == contract.getConsumerId()) {
                    consumer.setContractLength(0);
                }
            }
        }
    }

    public int getId() {
        return id;
    }

    public void setId(final int id) {
        this.id = id;
    }

    public int getContractLength() {
        return contractLength;
    }

    public void setContractLength(final int contractLength) {
        this.contractLength = contractLength;
    }

    public int getInitialBudget() {
        return initialBudget;
    }

    public void setInitialBudget(final int initialBudget) {
        this.initialBudget = initialBudget;
    }

    public int getInitialInfrastructureCost() {
        return initialInfrastructureCost;
    }

    public void setInitialInfrastructureCost(final int initialInfrastructureCost) {
        this.initialInfrastructureCost = initialInfrastructureCost;
    }

    public Distributor(int id, int contractLength, int initialBudget, int initialInfrastructureCost,
                       int energyNeededKW, EnergyChoiceStrategyType producerStrategy) {
        this.id = id;
        this.contractLength = contractLength;
        this.initialBudget = initialBudget;
        this.initialInfrastructureCost = initialInfrastructureCost;
        this.energyNeededKW = energyNeededKW;
        this.producerStrategy = producerStrategy;
    }

    /**
     * metoda returneaza pretul unui contract
     * @return
     */
    public int contractPrice() {
        int price;
        int profit = (int) Math.round(Math.floor(PROCENT_PROFIT * productionCost));
        if (noContracts > 0) {
            price = (int) Math.round(Math.floor(initialInfrastructureCost / noContracts)
                    + productionCost + profit);
        } else {
            price = (int) Math.round(initialInfrastructureCost + productionCost + profit);
        }
        this.contractCost = price;
        return price;
    }

    /**
     * metoda returneaza costul total pe care un distribuitor
     * trebuie sa-l plateasca la sfarsitul lunii
     * @param budget
     * @return
     */
    public int costTotal(final int budget) {
        int cost;
        int sold = 0;
        cost = this.initialInfrastructureCost + productionCost * this.contracts.size();
        sold = budget - cost;
        for (Contract contract : this.contracts) {
            sold = sold + contract.getPrice();
        }
        return sold;
    }

    /**
     * metoda sterge din lista de contracte pe acela
     * al consumatorului al carui id este dat ca parametru
     */
    public List<Contract> delContract(final int idConsumer) {
        this.contracts.removeIf(contract -> contract.getConsumerId() == idConsumer);
        return this.contracts;
    }

    /**
     * metoda sterge toate contractele existente
     */
    public void removeContract() {
        this.contracts.removeAll(contracts);
    }

    @Override
    public String toString() {
        return "Distributors{"
                + "id='" + id + '\''
                + ",contractLength='"
                + contractLength + '\''
                + ", initialBudget='" + initialBudget + '\''
                + ",initialInfrastructureCost='" + initialInfrastructureCost + '\''
                + ",energyNeededKW=" + energyNeededKW + '\''
                + ",producerStrategy=" + producerStrategy + '}';
    }
}
