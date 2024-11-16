import java.util.Scanner;
public class Main {

    //с помощью битовых операций
    public static int Bit(int a, int b) {
        int result = 0;
        while (b > 0) { //пока b больше 0
            // если b нечетное, добавляем a к результату
            if ((b & 1) == 1) { //если последний бит b равен 1
                result += a; //добавляем a к результату
            }
            //умножаем a на 2 (сдвигаем влево)
            a <<= 1; //сдвиг a влево на 1 бит
            //делим b на 2 (сдвигаем вправо)
            b >>= 1; //сдвиг b вправо на 1 бит
        }
        return result;
    }

    //через массив
    public static int Massiv(int a, int b) {
        int result = 0; //инициализация результата
        //массив для значений
        int[] mass = new int[b]; //создаем массив размером b
        for (int i = 0; i < b; i++) { //цикл по количеству элементов в массиве
            mass[i] = a; //записываем значение a в массив
        }
        for (int i = 0; i < b; i++) { //цикл по массиву для сложения
            result += mass[i]; //складываем значения в массиве
        }
        return result;
    }

    //рекурсивное
    public static int Recursion(int a, int b) {
        // базовый случай
        if (b == 0) { //если b равно 0
            return 0;
        }
        if (b < 0) { //если b отрицательное
            return Recursion(a, -b); //умножаем a на -b
        }
        //рекурсивный случай
        return a + Recursion(a, b - 1); //умножаем a на b, рекурсивно уменьшая b
    }

    //с использованием циклов
    public static int Loop(int a, int b) {
        int result = 0; //инициализация результата
        for (int i = 0; i < b; i++) { //цикл, который добавляет 'a', 'b' единицу
            result += a; //добавляем a к результату
        }
        return result;
    }

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);

        System.out.println("введите число a:");
        int a = in.nextInt();
        System.out.println("введите число b:");
        int b = in.nextInt();
        System.out.println("выберите способ умножения:");
        System.out.println("1. Умножение через битовые операции");
        System.out.println("2. Умножение через массив");
        System.out.println("3. Рекурсивное умножение");
        System.out.println("4. Умножение с использованием циклов");
        int select = in.nextInt();
        int result = 0;
        switch (select) {
            case 1:
                result = Bit(a, b); //битовое умножение
                break;
            case 2:
                result = Massiv(a, b); //умножение через массив
                break;
            case 3:
                result = Recursion(a, b); //рекурсивное умноженияе
                break;
            case 4:
                result = Loop(a, b); //умножение с использованием циклов
                break;
            default:
                System.out.println("некорректный выбор.");
                return;
        }
        System.out.println("результат: " + result);
    }
}
