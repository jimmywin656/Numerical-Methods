import java.text.DecimalFormat;
import java.util.function.Function;

public class ProjectThree {

    public static void main(String[] args) {
        double tolerance = 0.01; // Error tolerance (ea)
        int maxIterations = 100;
        
        double a = 0.0; // Lower bound of the interval
        double b = 1.0; // Upper bound of the interval

        // Define the function
        Function<Double, Double> function = x -> 2 * Math.pow(x, 3) - 11.7 * Math.pow(x, 2) + 17.7 * x - 5;

        Function<Double, Double> bfunc = x -> Math.pow(x, 3) - 3*x + 1;

        // Root-finding methods
        double bisectionResult = bisectionMethod(bfunc, a, b, maxIterations, tolerance);
        double falsePositionResult = falsePositionMethod(function, a, b, maxIterations, tolerance);

        // for newton raphson
        double initialGuess = 1.0;
        // for secant method
        double x0 = 0.0;
        double x1 = 1.0;

         // Define the derivative of function 1
         Function<Double, Double> derivative = x -> 6*(x*x) - 23.4*x + 17.7;

        double newtonRaphsonResult = newtonRaphsonMethod(function, derivative, initialGuess, tolerance);
        double secantResult = secantMethod(function, x0, x1, tolerance);

        
    }

    private static double bisectionMethod(Function<Double, Double> function, double a, double b, int maxIter,double error) {
        double fa = function.apply(a);
        double fb = function.apply(b);

        if (fa*fb > 0) {      // signs =
            System.out.println("Function has same signs at a and b");
            return Double.NaN;
        }

        double currentError = b - a;

        System.out.println();
        System.out.println("Bisection Method");
        System.out.println("------------------------------------------------");
        System.out.println("n \t c \t\t f(c) \t\t error \\t\\t rel. approx. err");
        System.out.println("------------------------------------------------");
        for (int i = 0; i < maxIter; i++) {
            currentError = currentError/2;
            double c = a + currentError;
            double fc = function.apply(c);

            printBisection(i, c, fc, currentError);

            if (Math.abs(currentError) < error) {
                // Print the root
                System.out.println();
                System.out.println("Function converges at Root: " + c);
                System.out.println();
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

    private static double falsePositionMethod(Function<Double, Double> function, double a, double b, int maxIter, double error) {
        double fa = function.apply(a);
        double fb = function.apply(b);

        if (fa * fb > 0) {      // signs =
            System.out.println("Function has same signs at a and b");
            return Double.NaN;
        }

        System.out.println();
        System.out.println("False Position Method");
        System.out.println("------------------------------------------------");
        System.out.println("n \t c \t\t f(c) \t      error");
        System.out.println("------------------------------------------------");

        double prevC = (a * fb - b * fa) / (fb - fa);

        for (int i = 0; i < maxIter; i++) {
            double c = (a * fb - b * fa) / (fb - fa);
            double fc = function.apply(c);
            double ea;

            if (i == 0) {
                ea = 1.0;
            } else {
                // calculate the approx rel err     // fix this
                ea = Math.abs((prevC - c) / prevC);
            }

            printFalsePosition(i, c, fc, ea);

            if (ea < error) {
                // Print the root
                System.out.println();
                System.out.println("Function converges at Root: " + c);
                System.out.println();
                return c;   // return estimated root
            }

            // update prevC for next iteration
            prevC = c;

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

    private static double newtonRaphsonMethod(Function<Double, Double> function, Function<Double, Double> derivative, double initialGuess, double tolerance) {
        
        double prev = initialGuess;
        double x0 = initialGuess;

        System.out.println();
        System.out.println("Newton Raphson Method");
        System.out.println("--------------------------------------------------");
        System.out.println("n \t x \t\t f(x) \t\t error");
        System.out.println("--------------------------------------------------");
        for (int i = 0; i < 100; i++) {
            double fx = function.apply(x0);
            double fPrimeX = derivative.apply(x0);

            prev = x0;
            x0 = x0 - fx / fPrimeX;
            
            // calculate the approximate relative error
            double ea = Math.abs((x0 - prev) / x0);

            printNewton(i, prev, fx, ea);

            if (ea < tolerance) {
                // Print the root
                System.out.println();
                System.out.println("Function converges at Root: " + x0);
                System.out.println();
                return x0;      // return estimated root
            }
        }

        // Max iterations reached
        System.out.println("Maximum iterations reached without convergence.");
        return Double.NaN;      // or can return x0 if you want the latest estimated root
    }

    private static double secantMethod(Function<Double, Double> function, double x0, double x1, double tolerance) {
        System.out.println();
        System.out.println("Secant Method");
        System.out.println("------------------------------------------------");
        System.out.println("n \t x \t\t f(x) \t\t error");
        System.out.println("------------------------------------------------");

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
                System.out.println("Function converges at Root: " + x1);
                System.out.println();
                return x1;      // return estimated root
            }
        }

        // Max iterations reached
        System.out.println("Maximum iterations reached without convergence.");
        return Double.NaN;
    }

    private static void printBisection(int n, double c, double fc, double error) {
        DecimalFormat decimalFormat = new DecimalFormat("#.####");

        String formattedC = decimalFormat.format(c);
        String formattedFc = decimalFormat.format(fc);
        String formattedErr = decimalFormat.format(error);

        System.out.println(n + "\t" + formattedC + "\t\t" + formattedFc + "\t\t" + formattedErr);
    }

    private static void printFalsePosition(int n, double c, double fc, double error) {
        DecimalFormat decimalFormat = new DecimalFormat("#.####");

        String formattedC = decimalFormat.format(c);
        String formattedFc = decimalFormat.format(fc);
        String formattedErr = decimalFormat.format(error);

        System.out.println(n + "\t" + formattedC + "\t\t" + formattedFc + "\t\t" + formattedErr);
    }

    private static void printNewton(int n, double x, double fx, double ea) {
        DecimalFormat decimalFormat = new DecimalFormat("#.####");
    
        String formattedN = String.format("%-9d", n);
        String formattedX = String.format("%-16s", decimalFormat.format(x));
        String formattedFx = String.format("%-16s", decimalFormat.format(fx));
        String formattedEa = String.format("%-20s", decimalFormat.format(ea));
    
        System.out.println(formattedN + formattedX + formattedFx + formattedEa);
    }

    private static void printSecant(int n, double x, double fx, double ea) {
        DecimalFormat decimalFormat = new DecimalFormat("#.####");
        DecimalFormat errorFormat = new DecimalFormat("#.######");
    
        String formattedN = String.format("%-9d", n);
        String formattedX = String.format("%-16s", decimalFormat.format(x));
        String formattedFx = String.format("%-16s", decimalFormat.format(fx));
        String formattedEa = String.format("%-20s", decimalFormat.format(ea));
    
        System.out.println(formattedN + formattedX + formattedFx + formattedEa);
    }


    public static double derivative(Function<Double, Double> f, double x) {
    // Calculate the derivative of the function at point x using a numerical approximation
    double h = 1e-6; // Small value for numerical differentiation
    return (f.apply(x + h) - f.apply(x)) / h;
    }
    
}
