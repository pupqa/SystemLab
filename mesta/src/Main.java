import java.io.*;
import java.util.*;
public class Main
{
    public static void main(String[] args)
    {
        String inputFileName = "src/input.txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(inputFileName)))
        {
            int N = Integer.parseInt(reader.readLine()); //читаем количество занятых мест
            TreeMap<Integer, TreeSet<Integer>> occupiedSeats = new TreeMap<>(); //TreeMap для автоматической сортировки рядов
            for (int i = 0; i < N; i++) //читаем и сохраняем занятые места
            {
                String[] input = reader.readLine().split(" ");
                int row = Integer.parseInt(input[0]);
                int seat = Integer.parseInt(input[1]);
                occupiedSeats.computeIfAbsent(row, k -> new TreeSet<>()).add(seat); //TreeSet для автоматической сортировки мест в ряду
            }
            Result result = findBestSeats(occupiedSeats);
            if (result.row != -1) //результат
            {
                System.out.println(result.row + " " + result.seat);
            }
            else
            {
                System.out.println("No suitable seats found.");
            }

        }
        catch (IOException e)
        {
            System.err.println("Error reading file: " + e.getMessage());
        }
    }
    private static class Result //для хранения результата
    {
        int row;
        int seat;
        Result(int row, int seat)
        {
            this.row = row;
            this.seat = seat;
        }
    }
    private static Result findBestSeats(TreeMap<Integer, TreeSet<Integer>> occupiedSeats)
    {
        Result bestResult = new Result(-1, Integer.MAX_VALUE);
        for (Map.Entry<Integer, TreeSet<Integer>> entry : occupiedSeats.entrySet()) //проход по каждому ряду
        {
            int currentRow = entry.getKey();
            TreeSet<Integer> seatsInRow = entry.getValue();
            List<Integer> seatsList = new ArrayList<>(seatsInRow); //TreeSet в список для удобства доступа
            for (int i = 0; i < seatsList.size() - 1; i++) //проверка последовательности мест
            {
                int currentSeat = seatsList.get(i);
                int nextSeat = seatsList.get(i + 1);
                if (nextSeat == currentSeat + 1) //проверка, являются ли места последовательными
                {
                    boolean hasLeftNeighbor = seatsInRow.contains(currentSeat - 1); //проверка наличия занятых мест слева и справа
                    boolean hasRightNeighbor = seatsInRow.contains(nextSeat + 1);
                    if (hasLeftNeighbor && hasRightNeighbor)
                    {
                        if (currentRow > bestResult.row ||
                                (currentRow == bestResult.row &&
                                        currentSeat < bestResult.seat)) //обновление результата, если текущий ряд лучше или если это тот же ряд, но место меньше
                        {
                            bestResult = new Result(currentRow, currentSeat);
                        }
                    }
                }
            }
        }
        return bestResult;
    }
}
