import java.util.Random;
import java.util.stream.IntStream;
public class Main
{
    public static void main(String[] args)
    {
        SequenceGenerator generator = new SequenceGenerator(10000);
        int[] sequence = generator.generateSequence();
        NumberDisplayer.display(sequence);
        SquareSumCalculator calculator = new SquareSumCalculator(sequence);
        System.out.println("Максимальная сумма квадратов: " + calculator.findMaxSquareSum());
    }
}
class SequenceGenerator
{
    private final int size;
    public SequenceGenerator(int size)
    {
        this.size = size;
    }
    public int[] generateSequence()
    {
        return new Random()
                .ints(size, -100, 101)
                .toArray();
    }
}
class NumberDisplayer
{
    private static final int NUMBERS_PER_LINE = 20;
    public static void display(int[] numbers)
    {
        System.out.println("Сгенерированные случайные числа:");
        IntStream.range(0, numbers.length)
                .forEach(i ->
                {
                    System.out.print(numbers[i] + " ");
                    if ((i + 1) % NUMBERS_PER_LINE == 0) System.out.println();
                }
                );
        System.out.println("\n");
    }
}
class SquareSumCalculator
{
    private final int[] sequence;
    private static final int GAP = 10;
    public SquareSumCalculator(int[] sequence)
    {
        this.sequence = sequence;
    }
    public int findMaxSquareSum()
    {
        return IntStream.range(0, sequence.length - GAP)
                .flatMap(i -> IntStream.range(i + GAP, sequence.length)
                        .map(j -> sequence[i] * sequence[i] + sequence[j] * sequence[j]))
                .max()
                .orElse(0);
    }
}