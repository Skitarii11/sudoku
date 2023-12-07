import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SudokuSolverUI {

    private JFrame frame;
    private JPanel sudokuPanel;
    private JTextField[][] sudokuCells;
    private JButton solveButton;
    private JComboBox<String> difficultyComboBox;

    private static final String[] DIFFICULTY_LEVELS = {"Easy", "Medium", "Hard"};
    private static final int[][][] INITIAL_SUDOKU_GRIDS = {
            { // Easy
                {5, 3, 0, 0, 7, 0, 0, 0, 0},
                {6, 0, 0, 1, 9, 5, 0, 0, 0},
                {0, 9, 8, 0, 0, 0, 0, 6, 0},
                {8, 0, 0, 0, 6, 0, 0, 0, 3},
                {4, 0, 0, 8, 0, 3, 0, 0, 1},
                {7, 0, 0, 0, 2, 0, 0, 0, 6},
                {0, 6, 0, 0, 0, 0, 2, 8, 0},
                {0, 0, 0, 4, 1, 9, 0, 0, 5},
                {0, 0, 0, 0, 8, 0, 0, 7, 9}
        },
        { // Medium
                {5, 3, 0, 0, 0, 0, 0, 0, 0},
                {6, 0, 0, 1, 9, 5, 0, 0, 0},
                {0, 9, 8, 0, 0, 0, 0, 6, 0},
                {8, 0, 0, 0, 6, 0, 0, 0, 3},
                {4, 0, 0, 8, 0, 0, 0, 0, 1},
                {0, 0, 0, 0, 2, 0, 0, 0, 6},
                {0, 6, 0, 0, 0, 0, 2, 8, 0},
                {0, 0, 0, 4, 0, 9, 0, 0, 5},
                {0, 0, 0, 0, 8, 0, 0, 7, 9}
        },
        { // Hard
                {0, 3, 0, 0, 7, 0, 0, 0, 0},
                {6, 0, 0, 1, 0, 5, 0, 0, 0},
                {0, 0, 8, 0, 0, 0, 0, 6, 0},
                {8, 0, 0, 0, 6, 0, 0, 0, 3},
                {0, 0, 0, 8, 0, 3, 0, 0, 0},
                {7, 0, 0, 0, 0, 0, 0, 0, 6},
                {0, 6, 0, 0, 0, 0, 2, 8, 0},
                {0, 0, 0, 0, 0, 9, 0, 0, 5},
                {0, 0, 0, 0, 8, 0, 0, 0, 9}
            }
    };

    public SudokuSolverUI() {
        initializeUI();
    }

    private void initializeUI() {
        frame = new JFrame("Sudoku Solver");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 450);

        sudokuPanel = new JPanel(new GridLayout(9, 9));
        sudokuCells = new JTextField[9][9];

        // Initialize Sudoku cells
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                sudokuCells[row][col] = new JTextField(1);
                sudokuPanel.add(sudokuCells[row][col]);
            }
        }

        solveButton = new JButton("Solve");
        solveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                solveSudokuFromUI();
            }
        });

        difficultyComboBox = new JComboBox<>(DIFFICULTY_LEVELS);
        difficultyComboBox.setSelectedIndex(0); // Default to Easy
        difficultyComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setDifficultyLevel();
            }
        });

        JPanel controlPanel = new JPanel(new FlowLayout());
        controlPanel.add(solveButton);
        controlPanel.add(new JLabel("Difficulty:"));
        controlPanel.add(difficultyComboBox);

        frame.getContentPane().setLayout(new BorderLayout());
        frame.getContentPane().add(sudokuPanel, BorderLayout.CENTER);
        frame.getContentPane().add(controlPanel, BorderLayout.SOUTH);

        setDifficultyLevel();

        frame.setVisible(true);
    }

    private void solveSudokuFromUI() {
        int[][] sudokuGrid = new int[9][9];

        // Get values from UI and populate the Sudoku grid
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                String value = sudokuCells[row][col].getText();
                if (value.isEmpty()) {
                    sudokuGrid[row][col] = 0;
                } else {
                    sudokuGrid[row][col] = Integer.parseInt(value);
                }
            }
        }

        SudokuSolver solver = new SudokuSolver();

        if (solver.solveSudoku(sudokuGrid)) {
            // Update the UI with the solved values
            for (int row = 0; row < 9; row++) {
                for (int col = 0; col < 9; col++) {
                    sudokuCells[row][col].setText(Integer.toString(sudokuGrid[row][col]));
                }
                // Disable "Solve" button after solving
                solveButton.setEnabled(false);
            }
        } else {
            JOptionPane.showMessageDialog(frame, "No solution exists.", "Sudoku Solver", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void setDifficultyLevel() {
        int selectedIndex = difficultyComboBox.getSelectedIndex();
        int[][] initialGrid = cloneGrid(INITIAL_SUDOKU_GRIDS[selectedIndex]);

        // Display the initial Sudoku grid based on the selected difficulty level
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                int value = initialGrid[row][col];
                sudokuCells[row][col].setText(value > 0 ? Integer.toString(value) : "");
                sudokuCells[row][col].setEditable(value == 0);
            }
            // Enable "Solve" button when selecting a difficulty level
            solveButton.setEnabled(true);
        }
    }

    private int[][] cloneGrid(int[][] original) {
        int[][] copy = new int[original.length][];
        for (int i = 0; i < original.length; i++) {
            copy[i] = original[i].clone();
        }
        return copy;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new SudokuSolverUI();
            }
        });
    }
}

class SudokuSolver {

    public boolean solveSudoku(int[][] grid) {
        int N = grid.length;

        // Find an empty cell
        int[] emptyCell = findEmptyCell(grid);
        int row = emptyCell[0];
        int col = emptyCell[1];

        // If no empty cell is found, the Sudoku is solved
        if (row == -1 && col == -1) {
            return true;
        }

        // Try filling the empty cell with values 1 to 9
        for (int num = 1; num <= 9; num++) {
            if (isSafe(grid, row, col, num)) {
                // Place the number in the cell
                grid[row][col] = num;

                // Recur to fill the rest of the grid
                if (solveSudoku(grid)) {
                    return true;
                }

                // If placing the number doesn't lead to a solution, backtrack
                grid[row][col] = 0;
            }
        }

        // If no number can be placed in the current cell, backtrack
        return false;
    }

    private boolean isSafe(int[][] grid, int row, int col, int num) {
        return !usedInRow(grid, row, num) &&
               !usedInColumn(grid, col, num) &&
               !usedInBox(grid, row - row % 3, col - col % 3, num);
    }

    private boolean usedInRow(int[][] grid, int row, int num) {
        for (int col = 0; col < 9; col++) {
            if (grid[row][col] == num) {
                return true;
            }
        }
        return false;
    }

    private boolean usedInColumn(int[][] grid, int col, int num) {
        for (int row = 0; row < 9; row++) {
            if (grid[row][col] == num) {
                return true;
            }
        }
        return false;
    }

    private boolean usedInBox(int[][] grid, int boxStartRow, int boxStartCol, int num) {
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                if (grid[row + boxStartRow][col + boxStartCol] == num) {
                    return true;
                }
            }
        }
        return false;
    }

    private int[] findEmptyCell(int[][] grid) {
        int[] cell = {-1, -1};

        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                if (grid[row][col] == 0) {
                    cell[0] = row;
                    cell[1] = col;
                    return cell;
                }
            }
        }

        return cell;
    }
}
