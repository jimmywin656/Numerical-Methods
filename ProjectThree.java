import java.text.DecimalFormat;
import java.util.function.Function;
import java.util.Scanner;

public class ProjectThree {

    public static void main(String[] args) {
        double tolerance = 0.01; // Error tolerance (ea)
        int maxIterations = 100;

        // Define the function
        Function<Double, Double> function1 = x -> 2 * Math.pow(x, 3) - 11.7 * Math.pow(x, 2) + 17.7 * x - 5;
        Function<Double, Double> derivative1 = x -> 6*(x*x) - 23.4*x + 17.7;

        Function<Double, Double> function2 = x -> x + 10 - x*Math.cosh(50/x);
        Function<Double, Double> derivative2 = x -> (50*Math.sinh(50/x))/x - Math.cosh(50/x) + 1;

        Scanner scan = new Scanner(System.in);

        System.out.println("Select a function to test: ");
        System.out.println("-----------------------");
        System.out.println("1. f(x) = 2x^3 - 11.7x^2 + 17.7x - 5");
        System.out.println("2. f(x) = x + 10 - xcosh(50/x)");
        int choice = scan.nextInt();

        if (choice == 1) {
            System.out.println("Please enter in 2 guesses/starting values to be used in the calculation for Bisection, False Position, and Secant Method");
            System.out.print("Guess 1: ");
            double guess1 = scan.nextDouble();
            System.out.print("Guess 2: ");
            double guess2 = scan.nextDouble();

            System.out.println("Please enter in an initial guess to be used in the calculation for Newton Raphson Method");
            System.out.print("Initial guess: ");
            double initialGuess = scan.nextDouble();

            // Root-finding methods for function 1
            double bisectionResult = bisectionMethod(function1, guess1, guess2, maxIterations, tolerance);
            double falsePositionResult = falsePositionMethod(function1, guess1, guess2, maxIterations, tolerance);
            double newtonRaphsonResult = newtonRaphsonMethod(function1, derivative1, initialGuess, tolerance);
            double secantResult = secantMethod(function1, guess1, guess2, tolerance);
        } else if (choice == 2) {
            System.out.println("Please enter in 2 guesses/starting values to be used in the calculation for Bisection, False Position, and Secant Method");
            System.out.print("Guess 1: ");
            double guess1 = scan.nextDouble();
            System.out.print("Guess 2: ");
            double guess2 = scan.nextDouble();

            System.out.println("Please enter in an initial guess to be used in the calculation for Newton Raphson Method");
            System.out.print("Initial guess: ");
            double initialGuess = scan.nextDouble();

            // Root-finding methods for function 1
            double bisectionResult = bisectionMethod(function2, guess1, guess2, maxIterations, tolerance);
            double falsePositionResult = falsePositionMethod(function2, guess1, guess2, maxIterations, tolerance);
            double newtonRaphsonResult = newtonRaphsonMethod(function2, derivative2, initialGuess, tolerance);
            double secantResult = secantMethod(function2, guess1, guess2, tolerance);
        } else {
            System.out.println("Invalid input. Try again.");
        }
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

    private static double falsePositionMethod(Function<Double, Double> function, double a, double b, int maxIter, double error) {
        double fa = function.apply(a);
        double fb = function.apply(b);

        if (fa * fb > 0) {      // signs =
            System.out.println("Function has same signs at a and b");
            return Double.NaN;
        }

        System.out.println();
        System.out.println("False Position Method from [" + a + ", " + b + "]");
        System.out.println("-------------------------------------------------------");
        System.out.println("n \t c \t\t f(c) \t\t approx. error");
        System.out.println("-------------------------------------------------------");

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
                System.out.println("Function converges at Root: " + c + " after " + (i+1) + " iterations.");
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
        System.out.println("Newton Raphson Method with Initial guess of " + initialGuess);
        System.out.println("-------------------------------------------------------");
        System.out.println("n \t x \t\t f(x) \t\t approx. error");
        System.out.println("-------------------------------------------------------");
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
                System.out.println("Function converges at Root: " + x0 + " after " + (i+1) + " iterations.");
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

    private static void printFalsePosition(int n, double c, double fc, double relError) {
        DecimalFormat decimalFormat = new DecimalFormat("#.######");
        DecimalFormat errorFormat = new DecimalFormat("#.########");

        String formattedN = String.format("%-9d", n);
        String formattedC = String.format("%-16s", decimalFormat.format(c));
        String formattedFc = String.format("%-17s", decimalFormat.format(fc));
        String formattedRelErr = String.format("%-20s", errorFormat.format(relError));
        
        System.out.println(formattedN + formattedC + formattedFc + formattedRelErr);
    }

    private static void printNewton(int n, double x, double fx, double ea) {
        DecimalFormat decimalFormat = new DecimalFormat("#.######");
        DecimalFormat errorFormat = new DecimalFormat("#.########");
    
        String formattedN = String.format("%-9d", n);
        String formattedX = String.format("%-16s", decimalFormat.format(x));
        String formattedFx = String.format("%-16s", decimalFormat.format(fx));
        String formattedEa = String.format("%-20s", errorFormat.format(ea));
    
        System.out.println(formattedN + formattedX + formattedFx + formattedEa);
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
