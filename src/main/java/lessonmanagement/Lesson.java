package lessonmanagement;

public class Lesson {

    private String lessonType;

    // Constructor
    public Lesson(String lessonType) {
        this.lessonType = lessonType;
    }

    // Getter for lessonType
    public String getLessonType() {
        return lessonType;
    }

    // Setter for lessonType
    public void setLessonType(String lessonType) {
        this.lessonType = lessonType;
    }
}
