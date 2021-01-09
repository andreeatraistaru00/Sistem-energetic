package data;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class QuantityStrategy implements ProducerStrategy {
    @Override
    public Producer chooseProducer(List<Producer> producers) {
        Comparator<Producer> producerComparator = Comparator.comparing(Producer::getEnergyPerDistributor)
                                                    .thenComparing(Producer::getId);

        List<Producer> sortedList = producers.stream().sorted(producerComparator)
                .collect(Collectors.toList());
        return sortedList.get(0);
    }
}
