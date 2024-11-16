import java.util.Scanner;

/**
 * Главный класс программы, который вычисляет значение математического выражения
 * на основе вводимого пользователем натурального числа n.
 * Программа суммирует элементы выражения ∑ (2^i / i!) от i = 2 до n.
 */
public class Main {

    /**
     * Главный метод программы. Он служит точкой входа для выполнения приложения.
     *
     * @param args аргументы командной строки (не используются в данной программе).
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Введите n: ");
        int n = scanner.nextInt();
        double x = 2.0;
        double result = 0;

        for (int i = 2; i <= n; i++) {
            result += Math.pow(x, i) / factorial(i);
        }

        System.out.println("Значение выражения: " + result);
    }

    /**
     * Метод для вычисления факториала заданного неотрицательного целого числа n.
     *
     * @param n целое число, для которого необходимо вычислить факториал.
     *          Если значение n отрицательное, метод возвращает 0.
     * @return факториал числа n. Если n <= 1, возвращает 1 (это определение
     *         факториала для 0 и 1).
     */
    private static double factorial(int n) {
        if (n <= 1) {
            return 1;
        } else if (n < 0) {
            return 0;
        } else {
            return n * factorial(n - 1);
        }
    }
}
