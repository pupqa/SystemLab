import java.io.IOException;
import java.nio.file.*;
class Main {
    public static void main(String[] args) throws IOException {
        String s = Files.readString(Path.of("src/fl.txt"));
        int max = 0, cur = 0;
        for (int i = 0; i < s.length() - 1; i++) {
            if ((s.charAt(i) == 'A' || s.charAt(i) == 'C') && s.charAt(i + 1) == 'B') {
                cur++;
                i++;
            } else {
                max = Math.max(max, cur);
                cur = 0;
            }
        }
        System.out.println("Число пар: " + Math.max(max, cur));
    }
}