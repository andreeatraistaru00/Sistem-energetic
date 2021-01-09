package data;

import java.util.List;

public interface ProducerStrategy {
    Producer chooseProducer(List<Producer> producers);
}
