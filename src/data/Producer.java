package data;

import entities.EnergyType;
import entities.Entitati;
import output.MonthlyStats;

import java.util.ArrayList;
import java.util.List;

public final class Producer extends Entitati {
    private int id;
    private EnergyType energyType;
    private int maxDistributors;
    private double priceKW;
    private int energyPerDistributor;
    private List<List<Integer>> distributorsIds = new ArrayList<>();
    private List<MonthlyStats> monthlyStats = new ArrayList<>();

    public Producer(int id, EnergyType energyType, int maxDistributors,
                         double priceKW, int energyPerDistributor) {
        this.id = id;
        this.energyType = energyType;
        this.maxDistributors = maxDistributors;
        this.priceKW = priceKW;
        this.energyPerDistributor = energyPerDistributor;
    }

    public List<MonthlyStats> getMonthlyStats() {
        return monthlyStats;
    }

    public void setMonthlyStats(List<MonthlyStats> monthlyStats) {
        this.monthlyStats = monthlyStats;
    }

    public List<List<Integer>> getDistributorsIds() {
        return distributorsIds;
    }

    public void setDistributorsIds(List<List<Integer>> distributorsIds) {
        this.distributorsIds = distributorsIds;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public EnergyType getEnergyType() {
        return energyType;
    }

    public void setEnergyType(EnergyType energyType) {
        this.energyType = energyType;
    }

    public int getMaxDistributors() {
        return maxDistributors;
    }

    public void setMaxDistributors(int maxDistributors) {
        this.maxDistributors = maxDistributors;
    }

    public double getPriceKW() {
        return priceKW;
    }

    public void setPriceKW(double priceKW) {
        this.priceKW = priceKW;
    }

    public int getEnergyPerDistributor() {
        return energyPerDistributor;
    }

    public void setEnergyPerDistributor(int energyPerDistributor) {
        this.energyPerDistributor = energyPerDistributor;
    }

    /**
     * metoda creaza o lista cu id-urile distribuitorilor
     * pentru fiecare luna
     * @param month
     */
    public void addDistributorsIds(int month) {
        for (int i = 0; i <= month; i++) {
            distributorsIds.add(new ArrayList<>());
        }
    }

    @Override
    public String toString() {
        return "ProducerInput{"
                + "id=" + id
                + ", energyType='" + energyType + '\''
                + ", maxDistributors=" + maxDistributors
                + ", priceKW=" + priceKW
                + ", energyPerDistributor=" + energyPerDistributor
                + '}';
    }
}
