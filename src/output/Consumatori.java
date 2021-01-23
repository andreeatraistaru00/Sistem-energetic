package output;

import entities.Entitati;

public final class Consumatori extends Entitati {

    private int id;
    private boolean isBankrupt;
    private int budget;

    public Consumatori(final int id, final boolean isBankrupt, final int budget) {
        this.id = id;
        this.isBankrupt = isBankrupt;
        this.budget = budget;
    }

    public int getId() {
        return id;
    }

    public void setId(final int id) {
        this.id = id;
    }

    public int getBudget() {
        return budget;
    }

    public void setBudget(final int budget) {
        this.budget = budget;
    }

    /**
     *
     * @return
     */
    public boolean getisBankrupt() {
        return isBankrupt;
    }

    /**
     *
     * @param bankrupt
     */
    public void setisBankrupt(final boolean bankrupt) {
        isBankrupt = bankrupt;
    }

    @Override
    public String toString() {
        return "Consumatori{"
                + "id=" + id
                + ", isBankrupt=" + isBankrupt
                + ", budget=" + budget
                + '}';
    }
}
