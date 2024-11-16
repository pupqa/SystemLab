import java.io.*;
import java.util.List;
import java.util.concurrent.*;

public class Main {

    // Метод для копирования файла с использованием потоков
    private static void copyFileUsingStream(File source, File dest) throws IOException {
        InputStream is = null; //объявляем переменную для входного потока (чтение)
        OutputStream os = null; //объявляем переменную для выходного потока (запись)
        try {
            is = new FileInputStream(source); //создание входного потока для чтения из исходного файла
            os = new FileOutputStream(dest); //создание выходного потока для записи в целевой файл
            byte[] buffer = new byte[1024]; // буфер для хранения данных, считываемых из файла
            int length; //переменная для хранения количества считанных байтов
            while ((length = is.read(buffer)) > 0) { // Чтение данных из входного потока
                os.write(buffer, 0, length); //запись данных в выходной поток
            }
        } finally {
            if (is != null) {
                is.close(); //закрываем входной поток, если он открыт
            }
            if (os != null) {
                os.close(); //закрываем выходной поток, если он открыт
            }
        }
    }

    //метод для последовательного копирования файлов
    public static void sequentialCopy() {
        //объявление файлов для исходного и целевого
        File sFile1 = new File("src\\sFile1.txt");
        File dFile1 = new File("src\\dFile1.txt");
        File sFile2 = new File("src\\sFile2.txt");
        File dFile2 = new File("src\\dFile2.txt");

        long startTime = System.nanoTime(); //запоминаем время начала копирования
        try {
            //копирование файлов с использованием метода copyFileUsingStream
            copyFileUsingStream(sFile1, dFile1);
            copyFileUsingStream(sFile2, dFile2);
        } catch (IOException e) {
            e.printStackTrace(); //обработка исключений при работе с файлами
        }
        long endTime = System.nanoTime(); //запоминаем время окончания копирования
        //выводим время, затраченное на последовательное копирование
        System.out.println("Время последовательного копирования: " + (endTime - startTime) + " нс");
    }

    //метод для параллельного копирования файлов
    public static void parallelCopy() {
        //объявление файлов для исходного и целевого
        File sFile1 = new File("src\\sFile1.txt");
        File dFile1 = new File("src\\dFile1.txt");
        File sFile2 = new File("src\\sFile2.txt");
        File dFile2 = new File("src\\dFile2.txt");

        //создаем пул потоков с фиксированным количеством потоков
        ExecutorService executor = Executors.newFixedThreadPool(2);
        long startTime = System.nanoTime(); // Запоминаем время начала копирования

        try {
            //запускаем задачи копирования параллельно
            executor.invokeAll(List.of(
                    Executors.callable(() -> { //первая задача для копирования sFile1
                        try {
                            copyFileUsingStream(sFile1, dFile1);
                        } catch (IOException e) {
                            throw new RuntimeException(e); //обработка исключений
                        }
                    }),
                    Executors.callable(() -> { //вторая задача для копирования sFile2
                        try {
                            copyFileUsingStream(sFile2, dFile2);
                        } catch (IOException e) {
                            throw new RuntimeException(e); //обработка исключений
                        }
                    })
            ));
        } catch (InterruptedException e) {
            e.printStackTrace(); //обработка прерывания потока
        } finally {
            executor.shutdown(); //остановка пула потоков
        }

        long endTime = System.nanoTime(); //запоминаем время окончания копирования
        //выводим время, затраченное на параллельное копирование
        System.out.println("Время параллельного копирования: " + (endTime - startTime) + " нс");
    }

    public static void main(String[] args) { //копирование одного файла
        File sourceFile = new File("src\\source.txt"); //исходный файл
        File destFile = new File("src\\destination.txt"); //целевой файл
        long copyStartTime = System.nanoTime(); //запоминаем время начала копирования
        try {
            copyFileUsingStream(sourceFile, destFile); //копируем файл с использованием метода copyFileUsingStream
            long copyEndTime = System.nanoTime(); //запоминаем время окончания копирования
            System.out.println("Файл успешно скопирован. Время копирования: " + (copyEndTime - copyStartTime) + " нс");
        } catch (IOException e) {
            e.printStackTrace(); //обработка исключений
        }
        sequentialCopy(); //последовательное копирование
        parallelCopy(); //параллельное копирование
    }
}
