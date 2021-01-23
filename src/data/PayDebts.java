package data;

import java.util.List;

public final class PayDebts {
    private List<Consumer> consumers;
    private List<Distributor> distributors;

    public PayDebts(List<Consumer> consumers, List<Distributor> distributors) {
        this.consumers = consumers;
        this.distributors = distributors;
    }

    /**
     * consumatorii si distribuitorii isi platesc cheltuielile lunare
     */
    public void pay() {
        for (Consumer consumer : consumers) {
            if (!consumer.isBankrupt()) {
                if (!consumer.getRestant()) {
                    if (consumer.changeBuget(consumer.getInitialBudget()) < 0) {
                        consumer.notEnoughMoney(consumer, distributors);
                    } else {
                        int changeBuget = consumer.changeBuget(consumer.getInitialBudget());
                        consumer.setInitialBudget(changeBuget);
                        consumer.setContractLength(consumer.getContractLength() - 1);
                    }

                } else {
                    if (consumer.getPenalise() != 0) {
                        if (consumer.penalizare2() < 0) {
                            consumer.faliment(consumer, distributors);
                        } else {
                            consumer.setInitialBudget(consumer.penalizare2());
                            consumer.setContractLength(consumer.getContractLength() - 1);

                        }
                    } else if (consumer.penalizare(consumer.getInitialBudget()) < 0) {
                        consumer.faliment(consumer, distributors);
                    } else {
                        int changedBuget = consumer.penalizare(consumer.getInitialBudget());
                        consumer.setInitialBudget(changedBuget);
                        consumer.setContractLength(consumer.getContractLength() - 1);
                    }
                }
            }
        }

        // distribuitorii isi platesc cheltuielile
        for (Distributor distributor : distributors) {
            distributor.setNoContracts(distributor.getContracts().size());
            if (!distributor.isBankrupt()) {
                int monthlyCosts = distributor.costTotal(distributor.getInitialBudget());
                distributor.setInitialBudget(monthlyCosts);
            }
            // daca dupa platirea costurilor distribuitorul nu mai are bani acesta
            //da faliment, iar contractele consumatorilor vor fi eliminate
            if (distributor.getInitialBudget() < 0 && !distributor.isBankrupt()) {
                distributor.setBankrupt(true);
                distributor.removeContract();
                distributor.removeConsumers(consumers);
            }
        }

    }
}
