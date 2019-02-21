import java.util.*;
import java.nio.file.*;
import java.nio.charset.*;

public class anagramSolverHashMap {
    public static void main(String[] Args) {
        long start = System.nanoTime();
        String word = Args[1];
        String fileName = Args[0];

        //In case there are more than 2 arguments provided. This must mean, that the anagram consists of multiple words.
        if (Args.length>2) {
            for (int i=2; i<Args.length; i++) {
                word += " " + Args[i];
            }
        }

        String output = "";
        double originalWordVector = wordVector(word);
        HashMap < Character, Integer > wordHash = wordHashMap(word);
        List < String > lines = Collections.emptyList();

        try {
            lines = Files.readAllLines(Paths.get(fileName), Charset.forName("windows-1257"));
            for (String line: lines) {
                if (line.length() == word.length() && !line.equals(word)) {

                    // We'll do word vector comparisons first, as this results (on average) in ~20% decrease
                    // of total time spent compared to using only HashMaps.
                    // As vector comparison produces some collisions, then those are checked again with the HashMap character counts.
                    // If two HashMaps are equal, then we have an anagram.

                    if (wordVector(line) == originalWordVector) {
                        HashMap < Character, Integer > currentHash = wordHashMap(line);
                        if (currentHash.equals(wordHash)) {
                            output += "," + line;
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            //TODO: handle exception
        }

        long elapsedTime = System.nanoTime() - start;
        System.out.println(elapsedTime / 1000 + output);
    }

    // Calculate vector lenght of the word: square root of the sum of all character values squared.
    public static double wordVector(String word) {
        long temp = 0;
        for (char key: word.toCharArray()) {
            temp += (int) key * (int) key;
        }
        return Math.sqrt(temp);
    }

    // HashMap to store the count of all characters found in the word.
    public static HashMap < Character, Integer > wordHashMap(String word) {
        HashMap < Character, Integer > wordHash = new HashMap < Character, Integer > ();
        for (char key: word.toCharArray()) {
            if (wordHash.get(key) == null) {
                wordHash.put(key, 1);
            } else {
                wordHash.put(key, wordHash.get(key) + 1);
            }
        }
        return wordHash;
    }

}