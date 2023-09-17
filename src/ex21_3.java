import java.util.*;
import java.io.*;

public class ex21_3 {
    public static void main(String[] args) throws Exception {
        if (args.length != 1) {
            System.out.println("Usage: java CountKeywords sourceFile");
            System.exit(1);
        }

        File file = new File(args[0]);
        if (file.exists()) {
            System.out.println("The number of keywords in " + args[0]
                    + " is " + countKeywords(file));
        } else {
            System.out.println("File " + args[0] + " does not exist");
        }
    }

    public static int countKeywords(File file) throws Exception {
        String[] keywordString = {"abstract", "assert", "boolean",
                // ... [rest of your keywords array]
                "while", "true", "false", "null"};

        Set<String> keywordSet = new HashSet<>(Arrays.asList(keywordString));
        int count = 0;

        Scanner input = new Scanner(file);
        boolean inBlockComment = false;
        boolean inLineComment = false;
        boolean inString = false;

        while (input.hasNextLine()) {
            String line = input.nextLine();

            for (int i = 0; i < line.length(); i++) {
                if (inBlockComment) {
                    if (line.charAt(i) == '*' && i < line.length() - 1 && line.charAt(i + 1) == '/') {
                        inBlockComment = false;
                        i++; // skip the '/' character
                    }
                } else if (inLineComment) {
                    break; // go to the next line
                } else if (inString) {
                    if (line.charAt(i) == '"') {
                        inString = false;
                    }
                } else {
                    if (line.charAt(i) == '/' && i < line.length() - 1) {
                        if (line.charAt(i + 1) == '/') {
                            inLineComment = true;
                            break; // go to the next line
                        } else if (line.charAt(i + 1) == '*') {
                            inBlockComment = true;
                            i++; // skip the '*' character
                        }
                    } else if (line.charAt(i) == '"') {
                        inString = true;
                    } else {
                        StringBuilder wordBuilder = new StringBuilder();
                        while (i < line.length() && Character.isJavaIdentifierPart(line.charAt(i))) {
                            wordBuilder.append(line.charAt(i));
                            i++;
                        }
                        if (keywordSet.contains(wordBuilder.toString())) {
                            count++;
                        }
                    }
                }
            }
            inLineComment = false; // reset the state for the next line
        }

        input.close();
        return count;
    }
}
