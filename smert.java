import java.util.Scanner;

public class smert {
    private static int boardSize = 3;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            displayMainMenu();
            int choice = scanner.nextInt();
            scanner.nextLine();
            handleMenuChoice(choice, scanner);
        }
    }

    private static void displayMainMenu() {
        System.out.println("Головне меню:");
        System.out.println("1. Грати");
        System.out.println("2. Налаштування");
        System.out.println("3. Вихід");
        System.out.print("Виберіть опцію: ");
    }

    private static void handleMenuChoice(int choice, Scanner scanner) {
        switch (choice) {
            case 1:
                playGame(scanner);
                break;
            case 2:
                configureSettings(scanner);
                break;
            case 3:
                System.out.println("Вихід із програми. До побачення!");
                scanner.close();
                System.exit(0);
            default:
                System.out.println("Невірний вибір. Спробуйте ще раз.");
        }
    }

    private static void playGame(Scanner scanner) {
        char[][] board = initializeBoard();
        char currentPlayer = 'X';
        boolean gameOver = false;

        while (!gameOver) {
            displayBoard(board);
            gameOver = handlePlayerTurn(scanner, board, currentPlayer);
            if (!gameOver) {
                currentPlayer = switchPlayer(currentPlayer);
            }
        }
        displayBoard(board);
        askForReplay(scanner);
    }

    private static char[][] initializeBoard() {
        char[][] board = new char[boardSize + 1][boardSize + 1];
        for (int i = 0; i <= boardSize; i++) {
            for (int j = 0; j <= boardSize; j++) {
                board[i][j] = (i == 0 && j == 0) ? ' ' : (i == 0 ? (char) ('0' + j) : (j == 0 ? (char) ('0' + i) : '-'));
            }
        }
        return board;
    }

    private static void displayBoard(char[][] board) {
        for (int i = 0; i <= boardSize; i++) {
            for (int j = 0; j <= boardSize; j++) {
                System.out.printf(" %c |", board[i][j]);
            }
            System.out.println();
            if (i < boardSize) {
                System.out.println("----".repeat(boardSize + 1));
            }
        }
    }

    private static boolean handlePlayerTurn(Scanner scanner, char[][] board, char currentPlayer) {
        System.out.println("Гравець " + currentPlayer + ", ваш хід.");
        int[] move = getPlayerMove(scanner, board);
        if (move[0] == 0 && move[1] == 0) return true;

        board[move[0]][move[1]] = currentPlayer;
        return checkWin(board, currentPlayer) || checkDraw(board);
    }

    private static int[] getPlayerMove(Scanner scanner, char[][] board) {
        int row, col;
        while (true) {
            System.out.print("Введіть рядок та стовпець (наприклад '1 2', якщо треба вийти з гри '0 0'): ");
            if (scanner.hasNextInt()) {
                row = scanner.nextInt();
                if (scanner.hasNextInt()) {
                    col = scanner.nextInt();
                    if (row == 0 && col == 0) {
                        System.out.println("Повернення до головного меню...");
                        return new int[]{0, 0};
                    }
                    if (row >= 1 && row <= boardSize && col >= 1 && col <= boardSize && board[row][col] == '-') {
                        return new int[]{row, col};
                    }
                }
            }
            scanner.nextLine();
            System.out.println("Невірний хід. Спробуйте ще раз.");
        }
    }

    private static char switchPlayer(char currentPlayer) {
        return (currentPlayer == 'X') ? 'O' : 'X';
    }

    private static boolean checkWin(char[][] board, char player) {
        for (int i = 1; i <= boardSize; i++) {
            if (checkLine(board, player, i, true) || checkLine(board, player, i, false)) return true;
        }
        return checkDiagonals(board, player);
    }

    private static boolean checkLine(char[][] board, char player, int index, boolean isRow) {
        for (int i = 1; i <= boardSize; i++) {
            if ((isRow ? board[index][i] : board[i][index]) != player) return false;
        }
        return true;
    }

    private static boolean checkDiagonals(char[][] board, char player) {
        boolean diag1 = true, diag2 = true;
        for (int i = 1; i <= boardSize; i++) {
            if (board[i][i] != player) diag1 = false;
            if (board[i][boardSize - i + 1] != player) diag2 = false;
        }
        return diag1 || diag2;
    }

    private static boolean checkDraw(char[][] board) {
        for (int i = 1; i <= boardSize; i++) {
            for (int j = 1; j <= boardSize; j++) {
                if (board[i][j] == '-') return false;
            }
        }
        return true;
    }

    private static void askForReplay(Scanner scanner) {
        System.out.print("Бажаєте зіграти ще раз? (так/ні): ");
        if (!scanner.next().equalsIgnoreCase("так")) {
            System.out.println("Повернення до головного меню...");
        }
    }

    private static void configureSettings(Scanner scanner) {
        System.out.println("1. 3x3 | 2. 5x5 | 3. 7x7 | 4. 9x9");
        int[] sizes = {3, 5, 7, 9};
        int choice = scanner.nextInt();
        if (choice >= 1 && choice <= 4) boardSize = sizes[choice - 1];
        System.out.println("Розмір змінено на " + boardSize + "x" + boardSize);
    }
}
