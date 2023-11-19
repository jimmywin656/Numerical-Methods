import java.util.function.Function;
import java.text.DecimalFormat;

public class NewtonRaphsonMethod {
    
    public static void main(String[] args) {
        // Define the function for which you want to find the root
        Function<Double, Double> function = x -> 2*(x*x*x) - 11.7*(x*x) + 17.7*x - 5;

        // Define the derivative of the function
        Function<Double, Double> derivative = x -> 6*(x*x) - 23.4*x + 17.7;

        // Initial guess for the root
        double initialGuess = 3.0;      // guesses for 3 roots: 1, 2, 3

        // Set the tolerance level for convergence
        double tolerance = 0.01;

        // Find the root using the Newton-Raphson method
        double root = newtonRaphsonMethod(function, derivative, initialGuess, tolerance);

        // Print the result
        System.out.println();
        System.out.println("Function converges at Root: " + root);
        System.out.println();
    }

    private static double newtonRaphsonMethod(Function<Double, Double> function, Function<Double, Double> derivative,
                                                double initialGuess, double tolerance) {
        double x0 = initialGuess;

        System.out.println();
        System.out.println("Newton Raphson Method");
        System.out.println("-----------------------------------------------");
        System.out.println("n \t x \t\t f(x)");
        System.out.println("-----------------------------------------------");
        for (int i = 0; i < 100; i++) {
            double fx = function.apply(x0);
            printLine(i, x0, fx);

            if (Math.abs(fx) < tolerance) {
                return x0;      // return estimated root
            }

            // not necessary for newton raphson method but just a check , can delete if you want
            double fPrimeX = derivative.apply(x0);

            x0 = x0 - fx / fPrimeX;
        }

        // Max iterations reached
        System.out.println("Maximum iterations reached without convergence.");
        return Double.NaN;      // or can return x0 if you want the latest estimated root
    }

    private static void printLine(int n, double x, double fx) {
        DecimalFormat decimalFormat = new DecimalFormat("#.####");

        String formattedX = decimalFormat.format(x);
        String formattedFx = decimalFormat.format(fx);

        System.out.println(n + "\t" + formattedX + "\t\t" + formattedFx);
    }
}
