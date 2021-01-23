package strategies;

import data.Producer;
import entities.EnergyType;

import java.util.Comparator;

public final class StrategySortingComparator implements Comparator<Producer> {
    @Override
    public int compare(Producer o1, Producer o2) {
        if (o1.getEnergyType().equals(EnergyType.WIND)
                || o1.getEnergyType().equals(EnergyType.HYDRO)
                      || o1.getEnergyType().equals(EnergyType.SOLAR)) {
            return -1;
        }
        return 1;
    }
}
