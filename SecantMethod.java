import java.util.function.Function;
import java.text.DecimalFormat;

public class SecantMethod {
  
    public static void main(String[] args) {
        // Define the function for which you want to find the root
        Function<Double, Double> function = x -> 2*(x*x*x) - 11.7*(x*x) + 17.7*x - 5;

        // Initial guesses for the roots
        double x0 = 3.0;            // (0,1) , (1,3), (3,4)
        double x1 = 4.0;

        // Set the tolerance level for convergence
        double tolerance = 0.01;

        // Find the root using the secant method
        double root = secantMethod(function, x0, x1, tolerance);

        // Print the result
        System.out.println();
        System.out.println("Function converges at Root: " + root);
        System.out.println();
    }

    // Secant method implementation
    private static double secantMethod(Function<Double, Double> function, double x0, double x1, double tolerance) {
        System.out.println();
        System.out.println("Secant Method");
        System.out.println("------------------------------------------------");
        System.out.println("n \t x \t\t f(x) \t\t error");
        System.out.println("------------------------------------------------");

        for (int i = 0; i < 100; i++) {
            double fx0 = function.apply(x0);
            double fx1 = function.apply(x1);

            // calculate the approx. rel err
            double ea = Math.abs((x1-x0) / x1) * 100;

            printLine(i, fx0, fx1, ea);

            if (Math.abs(fx1) < tolerance) {
                return x1;      // return estimated root
            }

            // update the estimates using the secant formula
            double x2 = x1 - fx1 * ((x1 - x0) / (fx1 - fx0));

            x0 = x1;
            x1 = x2;
        }

        // Max iterations reached
        System.out.println("Maximum iterations reached without convergence.");
        return Double.NaN;
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
