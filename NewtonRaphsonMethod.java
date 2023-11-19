import java.util.function.Function;
import java.text.DecimalFormat;

public class NewtonRaphsonMethod {
    
    public static void main(String[] args) {
        // Define the function for which you want to find the root
        Function<Double, Double> function = x -> 2*(x*x*x) - 11.7*(x*x) + 17.7*x - 5;

        // Define the derivative of the function
        Function<Double, Double> derivative = x -> 6*(x*x) - 23.4*x + 17.7;

        // Initial guess for the root
        double initialGuess = 4.0;      // guesses for 3 roots: 1, 2, 4

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
        System.out.println("--------------------------------------------------");
        System.out.println("n \t x \t\t f(x) \t\t error");
        System.out.println("--------------------------------------------------");
        for (int i = 0; i < 100; i++) {
            double fx = function.apply(x0);
            double fPrimeX = derivative.apply(x0);

            // calculate the approximate relative error
            double ea = Math.abs((x0-(x0-fx / fPrimeX)) / x0) * 100;

            printLine(i, x0, fx, ea);

            if (Math.abs(fx) < tolerance) {
                return x0;      // return estimated root
            }


            x0 = x0 - fx / fPrimeX;
        }

        // Max iterations reached
        System.out.println("Maximum iterations reached without convergence.");
        return Double.NaN;      // or can return x0 if you want the latest estimated root
    }

    private static void printLine(int n, double x, double fx, double ea) {
        DecimalFormat decimalFormat = new DecimalFormat("#.####");
    
        String formattedN = String.format("%-9d", n);
        String formattedX = String.format("%-16s", decimalFormat.format(x));
        String formattedFx = String.format("%-16s", decimalFormat.format(fx));
        String formattedEa = String.format("%-20s", decimalFormat.format(ea));
    
        System.out.println(formattedN + formattedX + formattedFx + formattedEa);
    }
}
