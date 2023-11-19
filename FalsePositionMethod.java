import java.util.function.Function;
import java.text.DecimalFormat;

public class FalsePositionMethod {

    public static void main(String[] args) {
        // Define the function for which you want to find the root
        Function<Double, Double> function = x -> 2*(x*x*x) - 11.7*(x*x) + 17.7*x - 5;

        // Specify the interval [a, b] where the root is expected
        double a = 3.0;         // intervals for all 3 roots: (0,1) , (1,3) , (3,4)
        double b = 4.0;

        // Set the tolerance level for convergence
        double tolerance = 0.01;

        double root = falsePositionMethod(function, a, b, 100, tolerance);

        // Print the root
        System.out.println("Root: " + root);
    }

    private static double falsePositionMethod(Function<Double, Double> function, double a, double b, int maxIter, double error) {
        double fa = function.apply(a);
        double fb = function.apply(b);

        if (fa * fb > 0) {      // signs =
            System.out.println("Function has same signs at a and b");
            return Double.NaN;
        }

        System.out.println("n \t c \t\t f(c)");
        System.out.println("---------------------------------");
        for (int i = 0; i < maxIter; i++) {
            double c = (a * fb - b * fa) / (fb - fa);
            double fc = function.apply(c);
            printLine(i, c, fc);

            if (Math.abs(fc) < error) {
                System.out.println("convergence");
                return c;   // return estimated root
            }
            if (fa * fc < 0) {
                b = c;
                fb = fc;
            } else {
                a = c;
                fa = fc;
            }
        }

        // Maximum iterations reached
        System.out.println("Maximum iterations reached without convergence.");
        return Double.NaN;      // or return c if you want the latest root at n=100
    }

    private static void printLine(int n, double c, double fc) {
        DecimalFormat decimalFormat = new DecimalFormat("#.####");

        String formattedC = decimalFormat.format(c);
        String formattedFc = decimalFormat.format(fc);

        System.out.println(n + "\t" + formattedC + "\t\t" + formattedFc);
        // can also add a + b to table if want
    }
}
