package output;

import entities.EnergyType;

import java.util.ArrayList;
import java.util.List;

public final class EnergyProducer {
    private int id;
    private int maxDistributors;
    private double priceKW;
    private EnergyType energyType;
    private int energyPerDistributor;
    private List<MonthlyStats> monthlyStats;

    public EnergyProducer(int id, int maxDistributors, double priceKW, EnergyType energyType, int energyPerDistributor, List<MonthlyStats> monthlyStats) {
        this.id = id;
        this.maxDistributors = maxDistributors;
        this.priceKW = priceKW;
        this.energyType = energyType;
        this.energyPerDistributor = energyPerDistributor;
        this.monthlyStats = monthlyStats;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public void setPriceKW(int priceKW) {
        this.priceKW = priceKW;
    }

    public EnergyType getEnergyType() {
        return energyType;
    }

    public void setEnergyType(EnergyType energyType) {
        this.energyType = energyType;
    }

    public int getEnergyPerDistributor() {
        return energyPerDistributor;
    }

    public void setEnergyPerDistributor(int energyPerDistributor) {
        this.energyPerDistributor = energyPerDistributor;
    }

    public List<MonthlyStats> getMonthlyStats() {
        return monthlyStats;
    }

    public void setMonthlyStats(List<MonthlyStats> monthlyStats) {
        this.monthlyStats = monthlyStats;
    }

    @Override
    public String toString() {
        return "EnergyProducer{" +
                "id=" + id +
                ", maxDistributors=" + maxDistributors +
                ", priceKW=" + priceKW +
                ", energyType=" + energyType +
                ", energyPerDistributor=" + energyPerDistributor +
                ", monthlyStats=" + monthlyStats +
                '}';
    }
}
