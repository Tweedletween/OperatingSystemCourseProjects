
/**
 * A class of thread to count the words.
 *
 * @author Miaoyan Zhang
 * @version Oct 3rd, 2016
 */

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;


public class CounterThread extends Thread
{
    private int startLine;
    private int linesToRead;
    private Map<String, Integer> wordsPerSegment = new HashMap<>();
    public Map<String, Integer> sortedWordsMap = new TreeMap<>();
    Book bookToRead;

    /**
     * Constructor for objects of class CounterThread
     */
    public CounterThread(int nth, int lines, Book book1) {
        this.startLine = nth;
        this.linesToRead = lines;
        this.bookToRead = book1;
    }

    /**
     * Count the words in the thread
     *
     */
    public void run() {
        int nthLine = startLine;
        int linesBeenRead = 0;
        String wordkey = "";
        int wordIndex = 0;

        System.out.println(this.getName() + "Starts.");

        while ( nthLine < bookToRead.getLines() && linesBeenRead < linesToRead) {
            String Text = bookToRead.getNthLine(nthLine);
            for (int i = 0; i < Text.length(); i++) {
                if (Text.charAt(i) >= 'a' && Text.charAt(i) <= 'z') {
                    wordkey += Text.charAt(i);
                    wordIndex++;
                    if (i == Text.length()-1) {
                        addToMap(wordkey);
                        wordIndex = 0;
                        wordkey = "";
                    }
                }
                else {
                    if (wordIndex > 0) {
                        addToMap(wordkey);
                        wordIndex = 0;
                        wordkey = "";
                    }
                }
            }

            nthLine++;
            linesBeenRead++;

        }

        //put all the elements in wordsPerSegment to sortedWordsMap, to sort the map by key
        sortedWordsMap.putAll(wordsPerSegment);

        //Print the word frequency generated by each thread, order by key
        for(Map.Entry<String, Integer> entry : sortedWordsMap.entrySet()) {
            System.out.println(this.getName() + " - " + entry.getKey() + ": " + entry.getValue());
        }
    }

    /**
     * A mothod to add the keyword to the Map wordsPerSegment
     *
     * * @param  wordkey   the word to be added to the Map wordsPerSegment
     */
    public void addToMap(String wordkey)
    {
        if (wordsPerSegment.containsKey(wordkey)) {
            int tmpFren = wordsPerSegment.get(wordkey);
            wordsPerSegment.remove(wordkey);
            wordsPerSegment.put(wordkey, tmpFren + 1);
        }
        else {
            if (wordkey.endsWith("\'s")) {
                wordkey = wordkey.substring(0, wordkey.length() - 2);
            }
            wordsPerSegment.put(wordkey, 1);
        }
    }

    /**
     * A mothod to get the address of the Map generated by the thread
     *
     * @return     the address of the Map generated by the thread
     */
    public Map<String, Integer> getSortedWordsMap()
    {
        return sortedWordsMap;
    }

}
