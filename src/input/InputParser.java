package input;

import java.util.ArrayList;
import java.util.List;

public final class InputParser {

    private int numberOfTurns;

    public int getNumberOfTurns() {
        return numberOfTurns;
    }

    public void setNumberOfTurns(final int numberOfTurns) {
        this.numberOfTurns = numberOfTurns;
    }

    private InitialDataInput initialData;

    private List<MonthlyUpdatesInput> monthlyUpdates = new ArrayList<>();

    public InputParser() {
        super();
    }

    public InputParser(final int numberOfTurns, final InitialDataInput initialData,
                       final List<MonthlyUpdatesInput> monthlyUpdates) {
        this.numberOfTurns = numberOfTurns;
        this.initialData = initialData;
        this.monthlyUpdates = monthlyUpdates;

    }

    public List<MonthlyUpdatesInput> getMonthlyUpdates() {
        return monthlyUpdates;
    }

    public void setMonthlyUpdates(final List<MonthlyUpdatesInput> monthlyUpdates) {
        this.monthlyUpdates = monthlyUpdates;
    }

    public InitialDataInput getInitialData() {
        return initialData;
    }

    public void setInitialData(final InitialDataInput initialData) {
        this.initialData = initialData;
    }
}
