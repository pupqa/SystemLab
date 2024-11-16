import java.io.*;
import java.nio.file.*; //работа с файлами и путями

public class Main {
    public static void main(String[] args) {
        String inputFilePath = "src/input.txt"; //входной файл из которого считываем символы
        StringBuilder output = new StringBuilder(); //объект StringBuilder для формирования выходного текста
        try { //try для обработки возможных исключений

            String text = new String(Files.readAllBytes(Paths.get(inputFilePath))); //считываем содержимое файла в строку

            int totalChars = text.length(); //общее количество символов в тексте
            int charsWithoutSpaces = text.replace(" ", "").length(); //количество символов без пробелов
            int wordCount = text.isEmpty() ? 0 : text.trim().split("\s+").length; //количество слов
            String[] paragraphs = text.split(" {2,}"); //разделяем текст на абзацы по двум пробелам
            int paragraphCount = paragraphs.length; //количество абзацев
            int lineCount = text.split("\n").length; //количество строк
            String[] pages = text.split("\u000c"); //разделяем текст по символу форматной страницы
            int pageCount = pages.length; //количество страниц по символу '\u000c'

            output.append("Количество символов в тексте: ").append(totalChars).append("\n")
                    .append("Количество символов без пробелов: ").append(charsWithoutSpaces).append("\n")
                    .append("Количество слов: ").append(wordCount).append("\n")
                    .append("Количество абзацев: ").append(paragraphCount).append("\n")
                    .append("Количество строк: ").append(lineCount).append("\n")
                    .append("Количество страниц: ").append(pageCount).append("\n");

            System.out.println(output.toString()); //вывод результата в консоль
            Files.write(Paths.get("src/outputya.svc"), output.toString().getBytes()); //запись результата в выходной svc файл
            Files.write(Paths.get("src/output.txt"), output.toString().getBytes()); //запись результата в выходной txt файл

        } catch (NoSuchFileException e) { //если файл не найден
            System.err.println("Ошибка: Файл не найден: " + inputFilePath); //сообщение об ошибке
        } catch (IOException e) { //обработка других ошибок ввода вывода
            e.printStackTrace(); //вывод стека ошибок
        }
    }
}
//3 абзаца потому что засчитывается также начальная строка засчитвается за абзац
// засчитывается за новую страницу