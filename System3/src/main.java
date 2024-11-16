import java.util.*;

public class main {
    public static void main(String[] args) {
        final int kolvoPairs = 16; //количество пар
        Scanner scanner = new Scanner(System.in);

        System.out.print("Введите количество пришедших sms-сообщений: ");
        int numberOfVotes = scanner.nextInt(); //считываем введенное пользователем количество голосов (сообщений)

        int[] votes = new int[kolvoPairs + 1]; //массив для хранения голосов от 1 до 16 (0 не используется)

        System.out.println("Введите номера пар от 1 до 16:");
        for (int i = 0; i < numberOfVotes; i++) { //цикл по количеству введенных голосов
            if (scanner.hasNextInt()) { //проверка на целое число в вводе
                int pairNumber = scanner.nextInt(); //считываем введенное число как номер пары
                if (pairNumber >= 1 && pairNumber <= kolvoPairs) { //проверяем находится ли номер пары в допустимом диапазоне
                    votes[pairNumber]++; //если номер корректный увеличиваем количество голосов для соответствующей пары
                } else {
                    System.out.println("Некорректный номер пары: " + pairNumber + ", он должен быть в диапазоне от 1 до 16");
                }
            } else {
                System.out.println("Пожалуйста, введите целое число.");
                scanner.next(); //очистка неверного ввода из сканера
            }
        }

        List<PairVotes> pairVotesList = new ArrayList<>(); //список для хранения объектов PairVotes с номером пары и количеством голосов
        for (int i = 1; i <= kolvoPairs; i++) { //цикл по диапазону от 1 до 16
            pairVotesList.add(new PairVotes(i, votes[i])); //создаем новый объект PairVotes и добавляем его в список
        }

        combSort(pairVotesList); //вызов метода сортировки расческой для сортировки списка по количеству голосов в порядке убывания

        System.out.println("Результаты голосования:");
        for (PairVotes pairVotes : pairVotesList) { //цикл по заполненному списку пар с голосами
            System.out.println("пара " + pairVotes.getPairNumber() + ": " + pairVotes.getVoteCount() + " голосов");
        }
    }

    private static void combSort(List<PairVotes> pairVotesList) {
        int gap = pairVotesList.size(); //инициализация переменной gap как размера списка
        boolean swapped = true; //флаг указывающий были ли произведены обмены

        //цикл продолжается, пока gap больше 1 или были произведены обмены
        while (gap > 1 || swapped) {
            gap = (int) (gap / 1.3); //уменьшаем gap в 1.3 раза
            if (gap < 1) gap = 1; //если gap меньше 1, устанавливаем его равным 1

            swapped = false; //сброс флага обмена

            //сравниваем и меняем местами элементы
            for (int i = 0; i < pairVotesList.size() - gap; i++) {
                if (pairVotesList.get(i).getVoteCount() < pairVotesList.get(i + gap).getVoteCount()) { //сравнение количества голосов
                    //меняем местами элементы, если порядок неверный
                    PairVotes temp = pairVotesList.get(i);
                    pairVotesList.set(i, pairVotesList.get(i + gap));
                    pairVotesList.set(i + gap, temp);
                    swapped = true; //устанавливаем флаг обмена
                }
            }
        }
    }
}

class PairVotes { //класс для хранения информации о паре и количестве голосов
    private final int pairNumber; //номер пары
    private final int voteCount; //количество голосов за пару

    //конструктор, принимающий номер пары и количество голосов
    public PairVotes(int pairNumber, int voteCount) {
        this.pairNumber = pairNumber; //инициализация номера пары
        this.voteCount = voteCount; //инициализация количества голосов
    }

    public int getPairNumber() { //получение номера пары
        return pairNumber;
    }

    public int getVoteCount() { //получение количества голосов
        return voteCount;
    }
}
