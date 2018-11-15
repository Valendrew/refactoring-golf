package payroll;

public interface TaxBand {
    final static TaxBand NIL_TAX_BAND = new TaxBand() {
        @Override public boolean appliesTo(long grossSalary) { return false; }
        @Override public long taxFor(long grossSalary) { return 0; }
        @Override public long maximumTaxSoFar() { return 0; }
    };

    boolean appliesTo(long grossSalary);
    long taxFor(long grossSalary);
    long maximumTaxSoFar();

    public class ChainedTaxBand implements TaxBand {
        private final double rate;
        private final int threshold;
        private final long maximumTax;
        private final TaxBand previousTaxBand;

        public ChainedTaxBand(double rate, int threshold, long maximumTax, TaxBand previousTaxBand) {
            this.rate = rate;
            this.threshold = threshold;
            this.maximumTax = maximumTax;
            this.previousTaxBand = previousTaxBand;
        }

        private long taxForBand(long grossSalary) {
            return Math.round((grossSalary - threshold) * rate);
        }

        @Override
        public boolean appliesTo(long grossSalary) {
            return grossSalary > threshold;
        }

        @Override
        public long taxFor(long grossSalary) {
            return previousTaxBand.maximumTaxSoFar() + taxForBand(grossSalary);
        }

        @Override
        public long maximumTaxSoFar() {
            return maximumTax + previousTaxBand.maximumTaxSoFar();
        }
    }
}
