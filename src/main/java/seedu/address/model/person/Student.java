package seedu.address.model.person;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import seedu.address.model.lesson.Lesson;
import seedu.address.model.tag.Tag;

/**
 * Represents a Person in the address book.
 * Guarantees: details are present and not null, field values are validated, immutable.
 */
public class Student {

    // Identity fields
    private final Name name;
    private final ParentContact parentContact;
    private final Email email;

    // Data fields
    private final Address address;
    private final Grade grade;
    private final Set<Tag> tags = new HashSet<>();
    private final Set<Lesson> lessons = new HashSet<>();

    /**
     * Every field must be present and not null.
     */
    public Student(Name name, ParentContact parentContact, Email email, Address address, Grade grade, Set<Tag> tags) {
        requireAllNonNull(name, parentContact, email, address, tags);
        this.name = name;
        this.parentContact = parentContact;
        this.email = email;
        this.address = address;
        this.grade = grade;
        this.tags.addAll(tags);
    }

    public Name getName() {
        return name;
    }

    public Email getEmail() {
        return email;
    }

    public Address getAddress() {
        return address;
    }

    public ParentContact getParentContact() {
        return parentContact;
    }

    public Grade getGrade() {
        return grade;
    }

    /**
     * Returns an immutable tag set, which throws {@code UnsupportedOperationException}
     * if modification is attempted.
     */
    public Set<Tag> getTags() {
        return Collections.unmodifiableSet(tags);
    }

    /**
     * Returns an immutable lesson set, which throws {@code UnsupportedOperationException}
     * if modification is attempted.
     */
    public Set<Lesson> getLessons() {
        return Collections.unmodifiableSet(lessons);
    }

    /**
     * Returns true if both persons have the same name.
     * This defines a weaker notion of equality between two persons.
     */
    public boolean isSamePerson(Student otherStudent) {
        if (otherStudent == this) {
            return true;
        }
        return otherStudent != null
                && otherStudent.getName().equals(getName());
    }

    /**
     * Returns an unmodifiable set of enrolled lesson codes for equality checks.
     */
    public Set<String> getEnrolledLessonCodes() {
        return lessons.stream().map(Lesson::getLessonCode).collect(Collectors.toUnmodifiableSet());
    }

    /**
     * Adds lesson to student instance.
     */
    public void enrollForLesson(Lesson lesson) {
        lessons.add(lesson);
    }

    /**
     * Remove lesson from student instance.
     */
    public void unenrollFromLesson(Lesson lesson) {
        lessons.remove(lesson);
    }

    /**
     * Returns true if both persons have the same identity and data fields.
     * This defines a stronger notion of equality between two persons.
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof Student)) {
            return false;
        }

        Student otherStudent = (Student) other;
        return otherStudent.getName().equals(getName())
                && otherStudent.getParentContact().equals(getParentContact())
                && otherStudent.getEmail().equals(getEmail())
                && otherStudent.getAddress().equals(getAddress())
                && otherStudent.getGrade().equals(getGrade())
                && otherStudent.getTags().equals(getTags())
                && otherStudent.getEnrolledLessonCodes().equals(getEnrolledLessonCodes());
    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(name, parentContact, email, address, tags, getEnrolledLessonCodes());
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append(getName())
                .append("; Phone: ")
                .append(getParentContact())
                .append("; Email: ")
                .append(getEmail())
                .append("; Address: ")
                .append(getAddress())
                .append("; Grade: ")
                .append(getGrade());

        Set<Tag> tags = getTags();
        if (!tags.isEmpty()) {
            builder.append("; Tags: ");
            tags.forEach(builder::append);
        }
        Set<String> lessons = getEnrolledLessonCodes();
        if (!lessons.isEmpty()) {
            builder.append("; Lesson: ");
            lessons.forEach(builder::append);
        }
        return builder.toString();
    }

}
