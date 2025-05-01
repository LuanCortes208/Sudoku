import java.util.Random;
import java.util.Scanner;

public class Sudoku {
    private int[][] board;
    private int[][] solution;
    private boolean[][] fixed;
    private final int SIZE = 9;
    private final int EMPTY = 0;
    private final int SUBGRID_SIZE = 3;

    public Sudoku() {
        board = new int[SIZE][SIZE];
        solution = new int[SIZE][SIZE];
        fixed = new boolean[SIZE][SIZE];
        generateSolution();
        createPuzzle();
    }

    private void generateSolution() {
        fillDiagonalSubgrids();
        solveSudoku();
        for (int i = 0; i < SIZE; i++) {
            System.arraycopy(board[i], 0, solution[i], 0, SIZE);
        }
    }

    private void fillDiagonalSubgrids() {
        for (int i = 0; i < SIZE; i += SUBGRID_SIZE) {
            fillSubgrid(i, i);
        }
    }

    private void fillSubgrid(int row, int col) {
        Random random = new Random();
        int[] nums = {1, 2, 3, 4, 5, 6, 7, 8, 9};

        for (int i = 0; i < nums.length; i++) {
            int randomIndex = i + random.nextInt(nums.length - i);
            int temp = nums[i];
            nums[i] = nums[randomIndex];
            nums[randomIndex] = temp;
        }

        int numIndex = 0;
        for (int i = 0; i < SUBGRID_SIZE; i++) {
            for (int j = 0; j < SUBGRID_SIZE; j++) {
                board[row + i][col + j] = nums[numIndex++];
            }
        }
    }

    private boolean solveSudoku() {
        int[] emptyCell = findEmptyCell();
        if (emptyCell == null) {
            return true;
        }

        int row = emptyCell[0];
        int col = emptyCell[1];

        for (int num = 1; num <= SIZE; num++) {
            if (isValid(row, col, num)) {
                board[row][col] = num;

                if (solveSudoku()) {
                    return true;
                }

                board[row][col] = EMPTY;
            }
        }

        return false;
    }

    private int[] findEmptyCell() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (board[i][j] == EMPTY) {
                    return new int[] {i, j};
                }
            }
        }
        return null;
    }

    private boolean isValid(int row, int col, int num) {
        return !usedInRow(row, num) && !usedInColumn(col, num) && !usedInSubgrid(row - row % SUBGRID_SIZE, col - col % SUBGRID_SIZE, num);
    }

    private boolean usedInRow(int row, int num) {
        for (int col = 0; col < SIZE; col++) {
            if (board[row][col] == num) {
                return true;
            }
        }
        return false;
    }

    private boolean usedInColumn(int col, int num) {
        for (int row = 0; row < SIZE; row++) {
            if (board[row][col] == num) {
                return true;
            }
        }
        return false;
    }

    private boolean usedInSubgrid(int startRow, int startCol, int num) {
        for (int row = 0; row < SUBGRID_SIZE; row++) {
            for (int col = 0; col < SUBGRID_SIZE; col++) {
                if (board[row + startRow][col + startCol] == num) {
                    return true;
                }
            }
        }
        return false;
    }

    private void createPuzzle() {
        Random random = new Random();
        int cellsToRemove = 40 + random.nextInt(21);

        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                fixed[i][j] = true;
            }
        }

        while (cellsToRemove > 0) {
            int row = random.nextInt(SIZE);
            int col = random.nextInt(SIZE);

            if (board[row][col] != EMPTY) {
                board[row][col] = EMPTY;
                fixed[row][col] = false;
                cellsToRemove--;
            }
        }
    }

    public void play() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            printBoard();

            System.out.println("\nEntre com uma posição (linha coluna valor) ou 0 para sair:");
            int input = scanner.nextInt();

            if (input == 0) {
                break;
            }

            int row = input - 1;
            int col = scanner.nextInt() - 1;
            int value = scanner.nextInt();

            if (row < 0 || row >= SIZE || col < 0 || col >= SIZE) {
                System.out.println("Posição inválida! Tente novamente.");
                continue;
            }

            if (value < 1 || value > 9) {
                System.out.println("Valor inválido! Deve ser de 1 a 9.");
                continue;
            }

            if (fixed[row][col]) {
                System.out.println("Não é possível alterar uma posição fixa!");
                continue;
            }

            if (value == solution[row][col]) {
                board[row][col] = value;
                System.out.println("Valor correto!");
            } else {
                System.out.println("Valor incorreto! Tente novamente.");
            }

            if (isSolved()) {
                printBoard();
                System.out.println("\nParabéns! Você resolveu o Sudoku!");
                break;
            }
        }

        scanner.close();
    }

    private boolean isSolved() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (board[i][j] != solution[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }

    private void printBoard() {
        System.out.println("\n---------------------------------");
        for (int i = 0; i < SIZE; i++) {
            System.out.print("| ");
            for (int j = 0; j < SIZE; j++) {
                if (board[i][j] == EMPTY) {
                    System.out.print("  ");
                } else {
                    System.out.print(board[i][j] + " ");
                }

                if ((j + 1) % SUBGRID_SIZE == 0) {
                    System.out.print("| ");
                }
            }
            System.out.println();
            if ((i + 1) % SUBGRID_SIZE == 0) {
                System.out.println("---------------------------------");
            }
        }
    }

    public static void main(String[] args) {
        System.out.println("Bem-vindo ao Sudoku!");
        Sudoku game = new Sudoku();
        game.play();
    }
}