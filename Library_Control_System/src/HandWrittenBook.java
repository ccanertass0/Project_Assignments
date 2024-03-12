public class HandWrittenBook extends Book{
    protected static int numberOfHandWrittenBooks;

    public HandWrittenBook() {
        bookId = ++bookCounter;
        numberOfHandWrittenBooks++;
    }


}
