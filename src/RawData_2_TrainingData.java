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
        File song_file;

        // Read file that contains the AI's Swedish corpus
        try {
            wordlist_file = new File("Data/swe_wordlist_v2.txt");
            regString_file = new File("Data/regexString");
            song_file = new File("Data/Songs/1.txt");
            reader = new Scanner(wordlist_file);
        }
        catch (FileNotFoundException | NullPointerException e) {
            throw new IOException();
        }

        // Add all the words to a list of strings
        ArrayList<String> wordlist = new ArrayList<>(430000);
        wordlist.add(" ");
        wordlist.add("\n");
        while (reader.hasNextLine()) {
            wordlist.add(reader.nextLine());
        }
        Comparator<String> cmp = Comparator
                .comparing(String::length).reversed();
        wordlist.sort(cmp);

        reader = new Scanner(regString_file);

        String regexString = reader.nextLine();
        Pattern regPattern = Pattern.compile(regexString, Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CHARACTER_CLASS);

        reader = new Scanner(song_file);
        ArrayList<String> songLines = new ArrayList<String>();

        while (reader.hasNextLine()){
            songLines.add(reader.nextLine());
        }
        songLines.remove(0);
        songLines.remove(0);
        songLines.remove(0);
        String song = String.join("\n", songLines);

        for (var match : regPattern.matcher(song).results().toList()) {
            String matchString = song.substring(match.start(), match.end()).toLowerCase();

            if (!wordlist.contains(matchString)) {
                System.out.printf("Match: [%s]\n", matchString);
                System.out.println("----NOT IN CORPUS----");
                System.out.printf("Replacement: [%s]\n", pick_word.pick(wordlist, matchString));
            }
        }
    }
}
