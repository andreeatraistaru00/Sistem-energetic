package data;

import output.Contract;

import java.util.List;

public final class Consumer extends Entitati {
    /**
     * id -ul unui consumator
     */
    private  int id;
    /**
     * bugetul initial al unui consumator
     */
    private  int initialBudget;
    /**
     * venitul lunar
     */
    private int  monthlyIncome;
    /**
     * rata lunara
     */
    private int rata;
    /**
     * lungimea contractului
     */
    private int contractLength;
    /**
     * retine daca un consumator nu a platit luna trecuta
     */
    private boolean restant = false;
    /**
     * id- ul distribuitorului la care are contract
     */
    private int idDistributor;
    /**
     * retine daca consumatorul este sau nu falimentat
     */
    private boolean bankrupt = false;
    /**
     * retine valoarea veche a ratei daca
     * consumatorul isi schimba distribuitorul si ramane cu
     * penalizare la un alt distribuitor
     */
    private int oldPenalise = 0;

    private static final double PROCENT_PROFIT = 0.2;
    private static final double PENALIZARE = 1.2;

    public int getPenalise() {
        return oldPenalise;
    }

    public void setPenalise(final int penalise) {
        this.oldPenalise = penalise;
    }

    public boolean isBankrupt() {
        return bankrupt;
    }

    public void setBankrupt(final boolean bankrupt) {
        this.bankrupt = bankrupt;
    }

    public int getIdDistributor() {
        return idDistributor;
    }

    public void setIdDistributor(final int idDistributor) {
        this.idDistributor = idDistributor;
    }

    public void setRestant(final boolean restant) {
        this.restant = restant;
    }

    public boolean getRestant() {
        return restant;
    }

    public Consumer() {
        super();
    }

    public int getRata() {
        return rata;
    }

    public void setRata(final int rata) {
        this.rata = rata;
    }

    public int getContractLength() {
        return contractLength;
    }

    public void setContractLength(final int contractLength) {
        this.contractLength = contractLength;
    }

    public Consumer(final int id, final int initialBudget, final int monthlyIncome) {
        this.id = id;
        this.initialBudget = initialBudget;
        this.monthlyIncome = monthlyIncome;
    }

    public int getId() {
        return id;
    }

    public void setId(final int id) {
        this.id = id;
    }

    public int getInitialBudget() {
        return initialBudget;
    }

    public void setInitialBudget(final int initialBudget) {
        this.initialBudget = initialBudget;
    }

    public int getMonthlyIncome() {
        return monthlyIncome;
    }

    public void setMonthlyIncome(final int monthlyIncome) {
        this.monthlyIncome = monthlyIncome;
    }

    /**
     * metoda gaseste distribuitor si seteaza rata
     * lungimea contractului si id- ul distribuitorului
     * @param distributors
     */
    public void findDistributor(final List<Distributor> distributors) {

        int indexMin = distributors.get(0).minRata(distributors);
        this.rata = distributors.get(indexMin).contractPrice();
        this.contractLength = distributors.get(indexMin).getContractLength();
        this.idDistributor = distributors.get(indexMin).getId();
    }

    /**
     * metoda returneaza suma cu care ramane consumatorul dupa plata
     * penalizarii
     */
    public int penalizare(final int budget) {
        int sold = budget - (int) (Math.round(Math.floor(PROCENT_PROFIT * rata)) + rata);
        return sold;
    }

    /**
     * metoda returneaza suma cu care ramane consumatorul dupa plata
     * penalizarii daca acesta a schimbat distribuitorul
     * @return
     */
    public int penalizare2() {
        int res = this.initialBudget
                - (int) (Math.round(Math.floor(PENALIZARE * this.oldPenalise)) + rata);
        return res;
    }

    /**
     * daca consumatorul nu poate plati rata acesta ramane restant, si i se adauga venitul lunar
     * distribuitorului i se scade rata pe care ar fi primit o de la consumator
     * daca contractului consumatorului se termina se retine rata pentru ca
     * in urmatoarea luna aceasta va fi actualizata si el va trebui sa ii platesca si
     * vechiului distribuitor
     * @param consumer
     * @param distributors
     */
    public void notEnoughMoney(final Consumer consumer, final List<Distributor> distributors) {
        consumer.setRestant(true);
        int aux;
        aux = distributors.get(consumer.getIdDistributor()).getInitialBudget() - consumer.getRata();
        distributors.get(consumer.getIdDistributor()).setInitialBudget(aux);

        consumer.setInitialBudget(consumer.getInitialBudget() + consumer.getMonthlyIncome());
        consumer.setContractLength(consumer.getContractLength() - 1);

        if (consumer.getContractLength() == 0) {
            consumer.setPenalise(consumer.getRata());
        }
    }

    /**
     * metoda sterge contractul consumatorului care a dat faliment
     * din lista de contracte a distribuitorului sau
     * distribuitorul plateste costul de productie pentru consumator
     * @param consumer
     * @param distributors
     */
    public void faliment(final Consumer consumer, final List<Distributor> distributors) {
        consumer.setInitialBudget(consumer.getInitialBudget() + consumer.getMonthlyIncome());
        List<Contract> contracts;
        contracts = distributors.get(consumer.getIdDistributor()).delContract(consumer.getId());
        distributors.get(consumer.getIdDistributor()).setContracts(contracts);

        int aux = distributors.get(consumer.getIdDistributor()).getInitialBudget();
        int aux1 = distributors.get(consumer.getIdDistributor()).getProductionCost();
        distributors.get(consumer.getIdDistributor()).setInitialBudget(aux - aux1);
        consumer.setBankrupt(true);
    }

    /**
     * metoda returneaza bugetul cu care ramane consumatorul la
     * finalul lunii dupa ce a primit salariul si platit rata
     * @param budget
     * @return
     */
    public int changeBuget(final int budget) {

        int sold = budget + monthlyIncome - rata;

        return sold;
    }

    @Override
    public String toString() {
        return "Consumers{"
                + "id='" + id + '\''
                + ", initialBudget='"
                + initialBudget + '\''
                + ", monthlyIncome=" + monthlyIncome + '}';
    }
}
