import java.io.*;
import java.util.*;

public class CountKeywords {
    public static void main(String[] args) throws Exception {
        if (args.length != 1) {
            System.out.println("Usage: java CountKeywords <JavaSourceFile>");
            return;
        }

        String filename = args[0];
        File file = new File(filename);

        if (file.exists()) {
            System.out.println("The number of keywords in " + filename
                + " is " + countKeywords(file));
        } else {
            System.out.println("File " + filename + " does not exist");
        }
    }

    public static int countKeywords(File file) throws Exception {
        String[] keywordString = {
            "abstract", "assert", "boolean", "break", "byte", "case", "catch",
            "char", "class", "const", "continue", "default", "do", "double",
            "else", "enum", "extends", "for", "final", "finally", "float",
            "goto", "if", "implements", "import", "instanceof", "int",
            "interface", "long", "native", "new", "package", "private",
            "protected", "public", "return", "short", "static", "strictfp",
            "super", "switch", "synchronized", "this", "throw", "throws",
            "transient", "try", "void", "volatile", "while", "true", "false", "null"
        };

        Set<String> keywordSet = new HashSet<>(Arrays.asList(keywordString));
        int count = 0;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            boolean inMultiLineComment = false;
            
            while ((line = reader.readLine()) != null) {
                if (inMultiLineComment) {
                    int endIndex = line.indexOf("*/");
                    if (endIndex != -1) {
                        line = line.substring(endIndex + 2);
                        inMultiLineComment = false;
                    } else {
                        continue;
                    }
                }
                
                while (line.contains("/*")) {
                    int startIndex = line.indexOf("/*");
                    int endIndex = line.indexOf("*/", startIndex);
                    if (endIndex != -1) {
                        line = line.substring(0, startIndex) + line.substring(endIndex + 2);
                    } else {
                        line = line.substring(0, startIndex);
                        inMultiLineComment = true;
                        break;
                    }
                }
                
                if (line.contains("//")) {
                    line = line.substring(0, line.indexOf("//")).trim();
                }
                
                int startIndex = 0;
                while (startIndex < line.length()) {
                    int stringStart = line.indexOf('"', startIndex);
                    int commentStart = line.indexOf("//", startIndex);
                    int multiLineStart = line.indexOf("/*", startIndex);
                    
                    if (stringStart == -1 && commentStart == -1 && multiLineStart == -1) {
                        break;
                    }
                    
                    if (stringStart != -1 && (commentStart == -1 || stringStart < commentStart) && (multiLineStart == -1 || stringStart < multiLineStart)) {
                        int stringEnd = line.indexOf('"', stringStart + 1);
                        if (stringEnd == -1) {
                            stringEnd = line.length();
                        }
                        startIndex = stringEnd + 1;
                    } else if (commentStart != -1 && (multiLineStart == -1 || commentStart < multiLineStart)) {
                        line = line.substring(0, commentStart).trim();
                        break;
                    } else if (multiLineStart != -1) {
                        int multiLineEnd = line.indexOf("*/", multiLineStart + 2);
                        if (multiLineEnd == -1) {
                            multiLineEnd = line.length();
                        }
                        line = line.substring(0, multiLineStart) + line.substring(multiLineEnd + 2);
                        if (multiLineEnd == line.length()) {
                            inMultiLineComment = false;
                        }
                        startIndex = multiLineStart;
                    }
                }
                
                String[] words = line.split("\\W+");
                for (String word : words) {
                    if (keywordSet.contains(word)) {
                        count++;
                    }
                }
            }
        }
        return count;
    }
}
