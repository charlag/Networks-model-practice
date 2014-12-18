package charlag.com.mmpisis;

public class QueringModel {
    private double mA;
    private long mV;
    private long mN;

    QueringModel(double a, long v, long n) {
        mA = a;
        mV = v;
        mN = n;
    }

    public double computeOneValue(long i) {
        double numerator = combination(mN, i) * Math.pow(mA / (1 - mA), i);
        double denominator = 0;
        for (int x = 0; x <= mV; x++) {
            denominator += combination(mN, x) * Math.pow(mA / (1 - mA), x);
        }
        return numerator / denominator;
    }

    public Number[] compute() {
        int v = ((int) mV);
        Number[] values = new Number[v];
        for (int i = 0; i < mV; i++) {
            values[i] = computeOneValue(i);
        }
        return values;
    }

    public double computePt() {
        double numerator = combination(mN, mV) * Math.pow(mA/(1 - mA), mV);
        double denominator = 0;
        for (int x = 0; x <= mV; x++) {
            denominator += combination(mN, x) * Math.pow(mA/(1 - mA), x);
        }
        return numerator / denominator;
    }


    private static double combination(long n, long k) {
        long numerator = factorial(n);
        long denominator = factorial(k) * factorial(n - k);
        return ((double) numerator) / denominator;
    }


    public static long factorial(long n) {
        long fact = 1;
        for (long i = 1; i <= n; i++) {
            fact *= i;
        }
        return fact;
    }
}
