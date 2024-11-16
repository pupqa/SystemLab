class AnimalThread extends Thread {
    private final String name; //имя животного
    private int priority; //приоритет выполнения потока
    private static final int DISTANCE_TO_RUN = 100; //дистанция которую должны пробежать животные
    public AnimalThread(String name, int priority) { //конструктор для инициализации имени и приоритета
        this.name = name; //сохраняем имя животного
        this.priority = priority; //сохраняем приоритет выполнения потока
        setName(name); //устанавливаем имя потока
        setPriority(priority); //устанавливаем приоритет потока
    }
    @Override
    public void run() { //метод, который выполняется, когда поток запускается
        int distance = 0; //начальное расстояние, которое пробежали животные
        while (distance < DISTANCE_TO_RUN) { //пока расстояние меньше заданной дистанции
            distance += (int)(Math.random() * 10) + 1; //добавляем случайное расстояние от 1 до 10 метров
            System.out.println(name + " пробежал: " + distance + " метров. Приоритет: " + priority); //сколько метров пробежало животное
            try {
                sleep(100); //пауза 100 миллисекунд
            } catch (InterruptedException e) { //если поток прервали
                Thread.currentThread().interrupt(); //устанавливаем флаг прерывания
            }
            if (distance < DISTANCE_TO_RUN / 2) { //проверка, если пробежали меньше половины дистанции
                setPriority(Thread.MAX_PRIORITY); //повышаем приоритет чтобы животное быстрее двигалось
            } else {
                setPriority(Thread.MIN_PRIORITY); //уменьшаем приоритет если дистанция больше половины
            }
        }
        System.out.println(name + " финишировал!"); //животное достигло сотки
    }
}
public class RabbitAndTurtle {
    public static void main(String[] args) { //основной метод
        AnimalThread rabbit = new AnimalThread("Кролик", Thread.NORM_PRIORITY); //создание потока для кролика
        AnimalThread turtle = new AnimalThread("Черепаха", Thread.NORM_PRIORITY); //создание потока для черепахи
        rabbit.start(); //запуск потока для кролика
        turtle.start(); //запуск потока для черепахи
    }
}
