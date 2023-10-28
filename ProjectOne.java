import java.io.File;
import java.io.FileNotFoundException;
import java.text.DecimalFormat;
import java.util.Scanner;

public class ProjectOne {
  
  public static void main(String[] args) {
    // Use scanner to get num of equations from user
    Scanner scanner = new Scanner(System.in);
    System.out.println();
    System.out.println("Enter the number of linear equations (n <= 10): ");
    int numEquations = scanner.nextInt();

    // Initialize matrix
    double[][] matrix = new double[numEquations][numEquations + 1];

    // Boolean for following the prompt
    boolean validInput = false;

    while (!validInput) {
      System.out.println("Press 1 to enter coefficient matrix\nPress 2 to enter file name with predetermined matrix");
      int choice = scanner.nextInt();
      scanner.nextLine();    // consume new line character

      if (choice == 1)  {
        // Read coefficients row by row
        System.out.println("Enter the coefficients (row by row, including b values):");
        for (int i = 0; i < numEquations; i++) {
            for (int j = 0; j <= numEquations; j++) {
                matrix[i][j] = scanner.nextDouble();
            }
        }
        System.out.println();   // add space
        scanner.close();
        validInput = true;
      } else if (choice == 2) {
          System.out.println("Enter the name of the file: ");
          String fileName = scanner.nextLine();
          File file = new File(fileName);

          try {
            Scanner fileScanner = new Scanner(file);
            
            while (fileScanner.hasNextLine()) {
              for (int i = 0; i < numEquations; i++) {
                for (int j = 0; j <= numEquations; j++) {
                    matrix[i][j] = fileScanner.nextDouble();
                }
              }
              System.out.println();   // add space
            }
            fileScanner.close();
          } catch (FileNotFoundException e) {
            System.err.println("File not found: " + fileName);
          }
          validInput = true;
      } else {
        System.out.println("Invalid Input.");
      }
    } // end while
    // Finished saving matrix either from user input or file

    gaussianElimination(matrix, numEquations);    
  }   // end main

  public static void gaussianElimination(double[][] matrix, int n) {

      // This loop gets the scale Factors and stores them in an array called scaleFactors
      // to be used later for calculating scale ratios
      double[] scaleFactors = new double[n];
      int[] indexVector = new int[n];
      for (int i= 0; i < n; i++) {
        double max = 0;
        for (int j = i; j < n; j++) {
          double largest = Math.abs(findMaxInRow(matrix, j, n));
          if (largest > max) {
            max = largest;
            scaleFactors[j] = max;
          }
        }
        indexVector[i] = i;
      }

      // Loop for showing steps
      for (int i = 0; i < n; i++) {
        int pivotRow = 0;
        // Print the current matrix
        System.out.println("Step " + (i + 1) + ":");
        printMatrix(matrix, n);

        // Find scale ratios
        double[] scaleRatios = new double[n - i];
        DecimalFormat scaleFormat = new DecimalFormat("#.###");
        int scaleRatiosIndex = 0;
        // Loop that divides the leading coefficient with the max value in each row (scale factor)
        for (int k = i; k < n; k++) {
            // Abs value of the first coefficient of each row
            double absValue = Math.abs(matrix[k][i]);
            double ratio = absValue / scaleFactors[k];
            
            // Add ratio to scaleRatios array
            scaleRatios[scaleRatiosIndex] = Double.parseDouble(scaleFormat.format(ratio));
            scaleRatiosIndex++;
        }

        // Find pivot row based on scale ratios
        double max = 0;
        for (int l = i; l < scaleRatios.length; l++) {
          if (scaleRatios[l] > max) {
            max = scaleRatios[l];
            pivotRow = l;  
          }
        }
        
        // Move pivot row to the top
        if (pivotRow + i != i) {
          swapRows(matrix, i, pivotRow+i);
          swapIndex(indexVector, i, pivotRow+i);
          swapIndex(scaleFactors, i, pivotRow+i);
        }

        printInfo(scaleRatios, pivotRow, i);
      

        // Eliminate entries below the pivot
        for (int j = i + 1; j < n; j++) {
            double factor = matrix[j][i] / matrix[i][i];
            for (int k = i; k <= n; k++) {
                matrix[j][k] -= factor * matrix[i][k];
            }
        }
        }

      // Back-substitution
      for (int i = n - 1; i >= 0; i--) {
          for (int j = i + 1; j < n; j++) {
              matrix[i][n] -= matrix[i][j] * matrix[j][n];
          }
          matrix[i][n] /= matrix[i][i];
      }

      // Print the final solution
      System.out.println("Final Solution:");
      DecimalFormat df = new DecimalFormat("#.####");
      for (int i = 0; i < n; i++) {
        double roundedAnswer = Double.parseDouble(df.format(matrix[i][n]));
          System.out.println("x" + (i + 1) + " = " + roundedAnswer);
      }
      System.out.println();
  }

  public static void swapRows(double[][] matrix, int i, int j) {
    double[] temp = matrix[i];
    matrix[i] = matrix[j];
    matrix[j] = temp;
  }

  public static void swapIndex(int[] indexes, int i, int pivot) {
    int temp = indexes[i];
    indexes[i] = indexes[pivot];
    indexes[pivot] = temp;
  }

  public static void swapIndex(double[] indexes, int i, int pivot) {
    double temp = indexes[i];
    indexes[i] = indexes[pivot];
    indexes[pivot] = temp;
  }

  public static double findMaxInRow(double[][] matrix, int row, int n) {
    double max = 0;
    for (int j = 0; j < n; j++) {
        double absValue = Math.abs(matrix[row][j]);
        if (absValue > max) {
            max = absValue;
        }
    }
    return max;
  }

  public static void printMatrix(double[][] matrix, int numEquations) {
    DecimalFormat df = new DecimalFormat("#.####");
    for (int i = 0; i < numEquations; i++) {
        for (int j = 0; j <= numEquations; j++) {
            double roundedValue = Double.parseDouble(df.format(matrix[i][j]));
            System.out.print(roundedValue + " ");
        }
        System.out.println();
    }
    System.out.println();
  }

  public static void printArray(double[] arr) {
    System.out.print("[");
    for (int i = 0; i < arr.length; i++) {
      if (i != arr.length-1) {
        System.out.print(arr[i] + ", ");
      } else {
        System.out.print(arr[i]);
      }
    }
    System.out.print("]");
  }

  public static void printArray(int[] arr) {
    System.out.print("[");
    for (int i = 0; i < arr.length; i++) {
      if (i != arr.length-1) {
        System.out.print(arr[i] + ", ");
      } else {
        System.out.print(arr[i]);
      }
    }
    System.out.print("]");
  }

  public static void printInfo(double[] scaleRatios, int pivotRow, int i) {
      // Print scaled ratios
      System.out.print("Scaled Ratios: ");
      printArray(scaleRatios);
      System.out.println();
      // Print the pivot row
      System.out.println("Pivot Row: " + (pivotRow + 1 + i));
      System.out.println();
  }

}
