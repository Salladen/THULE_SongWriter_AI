import info.debatty.java.stringsimilarity.Levenshtein;
import info.debatty.java.stringsimilarity.RatcliffObershelp;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InvalidObjectException;
import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.util.*;

public class pick_word {
    public static String pick(List<String> corpus, String targetWord){
        RatcliffObershelp ro = new RatcliffObershelp();
        Levenshtein ls = new Levenshtein();

        String picked_word = "";
        double current_ro = Double.POSITIVE_INFINITY;
        double current_ls = Double.POSITIVE_INFINITY;

        for (String word : corpus) {
            // Add [Levenshtein distance, Ratcliff Obershelp distance, The word]
            double temp_ls = ls.distance(targetWord, word);
            double temp_ro = ro.distance(targetWord, word);
            if (temp_ls < current_ls){
                current_ls = temp_ls;
                picked_word = word;

                if (temp_ro < current_ro){
                    current_ro = temp_ro;
                    picked_word = word;
                }
            }
        }

        return picked_word;
    }

    public static void main(String[] args) throws IOException {
        Scanner reader;
        Scanner inputReader = new Scanner(System.in);
        File wordlist_file;
        RatcliffObershelp ro = new RatcliffObershelp();
        Levenshtein ls = new Levenshtein();
        ThreadMXBean TB = ManagementFactory.getThreadMXBean();

        // Read file that contains the AI's Swedish corpus
        try {
            wordlist_file = new File("Data/swe_wordlist.txt");

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

        System.out.print("Input a Swedish word: ");
        String targetWord = inputReader.nextLine();

        // This is where all the words along with their similarity value to targetWord will be stored
        ArrayList<Object[]> wordDistances = new ArrayList<>(430000);

        long start = TB.getCurrentThreadCpuTime();

        for (String word : wordlist) {
            // Add [Levenshtein distance, Ratcliff Obershelp distance, The word]
            wordDistances.add(new Object[]{ls.distance(targetWord, word), ro.distance(targetWord, word), word});
        }

        long end = TB.getCurrentThreadCpuTime();
        double time = (end - start) / Math.pow(10, 9);
        System.out.printf("Calculating word distances took %.3f seconds \n", time);

        //Sort all of the word distances in ascending order by Levenshtein distance firstly and Ratlciff secondly.
        start = TB.getCurrentThreadCpuTime();
        Comparator<Object[]> cmp = Comparator
                .comparing((Object[] obj)-> (double) obj[0])
                .thenComparing((Object[] obj)-> (double) obj[1]);
        wordDistances.sort(cmp);
        end = TB.getCurrentThreadCpuTime();
        time = (end - start) / Math.pow(10, 9);

        System.out.printf("Sorting word distances took %.3f seconds \n", time);

        //Write out the top 10 most similar words according to the algorithm
        for(Object[] e : wordDistances.subList(0,10)){
            System.out.printf("""
                    
                    ----------------
                    Lev dist: %.2f
                    RatOb dist: %.2f
                    Word: %s
                    ----------------
                    """, (double) e[0], (double) e[1], e[2]);
        }
    }
}
