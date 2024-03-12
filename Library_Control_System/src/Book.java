import java.time.LocalDateTime;

public class Book {
    protected static int bookCounter;  // counts how many book is created, very useful for giving books their ids
    protected int bookId;
    private LocalDateTime borrowDate;  // borrowed at this date
    private LocalDateTime returnDate;  // should be returned at this date
    private boolean isBorrowed;
    private boolean isReadAtLibrary;  // is currently being read at the library?
    private Member currentBorrower;   // it is borrowed by whom?
    private boolean isExtended; // if a book is currently borrowed, this boolean shows if its returning date is extended by the current borrower.
                                // if true it is extended. else, not.


    public int getBookId() {
        return bookId;
    }
    public LocalDateTime getBorrowDate() {
        return borrowDate;
    }

    public void setBorrowDate(LocalDateTime borrowDate) {
        this.borrowDate = borrowDate;
    }

    public boolean isBorrowed() {
        return isBorrowed;
    }

    public void setBorrowed(boolean borrowed) {
        isBorrowed = borrowed;
    }

    public boolean isExtended() {
        return isExtended;
    }

    public void setExtended(boolean extended) {
        isExtended = extended;
    }

    public Member getCurrentBorrower() {
        return currentBorrower;
    }

    public void setCurrentBorrower(Member currentBorrower) {
        this.currentBorrower = currentBorrower;
    }

    public LocalDateTime getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(LocalDateTime returnDate) {
        this.returnDate = returnDate;
    }

    public boolean isReadAtLibrary() {
        return isReadAtLibrary;
    }

    public void setReadAtLibrary(boolean readAtLibrary) {
        isReadAtLibrary = readAtLibrary;
    }
}
