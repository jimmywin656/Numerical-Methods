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
    }

    // Secant method implementation
    private static double secantMethod(Function<Double, Double> function, double x0, double x1, double tolerance) {
        System.out.println();
        System.out.println("Secant Method from [" + x0 + ", " + x1 + "]");
        System.out.println("-------------------------------------------------------");
        System.out.println("n \t x \t\t f(x) \t\t approx error.");
        System.out.println("-------------------------------------------------------");

        for (int i = 0; i < 100; i++) {
            double fx0 = function.apply(x0);
            double fx1 = function.apply(x1);

            // update the estimates using the secant formula
            double x2 = x1 - fx1 * ((x1 - x0) / (fx1 - fx0));

            // calculate the approx. rel err
            double ea = Math.abs((x2-x1) / x2);

            printSecant(i, fx0, fx1, ea);

            x0 = x1;
            x1 = x2;

            if (ea < tolerance) {
                // Print the root
                System.out.println();
                System.out.println("Function converges at Root: " + x1 + " after " + (i+1) + " iterations.");
                System.out.println();
                return x1;      // return estimated root
            }
        }

        // Max iterations reached
        System.out.println("Maximum iterations reached without convergence.");
        return Double.NaN;
    }

    private static void printSecant(int n, double x, double fx, double ea) {
        DecimalFormat decimalFormat = new DecimalFormat("#.######");
        DecimalFormat errorFormat = new DecimalFormat("#.########");
    
        String formattedN = String.format("%-9d", n);
        String formattedX = String.format("%-16s", decimalFormat.format(x));
        String formattedFx = String.format("%-16s", decimalFormat.format(fx));
        String formattedEa = String.format("%-20s", errorFormat.format(ea));
    
        System.out.println(formattedN + formattedX + formattedFx + formattedEa);
    }
}
