package output;

import entities.Entitati;
import strategies.EnergyChoiceStrategyType;

import java.util.ArrayList;
import java.util.List;


public final class Distributori extends Entitati {
    private int id;
    private int energyNeededKW;
    private int contractCost;
    private int budget;
    private EnergyChoiceStrategyType producerStrategy;
    private boolean isBankrupt;
    private List<Contract> contracts = new ArrayList<>();

    public Distributori(int id,
                        int energyNeededKW,
                        int contractCost,
                        int budget,
                        EnergyChoiceStrategyType producerStrategy,
                        boolean isBankrupt,
                        List<Contract> contracts) {
        this.id = id;
        this.energyNeededKW = energyNeededKW;
        this.contractCost = contractCost;
        this.budget = budget;
        this.producerStrategy = producerStrategy;
        this.isBankrupt = isBankrupt;
        this.contracts = contracts;
    }

    public int getEnergyNeededKW() {
        return energyNeededKW;
    }

    public void setEnergyNeededKW(int energyNeededKW) {
        this.energyNeededKW = energyNeededKW;
    }

    public int getContractCost() {
        return contractCost;
    }

    public void setContractCost(int contractCost) {
        this.contractCost = contractCost;
    }

    public EnergyChoiceStrategyType getProducerStrategy() {
        return producerStrategy;
    }

    public void setProducerStrategy(EnergyChoiceStrategyType producerStrategy) {
        this.producerStrategy = producerStrategy;
    }

    public int getId() {
        return id;
    }

    public void setId(final int id) {
        this.id = id;
    }

    public int getBudget() {
        return budget;
    }

    public void setBudget(final int budget) {
        this.budget = budget;
    }

    /**
     *
     * @return
     */
    public boolean getisBankrupt() {
        return isBankrupt;
    }

    /**
     * @param bankrupt
     */
    public void setisBankrupt(final boolean bankrupt) {
        isBankrupt = bankrupt;
    }

    public List<Contract> getContracts() {
        return contracts;
    }

    public void setContracts(final List<Contract> contracts) {
        this.contracts = contracts;
    }

    @Override
    public String toString() {
        return "Distributori{"
                + "id=" + id
                + ", energyNeededKW=" + energyNeededKW
                + ", contractCost=" + contractCost
                + ", budget=" + budget
                + ", producerStrategy='" + producerStrategy + '\''
                + ", isBankrupt=" + isBankrupt
                + ", contracts=" + contracts
                + '}';
    }
}
