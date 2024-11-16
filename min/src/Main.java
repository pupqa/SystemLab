import java.io.IOException;
import java.nio.file.*;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.*;
public class Main
{
    private static final Set<String> RESERVED_WORDS = Set.of(
            "abstract", "assert", "boolean", "break", "byte", "case", "catch",
            "char", "class", "const", "continue", "default", "do", "double",
            "else", "enum", "extends", "final", "finally", "float", "for",
            "goto", "if", "implements", "import", "instanceof", "int",
            "interface", "long", "native", "new", "package", "private",
            "protected", "public", "return", "short", "static", "strictfp",
            "super", "switch", "synchronized", "this", "throw", "throws",
            "transient", "try", "void", "volatile", "while"
    );
    public static void main(String[] args)
    {
        Path inputPath = Path.of("c://Системка//file1.java");
        Path outputPath = Path.of("c://Систмка//file2.java");
        try
        {
            String minimizedCode = Files.readString(inputPath)
                    .transform(Main::removeComments)
                    .transform(Main::minimizeCode);
            Files.writeString(outputPath, minimizedCode);
            System.out.println("Файл " + outputPath + " успешно создан.");
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
    private static String removeComments(String code)
    {
        return code.replaceAll("/\\*[^*]*(?:\\*(?!/)[^*]*)*\\*/", "")
                .replaceAll("//.*", "");
    }
    private static String minimizeCode(String code)
    {
        Set<String> identifiers = extractIdentifiers(code);
        Map<String, String> mapping = createMapping(identifiers);
        return mapping.entrySet().stream()
                .reduce(code,
                        (c, e) -> c.replaceAll("\\b" + e.getKey() + "\\b", e.getValue()),
                        (a, b) -> b)
                .replaceAll("\\s+", " ")
                .replaceAll("\\s*([{}();=+\\-*/<>])\\s*", "$1")
                .trim();
    }
    private static Set<String> extractIdentifiers(String code)
    {
        return Pattern.compile("\\b[a-zA-Z_][a-zA-Z0-9_]*\\b")
                .matcher(code)
                .results()
                .map(m -> m.group())
                .filter(id -> !RESERVED_WORDS.contains(id))
                .collect(Collectors.toSet());
    }
    private static Map<String, String> createMapping(Set<String> identifiers)
    {
        String[] shortNames = generateNames(identifiers.size());
        return IntStream.range(0, identifiers.size())
                .boxed()
                .collect(Collectors.toMap(
                        i -> identifiers.toArray(new String[0])[i],
                        i -> shortNames[i]
                ));
    }
    private static String[] generateNames(int count)
    {
        return Stream.concat(
                        IntStream.rangeClosed('a', 'z')
                                .mapToObj(c -> String.valueOf((char)c)),
                        IntStream.rangeClosed('a', 'z')
                                .boxed()
                                .flatMap(c -> IntStream.rangeClosed('a', 'z')
                                        .mapToObj(d -> String.valueOf((char)(int)c) + (char)d))
                )
                .limit(count)
                .toArray(String[]::new);
    }
}
