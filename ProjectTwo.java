import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;

public class ProjectTwo {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter the number of linear equations (n <= 10): ");
        int n = scanner.nextInt();

        double[][] coefficients = new double[n][n + 1];
        double[] initialSolution = new double[n];

        System.out.println("Choose input method: ");
        System.out.println("1. Enter coefficients from the command line");
        System.out.println("2. Enter a file name with coefficients");
        int inputMethod = scanner.nextInt();

        if (inputMethod == 1) {
            // Read coefficients from the command line
            System.out.println("Enter the coefficients for each equation:");
            for (int i = 0; i < n; i++) {
                for (int j = 0; j <= n; j++) {
                    coefficients[i][j] = scanner.nextDouble();
                }
            }
            System.out.println("Enter the initial solution:");
            for (int i = 0; i < n; i++) {
                initialSolution[i] = scanner.nextDouble();
            }
        } else if (inputMethod == 2) {
            // Read coefficients from a file
            System.out.print("Enter the file name: ");
            String fileName = scanner.next();
            try {
                Scanner fileScanner = new Scanner(new File(fileName));
                for (int i = 0; i < n; i++) {
                    for (int j = 0; j <= n; j++) {
                        coefficients[i][j] = fileScanner.nextDouble();
                    }
                }
                System.out.println("Enter the initial solution:");
                for (int i = 0; i < n; i++) {
                    initialSolution[i] = scanner.nextDouble();
                }
                fileScanner.close();
            } catch (FileNotFoundException e) {
                System.out.println("File not found.");
                scanner.close();
                return;
            }
        } else {
            System.out.println("Invalid input method.");
            scanner.close();
            return;
        }

        System.out.print("Enter the desired stopping error: ");
        double error = scanner.nextDouble();

        System.out.println("Using Jacobi Iterative Method:");
        double[] jacobiSolution = jacobi(coefficients, initialSolution, error);
        printSolution(jacobiSolution);

        System.out.println("Using Gauss-Seidel Iterative Method:");
        double[] gaussSeidelSolution = gaussSeidel(coefficients, initialSolution, error);
        printSolution(gaussSeidelSolution);

        scanner.close();
    }

    public static double[] jacobi(double[][] coefficients, double[] initialSolution, double error) {
        int n = initialSolution.length;
        double[] currentSolution = new double[n];
        int maxIterations = 50;

        for (int iteration = 1; iteration <= maxIterations; iteration++) {
            for (int i = 0; i < n; i++) {
                double sum = coefficients[i][n];
                for (int j = 0; j < n; j++) {
                    if (j != i) {
                        sum -= coefficients[i][j] * initialSolution[j];
                    }
                }
                currentSolution[i] = sum / coefficients[i][i];
            }
            initialSolution = currentSolution;

            double l2Norm = calculateL2Norm(coefficients, currentSolution);
            System.out.print("Iteration " + iteration + ": ");
            printSolution(currentSolution);

            if (l2Norm < error) {
                System.out.println("Converged to the desired error.");
                return currentSolution;
            }
        }

        System.out.println("Did not reach the desired error after 50 iterations.");
        return currentSolution;
    }

    public static double[] gaussSeidel(double[][] coefficients, double[] initialSolution, double error) {
        int n = initialSolution.length;
        double[] currentSolution = new double[n];
        int maxIterations = 50;

        for (int iteration = 1; iteration <= maxIterations; iteration++) {
            for (int i = 0; i < n; i++) {
                double sum = coefficients[i][n];
                for (int j = 0; j < n; j++) {
                    if (j != i) {
                        sum -= coefficients[i][j] * currentSolution[j];
                    }
                }
                currentSolution[i] = sum / coefficients[i][i];
            }

            double l2Norm = calculateL2Norm(coefficients, currentSolution);
            System.out.print("Iteration " + iteration + ": ");
            printSolution(currentSolution);

            if (l2Norm < error) {
                System.out.println("Converged to the desired error.");
                return currentSolution;
            }
        }

        System.out.println("Did not reach the desired error after 50 iterations.");
        return currentSolution;
    }

    public static double calculateL2Norm(double[][] coefficients, double[] solution) {
        int n = solution.length;
        double sum = 0.0;

        for (int i = 0; i < n; i++) {
            double residual = coefficients[i][n];
            for (int j = 0; j < n; j++) {
                residual -= coefficients[i][j] * solution[j];
            }
            sum += residual * residual;
        }

        return Math.sqrt(sum);
    }

    public static void printSolution(double[] solution) {
        System.out.print("[");
        for (int i = 0; i < solution.length; i++) {
            System.out.printf("%.4f", solution[i]);
            if (i < solution.length - 1) {
                System.out.print(" ");
            }
        }
        System.out.println("]T");
    }
}
