import java.util.Random;

public class RandomNumberGenerator {

    static Random random = new Random(System.currentTimeMillis());
    public static double generateRandomNumber(double mean, double std) {

        double u1 = random.nextDouble();
        double u2 = random.nextDouble();
        double randStdNormal = Math.sqrt(-2.0 * Math.log(u1)) * Math.cos(2 * Math.PI * u2);
        return Math.max(0, mean + std * randStdNormal);
    }

}