import java.util.*;

public class Main {
    public static void main(String[] args) {
        System.out.print("введите размер массива: ");
        Scanner scanner = new Scanner(System.in); //Объект сканера для считываня ввода
        int arraySize = scanner.nextInt(); //инициализация размера массива
        int[] array = new int[arraySize]; //создание массива заданного размера

        System.out.println("введите элементы массива: ");
        for (int index = 0; index < arraySize; index++) { //заполняем массив через цикл
            array[index] = scanner.nextInt(); //считывание поо индексу
        }

        System.out.println("введите степень мажоритарности (%): ");
        int Porog = scanner.nextInt(); //порог мажоритарности

        int Vhod = 0; //переменная подсчета вхождений
        int major = 0; //переменная хранения мажоритарного числа

        for (int i = 0; i < arraySize; i++) { //перебор эллементов массива ВНЕШНИЙ ЦИКЛ
            for (int j = 0; j < arraySize; j++) { //сравнение текущего эллемента с другими
                if (i != j && array[i] == array[j]) { //если индексы различны и элементы равны
                    Vhod += 1; //увеличиваем счетчик вхождений
                }
            }
            //проверяем соответствует ли количество вхождений порогу
            if (Vhod >= (arraySize * Porog) / 100) { //проверка достаточно ли количество встреч числа чтобы быть мажоритарным
                major = array[i]; //устанавливаем текущее число как мажоритарное
                i = arraySize; //выходим из внешнего цикла
            } else {
                Vhod = 0; //обнуляем счетчик вхождений
            }
        }
        System.out.println("мажоритарное число: " + major);
    }
}