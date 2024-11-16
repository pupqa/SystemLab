public class Main {
    public static void main(String[] args) {
        int money = 15, price = 1, wrap = 3;
        System.out.println("максималка чоколадок " + calculate(money, price, wrap)); //вычисление и вывод максимального количества шоколадок
    }
    public static int calculate(int money, int price, int wrap) { //метод для вычисления максимального количества шоколадок
        int chocolates = money / price, wrappers = chocolates; //определение количества шоколадок, которые можно купить, и начальное количество оберток
        while (wrappers >= wrap) { //цикл для получения дополнительных шоколадок на основе оберток
            chocolates += wrappers / wrap; //добавление шоколадок, полученных за обертки
            wrappers = wrappers / wrap + wrappers % wrap; //обновление количества оберток (остаток + полученные за обертки)
        }
        return chocolates; //возврат общего количества шоколадок
    }
}
