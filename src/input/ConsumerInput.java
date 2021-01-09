package input;

public final class ConsumerInput {

        private  int id;
        private  int initialBudget;
        private int  monthlyIncome;

        public ConsumerInput() {
            super();
        }

        public ConsumerInput(final int id, final int initialBudget, final int monthlyIncome) {
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

        @Override
        public String toString() {
            return "Consumers{"
                    + "id='" + id + '\''
                    + ", initialBudget='"
                    + initialBudget + '\''
                    + ", monthlyIncome=" + monthlyIncome + '}';
        }
    }



