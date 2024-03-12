public class Academic extends Member{
    protected static int numberOfAcademicians;

    public Academic() {
        memberId = ++memberCounter;
        numberOfAcademicians++;
    }
}
