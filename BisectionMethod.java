import java.util.function.Function;
import java.text.DecimalFormat;

public class BisectionMethod {

    public static void main(String[] args) {
        // Define the function for which you want to find the root
        Function<Double, Double> function = x -> 2*(x*x*x) - 11.7*(x*x) + 17.7*x - 5;

        // Specify the interval [a, b] where the root is expected
        double a = 3.0;         // intervals for all 3 roots: (0,1) , (1,3) , (3,4)
        double b = 4.0;

        // Set the tolerance level for convergence
        double tolerance = 0.01;

        // Find the root using the bisection method
        double root = bisectionMethod(function, a, b, 100, tolerance);

        // Print the root
        System.out.println();
        System.out.println("Function converges at Root: " + root);
        System.out.println();
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

        System.out.println();
        System.out.println("Bisection Method from [" + a + ", " + b + "]");
        System.out.println("--------------------------------------------------------------------------");
        System.out.println("n \t c \t\t f(c) \t\t error \t\t    approx. err");
        System.out.println("--------------------------------------------------------------------------");
        double prevC = currentError/2 + a;

        for (int i = 0; i < maxIter; i++) {
            currentError = currentError/2;
            double c = a + currentError;
            double fc = function.apply(c);
            double relError;

            if (i==0) {
                relError = 1.0;
            } else {
                relError = Math.abs((c - prevC) / c);
            }

            printBisection(i, c, fc, currentError, relError);

            if (relError < error) {
                // Print the root
                System.out.println();
                System.out.println("Function converges at Root: " + c + " after " + (i+1) + " iterations.");
                System.out.println();
                return c;   // return estimated root
            }

            prevC = c;

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

    private static void printBisection(int n, double c, double fc, double error, double relError) {
        DecimalFormat decimalFormat = new DecimalFormat("#.######");
        DecimalFormat errorFormat = new DecimalFormat("#.########");

        String formattedN = String.format("%-9d", n);
        String formattedC = String.format("%-16s", decimalFormat.format(c));
        String formattedFc = String.format("%-16s", decimalFormat.format(fc));
        String formattedErr = String.format("%-20s", errorFormat.format(error));
        String formattedRelErr = String.format("%-20s", errorFormat.format(relError));
        
        System.out.println(formattedN + formattedC + formattedFc + formattedErr + formattedRelErr);
    }
}
