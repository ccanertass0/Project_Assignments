public class Member{
    protected static int memberCounter;  // counts how many members are added, very useful for giving members their Ids
    protected int memberId;
    protected int borrowedBookNum;   // this is the number of the books the member currently has.

    public int getMemberId() {
        return memberId;
    }

}
