package data;

import entities.EnergyType;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class GreenStrategy implements ProducerStrategy{
    public GreenStrategy() {
    }

    @Override
    public Producer chooseProducer(List<Producer> producers) {
        Comparator<Producer> producerComparator = Comparator.comparing(Producer::getPriceKW)
                .thenComparing(Producer::getEnergyPerDistributor);

        List<Producer> sortedList = producers.stream().sorted(producerComparator)
                .collect(Collectors.toList());

        for (Producer producer : sortedList){
            if (producer.getEnergyType().equals(EnergyType.SOLAR) ||
                producer.getEnergyType().equals(EnergyType.HYDRO) ||
                producer.getEnergyType().equals(EnergyType.WIND)){
                return producer;
            }
        }
        return sortedList.get(0);
    }
}