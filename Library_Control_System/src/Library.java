
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.Arrays;

public class Library {
    private final String inputFile; // the name of the input file (.txt file)
    private final String outputFile; // the name of the output file
    private String[] commands;  // each line of the input file is an element of this array.

    private Book[] books = new Book[0];  // each book is stored here
    private Member[] members = new Member[0]; // each member is stored here
    public Library(String inputFile, String outputFile) {
        this.inputFile = inputFile;
        this.outputFile = outputFile;
    }

    DateTimeFormatter formatter = new DateTimeFormatterBuilder()
            .appendPattern("yyyy-MM-dd[ HH:mm:ss]")
            .parseDefaulting(ChronoField.HOUR_OF_DAY, 0)
            .parseDefaulting(ChronoField.MINUTE_OF_HOUR, 0)
            .parseDefaulting(ChronoField.SECOND_OF_MINUTE, 0)
            .toFormatter();


    /**
     * This is the method where every command is understood and every action regarding these commands are taken.
     * {@link WriteOutput} and {@link ReadFile} objects necessary for input reading and output producing are created inside this method.
     * @throws IOException

     */
    public void operate() throws IOException {
        ReadFile readInputs = new ReadFile(inputFile);
        readInputs.readAndAppend();  // input file is read
        commands = readInputs.getInputArray(); // we have the commands now

        WriteOutput libraryOutput = new WriteOutput(outputFile);
        libraryOutput.createWriter();

        for(String command : commands){
            String[] commandWords = command.split("\t");
            String keyCommand = commandWords[0];   // this keyCommand is addBooker, addMember, getTheHistory etc.
            switch (keyCommand){
                case "addBook":
                    String type = commandWords[1];   // book type (P or H)
                    books = Arrays.copyOf(books, books.length + 1);
                    if(type.equals("P")){
                        books[books.length - 1] = new PrintedBook();

                    } else if (type.equals("H")) {
                        books[books.length - 1] = new HandWrittenBook();
                    }
                    libraryOutput.write("Created new book: " + (type.equals("P") ? "Printed" : "Handwritten") + " [id: " + Book.bookCounter + "]\n");

                    break;
                case "addMember":
                    String memberType = commandWords[1];  // academic or student
                    members = Arrays.copyOf(members, members.length + 1);
                    if(memberType.equals("S")){
                        members[members.length - 1] = new Student();
                    } else if (memberType.equals("A")) {
                        members[members.length - 1] = new Academic();
                    }
                    libraryOutput.write("Created new member: " + (memberType.equals("S") ? "Student" : "Academic") + " [id: " + Member.memberCounter + "]\n");
                    break;
                case "borrowBook":
                    int bookId = Integer.parseInt(commandWords[1]);
                    if(getBookObjectFromId(bookId).isBorrowed()){
                        libraryOutput.write("You cannot borrow this book! This book is already borrowed.\n");
                    }else{
                        int memberId = Integer.parseInt(commandWords[2]);
                        LocalDateTime borrowDate = LocalDateTime.parse(commandWords[3], formatter);

                        if(getMemberObjectFromId(memberId) instanceof Student){
                            if(getMemberObjectFromId(memberId).borrowedBookNum < 2){
                                getBookObjectFromId(bookId).setBorrowed(true); // book is borrowed
                                getBookObjectFromId(bookId).setBorrowDate(borrowDate); // borrowed at borrowDate
                                getBookObjectFromId(bookId).setReturnDate(borrowDate.plusDays(7)); // return date is 7 days layer
                                getBookObjectFromId(bookId).setCurrentBorrower(getMemberObjectFromId(memberId));
                                libraryOutput.write("The book [" + bookId + "] was borrowed by member [" + memberId +
                                        "] at " + borrowDate.format(formatter).substring(0,10) + "\n");
                                getMemberObjectFromId(memberId).borrowedBookNum++;
                            }else{
                                libraryOutput.write("You have exceeded the borrowing limit!\n");
                            }
                        }else {  // then academic
                            if(getMemberObjectFromId(memberId).borrowedBookNum < 4){
                                getBookObjectFromId(bookId).setBorrowed(true);  // book is borrowed
                                getBookObjectFromId(bookId).setBorrowDate(borrowDate); // borrowed at borrowDate
                                getBookObjectFromId(bookId).setReturnDate(borrowDate.plusDays(14));  // return date is 14 days later.
                                getBookObjectFromId(bookId).setCurrentBorrower(getMemberObjectFromId(memberId));  // we set the currentBorrower

                                libraryOutput.write("The book [" + bookId + "] was borrowed by member [" +
                                        memberId + "] at " + borrowDate.format(formatter).substring(0,10) + "\n");
                                getMemberObjectFromId(memberId).borrowedBookNum++;
                                libraryOutput.write("You have exceeded the borrowing limit!\n");
                            }
                        }
                    }

                    break;
                case "returnBook":
                    int bookId2 = Integer.parseInt(commandWords[1]);
                    if(!getBookObjectFromId(bookId2).isBorrowed()){
                        libraryOutput.write("This book is not borrowed. You can't return it!\n");
                    }else{
                        int memberId = Integer.parseInt(commandWords[2]);
                        LocalDateTime returnedAt = LocalDateTime.parse(commandWords[3], formatter); // the book is returned at this date.

                        if(returnedAt.isAfter(getBookObjectFromId(bookId2).getReturnDate())){
                            Duration duration;
                            long fee = 0;
                            if(getBookObjectFromId(bookId2).getReturnDate().isBefore(LocalDateTime.parse("9999-12-27", formatter))){
                                duration = Duration.between(getBookObjectFromId(bookId2).getReturnDate(), returnedAt);
                                fee = duration.toDays();
                            }

                            libraryOutput.write("The book [" + bookId2 + "] was returned by member [" +
                                    memberId + "] at " +
                                    returnedAt.format(formatter).substring(0,10) +  " Fee: " + fee + "\n");
                        }else{

                            libraryOutput.write("The book [" + bookId2 + "] was returned by member ["
                                    + memberId + "] at " +
                                    returnedAt.format(formatter).substring(0, 10) + " Fee: 0\n");

                        }

                        // every feature of the book is set to default below when book is returned.
                        getBookObjectFromId(bookId2).setBorrowed(false);
                        getBookObjectFromId(bookId2).setCurrentBorrower(null);
                        getBookObjectFromId(bookId2).setBorrowDate(null);
                        getBookObjectFromId(bookId2).setReturnDate(null);
                        getBookObjectFromId(bookId2).setExtended(false);
                        getBookObjectFromId(bookId2).setReadAtLibrary(false);
                        getMemberObjectFromId(memberId).borrowedBookNum--;
                    }
                    break;
                case "extendBook":
                    int bookId3 = Integer.parseInt(commandWords[1]);
                    if(!getBookObjectFromId(bookId3).isBorrowed()){
                        libraryOutput.write("This book is not borrowed, you can't extend the deadline\n");
                    }else{
                        int memberId = Integer.parseInt(commandWords[2]);
                        LocalDateTime currentDate = LocalDateTime.parse(commandWords[3],formatter);
                        if(currentDate.isAfter(getBookObjectFromId(bookId3).getReturnDate())){  // might be extended
                            libraryOutput.write("You can not extend the deadline you should have come at least at the last day of your returning date!\n");
                        }else{
                            if(getBookObjectFromId(bookId3).isExtended()){
                                libraryOutput.write("You cannot extend the deadline!\n");
                            }else{
                                getBookObjectFromId(bookId3).setExtended(true);
                                getBookObjectFromId(bookId3).setReturnDate(currentDate.plusDays(7));
                                libraryOutput.write("The deadline of book [" + bookId3 +
                                        "] was extended by member [" + memberId + "] at " + currentDate.format(formatter).substring(0,10) + "\n");
                                libraryOutput.write("New deadline of book [" + bookId3 +
                                        "] is " + getBookObjectFromId(bookId3).getReturnDate().format(formatter).substring(0, 10) + "\n");
                            }
                        }
                    }

                    break;
                case "readInLibrary":
                    int bookId4 = Integer.parseInt(commandWords[1]);
                    if(getBookObjectFromId(bookId4).isBorrowed()){
                        libraryOutput.write("You can not read this book!\n");
                    }else{      // if not borrowed
                        int memberId = Integer.parseInt(commandWords[2]);
                        LocalDateTime currentDate = LocalDateTime.parse(commandWords[3], formatter);
                        if(getBookObjectFromId(bookId4) instanceof  HandWrittenBook){
                            if(getMemberObjectFromId(memberId) instanceof Student){
                                libraryOutput.write("Students can not read handwritten books!\n");
                            }else{
                                getBookObjectFromId(bookId4).setBorrowed(true);  // book is now borrowed
                                getBookObjectFromId(bookId4).setBorrowDate(currentDate); //borrowed at the given date
                                getBookObjectFromId(bookId4).setReadAtLibrary(true);  // being read at the library

                                // the books that are read in the library are considered to be borrowed with a return date in the year 9999
                                // its because I had designed the project in my mind such that every book that are borrowed has a return date
                                // I didn't want to destroy this design. Surely this could be handled in a more elegant way, But since this does not reach
                                // the boundaries of input files (hopefully as in the assignment2). I didn't want to spend more time on this.
                                // Because I had a very bad physics midterm last week I have to study more physics.

                                getBookObjectFromId(bookId4).setReturnDate(LocalDateTime.parse("9999-12-27",formatter));
                                getBookObjectFromId(bookId4).setCurrentBorrower(getMemberObjectFromId(memberId));
                                libraryOutput.write("The book [" + bookId4 +
                                        "] was read in library by member [" + memberId +
                                        "] at " + currentDate.format(formatter).substring(0,10) + "\n");
                            }
                        }else{
                            getBookObjectFromId(bookId4).setBorrowed(true);
                            getBookObjectFromId(bookId4).setBorrowDate(currentDate);
                            getBookObjectFromId(bookId4).setReadAtLibrary(true);

                            // the same situation as the one above
                            getBookObjectFromId(bookId4).setReturnDate(LocalDateTime.parse("9999-12-27",formatter));

                            getBookObjectFromId(bookId4).setCurrentBorrower(getMemberObjectFromId(memberId));

                            libraryOutput.write("The book [" + bookId4 +
                                    "] was read in library by member [" + memberId +
                                    "] at " + currentDate.format(formatter).substring(0,10) + "\n");
                        }
                    }
                    break;
                case "getTheHistory":
                    libraryOutput.write("History of library:\n\n");

                    libraryOutput.write("Number of students: " + Student.numberOfStudents + "\n");
                    for(Member member : members){
                        if(member instanceof Student){
                            libraryOutput.write("Student [id: " + member.getMemberId() + "]\n");
                        }
                    }
                    libraryOutput.write("\n");
                    libraryOutput.write("Number of academics: " + Academic.numberOfAcademicians + "\n");
                    for(Member member : members){
                        if(member instanceof Academic){
                            libraryOutput.write("Academic [id: " + member.getMemberId() + "]\n");
                        }
                    }
                    libraryOutput.write("\n");

                    libraryOutput.write("Number of printed books: " + PrintedBook.numberOfPrintedBooks + "\n");
                    for(Book book : books){
                        if(book instanceof PrintedBook){
                            libraryOutput.write("Printed [id: " + book.getBookId() + "]\n");
                        }
                    }
                    libraryOutput.write("\n");

                    libraryOutput.write("Number of handwritten books: " + HandWrittenBook.numberOfHandWrittenBooks + "\n");
                    for(Book book : books){
                        if(book instanceof HandWrittenBook){
                            libraryOutput.write("Handwritten [id: " + book.getBookId() + "]\n");
                        }
                    }
                    libraryOutput.write("\n");

                    int numOfBooksBeingRead = 0;
                    int numOfBorrowedBooks = 0;
                    for(Book book : books){
                        if(book.isBorrowed() && !book.isReadAtLibrary()){  // if it is borrowed and not being read at the library
                            numOfBorrowedBooks++;                           // this statement exists because if a book is read at the library then I "mark" them
                        }                                                   // as borrowed.

                        if(book.isReadAtLibrary()){ // if it is currently being read at the library
                            numOfBooksBeingRead++;
                        }
                    }
                    libraryOutput.write("Number of borrowed books: " + numOfBorrowedBooks + "\n");
                    for(Book book : books){
                        if(book.isBorrowed() && !book.isReadAtLibrary()){
                            libraryOutput.write("The book [" + book.getBookId() + "] was borrowed by member [" +
                                    book.getCurrentBorrower().getMemberId() + "] at " +
                                    book.getBorrowDate().format(formatter).substring(0,10) + "\n");
                        }
                    }
                    libraryOutput.write("\n");

                    libraryOutput.write("Number of books read in library: " + numOfBooksBeingRead + "\n");
                    for(Book book : books){
                        if(book.isReadAtLibrary()){
                            libraryOutput.write("The book [" + book.getBookId() +
                                    "] was read in library by member [" + book.getCurrentBorrower().getMemberId() +
                                    "] at " + book.getBorrowDate().format(formatter).substring(0,10) + "\n");
                        }
                    }
                    break;
                default:
                    libraryOutput.write("Wrong command\n");
            }
        }
        libraryOutput.closeFile();
    }


    /**
     * @param bookId is the book ID, with which we can access the book object.
     * @return the {@link Book} object that has the given integer as its bookId. If no such a book exists, then returns null.
     */
    private Book getBookObjectFromId(int bookId){
        for(Book book : books){
            if(book.getBookId() == bookId){
                return book;
            }
        }
        return null;
    }

    /**
     * @param memberId is the member ID of the member we are looking for
     * @return the {@link Member} object that has the given integer as its memberId. If no such a member exists, then returns null.
     *
     */

    private Member getMemberObjectFromId(int memberId){
        for(Member member : members){
            if (member.getMemberId() == memberId){
                return member;
            }
        }
        return null;
    }


}
