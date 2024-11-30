import java.util.*;
public class Main
{
    static boolean[][] board = new boolean[9][9]; //двумерный массив для хранения доски размером 9x9
    static int[] dx = { //массив смещений по строкам для проверки соседних клеток
            0, 0, 1, -1
    };
    static int[] dy = { //массив смещений по столбцам для проверки соседних клеток
            1, -1, 0, 0
    };
    public static void main(String[] args)
    {
        Scanner scanner = new Scanner(System.in);
        int N = scanner.nextInt(); //считывание количества выпиленных клеток
        for (int i = 0; i < N; i++) //цикл для считывания координат выпиленных клеток
        {
            int row = scanner.nextInt(); //номер строки
            int col = scanner.nextInt(); //номер столбца
            board[row][col] = true; //отметка выпиленной клетки на доске
        }
        int perimeter = 0; //переменная для периметра
        for (int i = 1; i <= 8; i++) //проход по всем строкам доски
        {
            for (int j = 1; j <= 8; j++) //проход по всем столбцам доски
            {
                if (board[i][j]) //если клетка выпилена
                {
                    perimeter += countExposedSides(i, j); //добавка к периметра количества открытых сторон клетки
                }
            }
        }
        System.out.println(perimeter);
    }
    static int countExposedSides(int row, int col) //подсчет открытых сторон клетки
    {
        int exposedSides = 0; //счетчик открытых сторон
        for (int i = 0; i < 4; i++) //проверка на все четыре стороны клетки
        {
            int newRow = row + dx[i]; //координата строки соседней клетки
            int newCol = col + dy[i]; //координата столбца соседней клетки
            if (newRow < 1 || newRow > 8 || newCol < 1 || newCol > 8 || !board[newRow][newCol]) //если соседняя клетка за пределами доски или не выпилена
            {
                exposedSides++; //увеличение счетчика открытых сторон
            }
        }
        return exposedSides; //возврат количества открытых сторон
    }
}