package cpe_lab_inventory;

public class Student {
    private String studentId;
    private String name;
    private String course;
    private int yearLevel;
    private String contactNumber;
    private String registeredAt;

    public Student(String studentId, String name, String course, int yearLevel, String contactNumber, String registeredAt) {
        this.studentId = studentId;
        this.name = name;
        this.course = course;
        this.yearLevel = yearLevel;
        this.contactNumber = contactNumber;
        this.registeredAt = registeredAt;
    }

    public String getStudentId() { return studentId; }
    public String getName() { return name; }
    public String getCourse() { return course; }
    public int getYearLevel() { return yearLevel; }
    public String getContactNumber() { return contactNumber; }
    public String getRegisteredAt() { return registeredAt; }
}
