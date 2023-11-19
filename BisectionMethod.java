import java.util.function.Function;
import java.text.DecimalFormat;

public class BisectionMethod {

    public static void main(String[] args) {
        // Define the function for which you want to find the root
        Function<Double, Double> function = x -> 2*(x*x*x) - 11.7*(x*x) + 17.7*x - 5;

        // Specify the interval [a, b] where the root is expected
        double a = 1.0;         // intervals for all 3 roots: (0,1) , (1,3) , (3,4)
        double b = 3.0;

        // Set the tolerance level for convergence
        double tolerance = 0.01;

        // Find the root using the bisection method
        double root = bisectionMethod(function, a, b, 100, tolerance);

        // Print the root
        System.out.println("Root: " + root);
    }

    // Bisection method implementation
    private static double bisectionMethod(Function<Double, Double> function, double a, double b, int maxIter,double error) {
        double fa = function.apply(a);
        double fb = function.apply(b);

        if (fa*fb > 0) {      // signs =
            System.out.println("Function has same signs at a and b");
            return Double.NaN;
        }

        double currentError = b - a;

        System.out.println("n \t c \t\t f(c) \t\t error");
        System.out.println("------------------------------------------------");
        for (int i = 0; i < maxIter; i++) {
            currentError = currentError/2;
            double c = a + currentError;
            double fc = function.apply(c);
            printLine(i, c, fc, currentError);

            if (Math.abs(currentError) < error) {
                System.out.println("convergence");
                return c;   // return estimated root
            }
            if (fa*fc < 0) {        // signs != 
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

    private static void printLine(int n, double c, double fc, double error) {
        DecimalFormat decimalFormat = new DecimalFormat("#.####");

        String formattedC = decimalFormat.format(c);
        String formattedFc = decimalFormat.format(fc);
        String formattedErr = decimalFormat.format(error);

        System.out.println(n + "\t" + formattedC + "\t\t" + formattedFc + "\t\t" + formattedErr);
        // can also add a + b to table if want
    }
}
