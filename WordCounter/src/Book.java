
/**
 * A class of Book.
 *
 * @author Miaoyan Zhang
 * @version Oct 3rd, 2016
 */

import java.util.Scanner;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Book
{
    private int lines;
    private List<String> contentList = new ArrayList<String>();

    /**
     * Constructor for objects of class Book
     */
    public Book(String fileName)
    {
        readBook(fileName);
    }

    /**
     * A mothod to read the content of the book and count the total lines
     *
     * @param  fileName   the name of the book to be read
     */
    public void readBook(String fileName) {
        lines = 0;
        try {

            Scanner file = new Scanner(new File(fileName));
            while (file.hasNextLine()) {
                contentList.add(file.nextLine().toLowerCase());
                lines++;
            }
            file.close();
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    /**
     * A mothod to get the total lines the book
     *
     * @return     the lines of the book
     */
    public int getLines()
    {
        return this.lines;
    }

    /**
     * A mothod to read the nth line of the book
     *
     * @param  nthLine   the nth line
     * @return     the content of the nth line
     */
    public String getNthLine(int nthLine)
    {
        return contentList.get(nthLine);
    }


}
