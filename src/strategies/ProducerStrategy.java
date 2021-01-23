package strategies;

import data.Producer;

import java.util.List;

public interface ProducerStrategy {
    /**
     * metoda alege un producator conform unei strategii
     * @param producers
     * @return
     */
    Producer chooseProducer(List<Producer> producers);
}
