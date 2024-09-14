import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Stack;

public class GroupingSymbolChecker {
    
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java GroupingSymbolChecker <source-file>");
            return;
        }

        String fileName = args[0];
        
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            Stack<Character> stack = new Stack<>();
            int lineNumber = 0;
            int character;
            
            while ((character = reader.read()) != -1) {
                char ch = (char) character;

                switch (ch) {
                    case '(', '{', '[' -> stack.push(ch);
                    case ')' -> {
                        if (stack.isEmpty() || stack.pop() != '(') {
                            System.out.printf("Mismatched ')' at line %d%n", lineNumber);
                            return;
                        }
                    }
                    case '}' -> {
                        if (stack.isEmpty() || stack.pop() != '{') {
                            System.out.printf("Mismatched '}' at line %d%n", lineNumber);
                            return;
                        }
                    }
                    case ']' -> {
                        if (stack.isEmpty() || stack.pop() != '[') {
                            System.out.printf("Mismatched ']' at line %d%n", lineNumber);
                            return;
                        }
                    }
                    case '\n' -> lineNumber++;
                }
            }
            
            if (!stack.isEmpty()) {
                System.out.println("Unmatched opening symbols.");
            } else {
                System.out.println("All grouping symbols are correctly matched.");
            }
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
    }
}
