package data;


import input.ProducerChanges;

import java.util.ArrayList;
import java.util.List;

public class Subject {
    List<Distributor> distributors;

    public Subject(List<Distributor> distributors) {
        this.distributors = distributors;
    }

    public void notifyDistributors(List<ProducerChanges> producerChangesList, int month, List<Producer>producers){
        for (ProducerChanges producerChanges : producerChangesList){
            for (Distributor distributor : distributors){
                if (distributor.getProducerList().stream().anyMatch(producer -> producerChanges.getId() == producer.getId())){
                    distributor.update(producers, month);
                }
            }
        }
    }
}
