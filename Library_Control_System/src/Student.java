public class Student extends Member{
    protected static int numberOfStudents;
    public Student() {
        memberId = ++memberCounter;
        numberOfStudents++;
    }
}
