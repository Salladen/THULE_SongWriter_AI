import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Scanner;
import java.util.regex.*;

public class RawData_2_TrainingData {
    public static void main(String[] args) throws IOException {
        Scanner reader;
        File wordlist_file;
        File regString_file;
        // Read file that contains the AI's Swedish corpus
        try {
            wordlist_file = new File("Data/swe_wordlist.txt");
            regString_file = new File("Data/regexString");
            reader = new Scanner(wordlist_file);
        }
        catch (FileNotFoundException | NullPointerException e) {
            throw new IOException();
        }

        // Add all the words to a list of strings
        ArrayList<String> wordlist = new ArrayList<>(430000);
        while (reader.hasNextLine()) {
            wordlist.add(reader.nextLine());
        }
        Comparator<String> cmp = Comparator
                .comparing(String::length).reversed();
        wordlist.sort(cmp);

        reader = new Scanner(regString_file);

        String regexString = reader.nextLine();

        Pattern regPattern = Pattern.compile(regexString, Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CHARACTER_CLASS);
        for (var match : regPattern.matcher("satellit ungarnas måne springer").results().toList()){
            System.out.println("satellit ungarnas måne springer".substring(match.start(), match.end()));
        }
    }
}
