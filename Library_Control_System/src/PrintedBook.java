public class PrintedBook extends Book{
    protected static int numberOfPrintedBooks;
    public PrintedBook() {
        bookId = ++bookCounter;
        numberOfPrintedBooks++;
    }

}
