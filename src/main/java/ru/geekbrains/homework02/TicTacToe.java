package ru.geekbrains.homework02;

import java.sql.SQLOutput;
import java.util.Random;
import java.util.Scanner;

public class TicTacToe {

    private static final char DOT_HUMAN = 'X'; // Фишка игрока - человек
    private static final char DOT_AI = '0'; // Фишка игрока - компьютер
    private static final char DOT_EMPTY = '*'; // Признак пустого поля

    private static final Scanner scn = new Scanner(System.in);
    private static final Random random = new Random();

    private static char[][] field; //Двумерный массив хранит текущее состояние игрового поля

    private static int fieldSizeX; // Размерность игрового поля
    private static int fieldSizeY; // Размерность игрового поля
    private static int cellsForWin; // Количество ячеек для победы

    public static void main(String[] args) {
        while (true) {
            initialize();
            printField();
            while (true) {
                humanTurn();
                printField();
                if (checkGameState(DOT_HUMAN, "Вы победили"))
                    break;
                aiTurn();
                printField();
                if (checkGameState(DOT_AI, "Победил компьютер!"))
                    break;
            }
            System.out.println("Желаете сыграть еще раз? (Y - да):");
            if (!scn.next().equalsIgnoreCase("Y"))
                break;
        }
    }


    /**
     * Инициализация объектов игры
     */
    private static void initialize() {
        System.out.println("Введите размеры поля через пробел: ");
        fieldSizeX = scn.nextInt();
        fieldSizeY = scn.nextInt();

        do {
            System.out.println("Введите количество ячеек для победы: ");
            cellsForWin = scn.nextInt();
            if (cellsForWin > Math.max(fieldSizeX, fieldSizeY))
                System.out.println("Количество победных ячеек не может превышать размер поля!");
        }
        while (cellsForWin > Math.max(fieldSizeX, fieldSizeY));
        field = new char[fieldSizeX][fieldSizeY];
        for (int x = 0; x < fieldSizeX; x++) {
            for (int y = 0; y < fieldSizeY; y++) {
                field[x][y] = DOT_EMPTY;
            }
        }
    }

    /**
     * Отрисовка игрового поля
     */

    private static void printField() {
        System.out.print("+");
        for (int x = 0; x < fieldSizeY * 2 + 1; x++) {
            System.out.print((x % 2 == 0) ? "-" : x / 2 + 1);
        }
        System.out.println();

        for (int x = 0; x < fieldSizeX; x++) {
            System.out.print(x + 1 + "|");
            for (int y = 0; y < fieldSizeY; y++) {
                System.out.print(field[x][y] + "|");
            }
            System.out.println();
        }
        for (int x = 0; x < fieldSizeY * 2 + 2; x++) {
            System.out.print("-");
        }
        System.out.println();
    }

    /**
     * Обработка хода игрока (человек)
     */
    private static void humanTurn() {
        int x, y;

        do {
            System.out.print("Введите координаты хода X и Y через пробел: ");
            x = scn.nextInt() - 1;
            y = scn.nextInt() - 1;
            if (!isCellValid(x, y))
                System.out.println("Некорректные значения X и Y для ячейки");
            else if (!isCellEmpty(x, y))
                System.out.println("Ячейка занята, выберите корректную ячейку!");
        }
        while (!isCellValid(x, y) || !isCellEmpty(x, y));

        field[x][y] = DOT_HUMAN;
    }

    /**
     * Проверка, ячейка является пустой (DOT_EMPTY)
     *
     * @param x
     * @param y
     * @return
     */
    private static boolean isCellEmpty(int x, int y) {
        return field[x][y] == DOT_EMPTY;
    }

    /**
     * Проверка корректности ввода
     * (координаты хода не должны превышать размер игрового поля)
     *
     * @param x
     * @param y
     * @return
     */
    private static boolean isCellValid(int x, int y) {
        return x >= 0 && x < fieldSizeX && y >= 0 && y < fieldSizeY;
    }

    /**
     * Обработка хода компьютера
     */
    private static void aiTurn() {
        int x, y;

        do {
            x = random.nextInt(fieldSizeX);
            y = random.nextInt(fieldSizeY);
        }
        while (!isCellEmpty(x, y));
        field[x][y] = DOT_AI;
    }

    /**
     * Проверка состояния игры
     *
     * @param c фишка игрока
     * @param s победный слоган
     * @return
     */
    private static boolean checkGameState(char c, String s) {
        if (checkWin(c)) {
            System.out.println(s);
            return true;
        }
        if (checkDraw()) {
            System.out.println("Ничья!");
            return true;
        }
        return false; // Игра продолжается
    }

    /**
     * Проверка победы
     *
     * @param c
     * @return
     */
    private static boolean checkWin(char c) {
        return checkOX(c) || checkOY(c) || checkFirstDiag(c) || checkSecondDiag(c);
    }

    /**
     * Проверка по оси Х
     * @param c
     * @return
     */
    private static boolean checkOX(char c) {
        int count = 0;
        for (int x = 0; x < fieldSizeX; x++) {
            for (int y = 0; y < fieldSizeY; y++) {
                if (field[x][y] == c) {
                    count++;
                } else {
                    count = 0;
                }
                if (count == cellsForWin) return true;
            }
        }
        return false;
    }

    /**
     * Проверка по оси Y
     * @param c
     * @return
     */
    private static boolean checkOY(char c) {
        int count;
        for (int x = 0; x < fieldSizeX; x++) {
            for (int y = 0; y < fieldSizeY; y++) {
                count = 0;
                int i = x;
                while (i < fieldSizeX) {
                    if (field[i][y] == c) {
                        count++;
                    } else {
                        count = 0;
                    }
                    i++;
                    if (count == cellsForWin) return true;
                }
            }
        }
        return false;
    }

    /**
     * Проверка по первой диагонали
     * @param c
     * @return
     */
    private static boolean checkFirstDiag(char c) {
        int count;
        for (int x = 0; x < fieldSizeX; x++) {
            for (int y = 0; y < fieldSizeY; y++) {
                count = 0;
                int i = x;
                int j = y;
                while (i < fieldSizeX && j < fieldSizeY) {
                    if (field[i][j] == c) {
                        count++;
                    } else {
                        count = 0;
                    }
                    i++;
                    j++;
                    if (count == cellsForWin) return true;
                }
            }
        }
        return false;
    }
    /**
     * Проверка по второй диагонали
     * @param c
     * @return
     */
    private static boolean checkSecondDiag(char c) {
        int count;
        for (int x = fieldSizeX - 1; x >= 0; x--) {
            for (int y = 0; y < fieldSizeY; y++) {
                count = 0;
                int i = x;
                int j = y;
                while (i >= 0 && j < fieldSizeY) {
                    if (field[i][j] == c) {
                        count++;
                    } else {
                        count = 0;
                    }
                    i--;
                    j++;
                    if (count == cellsForWin) return true;
                }
            }
        }
        return false;
    }

    /**
     * Проверка на ничью
     *
     * @return
     */
    private static boolean checkDraw() {
        for (int x = 0; x < fieldSizeX; x++) {
            for (int y = 0; y < fieldSizeY; y++) {
                if (isCellEmpty(x, y)) return false;
            }
        }
        return true;
    }
}
