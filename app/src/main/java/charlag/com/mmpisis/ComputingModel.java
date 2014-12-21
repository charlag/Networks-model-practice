package charlag.com.mmpisis;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Класс, предназначенный для вычисления потерь в СМО M/M/V/K/N
 * Пользователя класса скорее всего будет интересовать только метод compute().
 */
public class ComputingModel {
    double mA;
    long mV;
    long mN;
    BigDecimal mFragction;

    ComputingModel(double a, long v, long n) {
        mA = a;
        mV = v;
        mN = n;
        mFragction = new BigDecimal(a / (1 - a));
    }


    BigDecimal denominator = null;

    /**
     * Метод, вычисляющий вероятность потерь по вызовам.
     */
    public double computeOneValue(long i) {
        BigDecimal numerator = combination(mN, i).multiply(pow(mFragction, i));
        if (denominator == null) {
            denominator = BigDecimal.ZERO;
            for (int x = 0; x <= mV; x++) {
                denominator = denominator.add(combination(mN, x)).multiply(pow(mFragction, mV));
            }
        }
        return numerator.divide(denominator, 20, RoundingMode.HALF_UP).doubleValue();
    }

    /**
     * Метод, вычисляющий вероятность потерь по вызовам.
     *
     * @return массив вероятностей для каждого состояния
     */
    public Number[] compute() {
        int arrSize = (int) mV + 1;
        Number[] values = new Number[arrSize];
        for (int i = 0; i <= mV; i++) {
            values[i] = computeOneValue(i);
        }
        return values;
    }

    /**
     * Метод, вычисляющий потери по времени
     */
    public double computePt() {
        BigDecimal numerator = combination(mN, mV).multiply(pow(mFragction, mV));
        BigDecimal denominator = BigDecimal.ZERO;
        for (int x = 0; x <= mV; x++) {
            denominator = denominator.add(combination(mN, x).multiply(pow(mFragction, x)));
        }
        return numerator.divide(denominator, 20, RoundingMode.HALF_UP).doubleValue();
    }

    public static BigDecimal factorial(long n) {
        BigDecimal fact = BigDecimal.ONE;
        BigDecimal x = new BigDecimal(n);
        while (!x.equals(BigDecimal.ZERO)) {
            fact = fact.multiply(x);
            x = x.subtract(BigDecimal.ONE);
        }
        return fact;
    }

    private static BigDecimal combination(long n, long k) {
        BigDecimal numerator = factorial(n);
        BigDecimal denominator = factorial(k).multiply(factorial(n - k));
        return numerator.divide(denominator, 20, RoundingMode.HALF_UP);
    }


    private static BigDecimal pow(BigDecimal num, long pow) {
        BigDecimal n = new BigDecimal(num.toPlainString());
        for (int i = 0; i < pow; i++) {
            n = n.multiply(n);
        }
        return n;
    }

}