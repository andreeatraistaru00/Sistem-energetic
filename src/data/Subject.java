package data;


import input.ProducerChanges;
import java.util.List;

public final class Subject {
    private List<Distributor> distributors;

    public Subject(List<Distributor> distributors) {
        this.distributors = distributors;
    }

    /**
     * metoda notifica distribuitorii ai caror
     * producatori si-au actualizat valorile
     * @param producerChangesList
     */
    public void notifyDistributors(List<ProducerChanges> producerChangesList) {
        for (ProducerChanges producerChanges : producerChangesList) {
            for (Distributor distributor : distributors) {
                if (distributor.getProducerList().
                        stream().anyMatch(producer ->
                        producerChanges.getId() == producer.getId())) {
                    distributor.setFlagUpdate(true);
                }
            }
        }
    }
}
