import java.util.function.Function;

public class ProjectThree {

    public static void main(String[] args) {
        double tolerance = 0.01; // Error tolerance (ea)
        int maxIterations = 100;
        
        // double a = 0.0; // Lower bound of the interval
        // double b = 4.0; // Upper bound of the interval

        // Define the function
        Function<Double, Double> function = x -> 2 * Math.pow(x, 3) - 11.7 * Math.pow(x, 2) + 17.7 * x - 5;

        // Root-finding methods
        double bisectionResult = bisectionMethod(function, a, b, tolerance, maxIterations);
        double newtonRaphsonResult = newtonRaphsonMethod(function, a, tolerance, maxIterations);
        double secantResult = secantMethod(function, a, b, tolerance, maxIterations);
        double falsePositionResult = falsePositionMethod(function, a, b, tolerance, maxIterations);

    }

    public static double bisectionMethod(Function<Double, Double> f, double a, double b, double tolerance, int maxIterations) {
      double fa = f.apply(a);
      double fb = f.apply(b);
  
      if (fa * fb >= 0) {
          throw new IllegalArgumentException("The function values at the interval boundaries must have different signs.");
      }
  
      for (int i = 1; i <= maxIterations; i++) {
          double c = (a + b) / 2;
          double fc = f.apply(c);
  
          if (fc == 0 || (b - a) / 2 < tolerance) {
              return c; // The root is found or the interval is within tolerance
          }
  
          if (fa * fc < 0) {
              b = c;
              fb = fc;
          } else {
              a = c;
              fa = fc;
          }
      }
  
      throw new IllegalStateException("Bisection method did not converge within the maximum number of iterations.");
  }
  

    public static double newtonRaphsonMethod(Function<Double, Double> f, double initialGuess, double tolerance, int maxIterations) {
    double x0 = initialGuess;

    for (int i = 1; i <= maxIterations; i++) {
        double fx0 = f.apply(x0);
        double dfx0 = derivative(f, x0);

        if (dfx0 == 0) {
            throw new ArithmeticException("Derivative is zero, Newton-Raphson method cannot continue.");
        }

        double x1 = x0 - fx0 / dfx0;

        if (Math.abs(x1 - x0) < tolerance) {
            return x1; // The root is found
        }

        x0 = x1;
    }

    throw new IllegalStateException("Newton-Raphson method did not converge within the maximum number of iterations.");
}

    public static double secantMethod(Function<Double, Double> f, double x0, double x1, double tolerance, int maxIterations) {
    double xnMinus1 = x0;
    double xn = x1;

    for (int i = 1; i <= maxIterations; i++) {
        double fxnMinus1 = f.apply(xnMinus1);
        double fxn = f.apply(xn);

        if (Math.abs(fxn - fxnMinus1) < tolerance) {
            return xn; // The root is found
        }

        double xNext = xn - fxn * (xn - xnMinus1) / (fxn - fxnMinus1);

        xnMinus1 = xn;
        xn = xNext;
    }

    throw new IllegalStateException("Secant method did not converge within the maximum number of iterations.");
}


    public static double falsePositionMethod(Function<Double, Double> f, double a, double b, double tolerance, int maxIterations) {
    double fa = f.apply(a);
    double fb = f.apply(b);

    if (fa * fb >= 0) {
        throw new IllegalArgumentException("The function values at the interval boundaries must have different signs.");
    }

    double c = 0.0; // Initialize c

    for (int i = 1; i <= maxIterations; i++) {
        c = (a * fb - b * fa) / (fb - fa);
        double fc = f.apply(c);

        if (Math.abs(fc) < tolerance) {
            return c; // The root is found
        }

        if (fa * fc < 0) {
            b = c;
            fb = fc;
        } else {
            a = c;
            fa = fc;
        }
    }

    throw new IllegalStateException("False Position method did not converge within the maximum number of iterations.");
}


    public static double derivative(Function<Double, Double> f, double x) {
    // Calculate the derivative of the function at point x using a numerical approximation
    double h = 1e-6; // Small value for numerical differentiation
    return (f.apply(x + h) - f.apply(x)) / h;
    }
    
}
