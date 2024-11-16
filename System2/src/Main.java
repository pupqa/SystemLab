import java.util.*;
public class Main {
    public static void main(String[] args) {
        final int SIZE = 1000; //размер массива

        int[] nums = new int[SIZE]; //создаем массив для хранения чисел

        Random rand = new Random(); //объкт генерации сдучайеых чисел

        //заполняем массив случайными числами от 0 до 999
        for (int i = 0; i < SIZE; i++) { //цикл от 0 до 999.
            nums[i] = rand.nextInt(10001); //сохр случайных чисел в массив
        }

        //инициализация переменных для хранения минимальных чисел
        int min3 = Integer.MAX_VALUE; //минимальное число, кратное 3
        int min7 = Integer.MAX_VALUE; //минимальное число, кратное 7
        int min21 = Integer.MAX_VALUE; //минимальное число, кратное 21
        int minNeKrat = Integer.MAX_VALUE; //минимальное число, не кратное 3, 7 и 21

        //поиск минимальных чисел среди элементов массива
        for (int i = 0; i < SIZE; i++) { //проверка каждого числа в массиве
            if (nums[i] % 21 == 0 && nums[i] < min21) { //проверка кратно ли число 21 и меньше текущего min21
                min21 = nums[i]; //если да обновление min21
            }
            else if (nums[i] % 3 == 0 && nums[i] < min3) { //проверяем, кратно ли число 3 и меньше текущего min3
                min3 = nums[i]; //если да обновляем min3
            }
            else if (nums[i] % 7 == 0 && nums[i] < min7) { //проверяем, кратно ли число 7 и меньше текущего min7
                min7 = nums[i]; //если да обновляем min7
            }
            //обновляем минимальное число, если число меньше текущего
            else if (nums[i] < minNeKrat) {
                minNeKrat = nums[i];
            }
        }

        int result1 = 0; //результат для произведения кратного 3 и 7
        int result2 = 0; //результат для произведения минимального числа и кратного 21

        if (min3 != Integer.MAX_VALUE && min7 != Integer.MAX_VALUE) { //проверяем были ли найдены минимальные числа
            result1 = min3 * min7; //если минимальные числа найдены вычисляем их произведение
        }

        if (min21 != Integer.MAX_VALUE) { //проверка найдено ли минимальное число кратное 21
            result2 = min21 * Math.min(min3, Math.min(min7, minNeKrat)); //вычисляем произведение min21 и самого малого среди min3, min7 и min
        }

        int answer = Math.min(result1, result2); //определяем минимальный результат из двух возможных

        //выводим ответ или -1, если результат не найден
        if (answer != -1) {
            System.out.println("минимальное число кратное 21, и являющееся произведением 2 других чисел: "+answer);
        } else {
            System.out.println(-1);
        }
    }
}