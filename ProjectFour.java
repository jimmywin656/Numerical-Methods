import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Scanner;

public class ProjectFour {
    public static void main(String[] args) {
        try {
            Scanner scanner = new Scanner(System.in);
            System.out.println();
            System.out.print("Enter the file name: ");
            String fileName = scanner.nextLine();
            scanner.close();

            File file = new File(fileName);
            Scanner fileScanner = new Scanner(file);

            // Read x values from the first row
            String[] xRow = fileScanner.nextLine().split(" ");
            double[] xValues = new double[xRow.length];
            for (int i = 0; i < xRow.length; i++) {
                xValues[i] = Double.parseDouble(xRow[i]);
            }

            // Read y values from the second row
            String[] yRow = fileScanner.nextLine().split(" ");
            double[] yValues = new double[yRow.length];
            for (int i = 0; i < yRow.length; i++) {
                yValues[i] = Double.parseDouble(yRow[i]);
            }

            fileScanner.close();

            printDividedDifferenceTable(xValues, yValues);
            printNewtonForm(xValues, yValues);
            printLagrangeForm(xValues, yValues);
            printSimplifiedForm(xValues, yValues);

        } catch (FileNotFoundException e) {
            System.out.println("File not found.");
        }
    }

    private static void printNewtonForm(double[] x, double[] y) {
        System.out.println();
        System.out.println("Newton's Form:");
        double[] dividedDifferenceCoefficients = calculateDividedDifferenceCoefficients(x, y);
    
        StringBuilder newtonForm = new StringBuilder();
        newtonForm.append(formatCoefficient(dividedDifferenceCoefficients[0]));
    
        for (int i = 1; i < x.length; i++) {
            newtonForm.append(" + ").append(formatCoefficient(dividedDifferenceCoefficients[i]));
            for (int j = 0; j < i; j++) {
                newtonForm.append(" * (x - ").append(x[j]).append(")");
            }
        }
    
        System.out.println(newtonForm.toString());
        System.out.println();
    }
    
    

    private static void printLagrangeForm(double[] x, double[] y) {
        System.out.println("Lagrange's Form:");
        StringBuilder lagrangeForm = new StringBuilder();
    
        for (int i = 0; i < x.length; i++) {
            double coefficient = calculateLagrangeCoefficient(x, y, i);
    
            if (coefficient != 0) {
                if (i != 0) {
                    lagrangeForm.append(" + ");
                }
                lagrangeForm.append(formatCoefficient(coefficient));
    
                // Print (x - x[j]) terms
                for (int j = 0; j < x.length; j++) {
                    if (i != j) {
                        lagrangeForm.append(" * (x - ").append(x[j]).append(")");
                    }
                }
            }
        }
    
        System.out.println(lagrangeForm.toString());
        System.out.println();
    }
    

    private static void printSimplifiedForm(double[] x, double[] y) {
        System.out.println("Simplified Form:");
        double[] coefficients = calculateSimplifiedFormCoefficients(x, y);
    
        StringBuilder simplifiedForm = new StringBuilder();
        for (int i = 0; i < coefficients.length; i++) {
            simplifiedForm.append(formatCoefficient(coefficients[i]));
            if (i < coefficients.length - 1) {
                simplifiedForm.append(" * x^").append(coefficients.length - 1 - i);
                simplifiedForm.append(" + ");
            }
        }
    
        System.out.println(simplifiedForm.toString());
        System.out.println();
    }
    
    private static double[] calculateSimplifiedFormCoefficients(double[] x, double[] y) {
        int n = x.length;
        double[][] matrix = new double[n][n + 1];
    
        // Build the augmented matrix
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                matrix[i][j] = Math.pow(x[i], n - 1 - j);
            }
            matrix[i][n] = y[i];
        }
    
        // Perform Gaussian elimination with pivot
        for (int i = 0; i < n; i++) {
            if (matrix[i][i] == 0) {
                // Find a non-zero pivot in the same column
                int swapRow = -1;
                for (int j = i + 1; j < n; j++) {
                    if (matrix[j][i] != 0) {
                        swapRow = j;
                        break;
                    }
                }
    
                // If a non-zero pivot is found, swap rows
                if (swapRow != -1) {
                    double[] temp = matrix[i];
                    matrix[i] = matrix[swapRow];
                    matrix[swapRow] = temp;
                } else {
                    // Handle the case where all elements in the column are zero
                    throw new IllegalArgumentException("Matrix is singular");
                }
            }
    
            double pivot = matrix[i][i];
            for (int j = i + 1; j < n; j++) {
                double ratio = matrix[j][i] / pivot;
                for (int k = i; k <= n; k++) {
                    matrix[j][k] -= ratio * matrix[i][k];
                }
            }
        }
    
        // Back-substitution
        double[] coefficients = new double[n];
        for (int i = n - 1; i >= 0; i--) {
            coefficients[i] = matrix[i][n];
            for (int j = i + 1; j < n; j++) {
                coefficients[i] -= matrix[i][j] * coefficients[j];
            }
            coefficients[i] /= matrix[i][i];
        }
    
        return coefficients;
    }
    
    

    private static void printDividedDifferenceTable(double[] x, double[] y) {
        System.out.println();
        System.out.println("Divided Difference:");
        int n = x.length;
        double[][] f = calculateDividedDifferenceTable(x, y);

        // Print header
        System.out.printf("%-25s", "x");
        for (int i = 0; i < n; i++) {
            int commaCount = i; // Number of commas
            StringBuilder header = new StringBuilder("f[");
            for (int j = 0; j < commaCount; j++) {
                header.append(",");
            }
            header.append("]");
            System.out.printf("%-25s", header.toString());
        }
        System.out.println();

        // Print table
        for (int i = 0; i < n; i++) {
            System.out.printf("%-25s", x[i]);
            for (int j = 0; j < n - i; j++) {
                System.out.printf("%-25s", formatCoefficient(f[i][j]));
            }
            System.out.println();
        }
    }

    private static double[][] calculateDividedDifferenceTable(double[] x, double[] y) {
        int n = x.length;
        double[][] f = new double[n][n];

        for (int i = 0; i < n; i++) {
            f[i][0] = y[i];
        }

        for (int j = 1; j < n; j++) {
            for (int i = 0; i < n - j; i++) {
                f[i][j] = (f[i + 1][j - 1] - f[i][j - 1]) / (x[i + j] - x[i]);
            }
        }

        return f;
    }

    private static double[] calculateDividedDifferenceCoefficients(double[] x, double[] y) {
        int n = x.length;
        double[] f = Arrays.copyOf(y, n);
    
        for (int j = 1; j < n; j++) {
            for (int i = n - 1; i >= j; i--) {
                f[i] = (f[i] - f[i - 1]) / (x[i] - x[i - j]);
            }
        }
    
        return f;
    }

    private static double calculateLagrangeCoefficient(double[] x, double[] y, int index) {
        double coefficient = y[index];
        
        for (int i = 0; i < x.length; i++) {
            if (i != index) {
                coefficient /= (x[index] - x[i]);
            }
        }
    
        return coefficient;
    }

    private static String formatCoefficient(double coefficient) {
        if (coefficient == 0) {
            return "";
        } else {
            // Round the coefficient to 2 decimal places
            String formattedCoefficient = String.format("%.2f", coefficient);
            return formattedCoefficient;
        }
    }
}
