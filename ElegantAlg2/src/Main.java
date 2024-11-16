import java.io.*;
import java.nio.file.*;
public class Main
{
    public static int findMaxSequence(String input)
    {
        int maxLen = 0, curLen = 0;
        for (int i = 0; i < input.length(); i++)
        {
            char c = input.charAt(i);
            if ("QRW124".indexOf(c) == -1)
            {
                curLen = 0;
                continue;
            }
            if (i == 0) curLen = 1;
            else
            {
                char p = input.charAt(i - 1);
                curLen = ("QRW".indexOf(p) >= 0 && "124".indexOf(c) >= 0) ||
                        ("124".indexOf(p) >= 0 && "QRW".indexOf(c) >= 0) ? curLen + 1 : 1;
            }
            maxLen = Math.max(maxLen, curLen);
        }
        return maxLen;
    }
    public static void main(String[] args) throws IOException {
        String content = Files.readString(Path.of("src/ogo.txt"));
        System.out.println("Максимальная длина последовательности: " + findMaxSequence(content));
    }
}