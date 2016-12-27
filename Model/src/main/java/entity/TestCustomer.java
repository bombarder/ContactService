package entity;

import annotations.CustomDateFormat;
import annotations.JsonValue;

import java.time.LocalDate;

public class TestCustomer {

    private String firstName;
    @JsonValue(name ="fun")
    private String hobby;
    @JsonValue(name ="funDay")
    @CustomDateFormat(format = "dd-MM-yyyy")
    private LocalDate birthDate;
    private byte byteNumber;
    private short shortNumber;
    private int age;
    private long longNumber;
    private float floatNumber;
    private double doubleNumber;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getHobby() {
        return hobby;
    }

    public void setHobby(String hobby) {
        this.hobby = hobby;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public byte getByteNumber() {
        return byteNumber;
    }

    public void setByteNumber(byte byteNumber) {
        this.byteNumber = byteNumber;
    }

    public short getShortNumber() {
        return shortNumber;
    }

    public void setShortNumber(short shortNumber) {
        this.shortNumber = shortNumber;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public long getLongNumber() {
        return longNumber;
    }

    public void setLongNumber(long longNumber) {
        this.longNumber = longNumber;
    }

    public float getFloatNumber() {
        return floatNumber;
    }

    public void setFloatNumber(float floatNumber) {
        this.floatNumber = floatNumber;
    }

    public double getDoubleNumber() {
        return doubleNumber;
    }

    public void setDoubleNumber(double doubleNumber) {
        this.doubleNumber = doubleNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TestCustomer that = (TestCustomer) o;

        if (byteNumber != that.byteNumber) return false;
        if (shortNumber != that.shortNumber) return false;
        if (age != that.age) return false;
        if (longNumber != that.longNumber) return false;
        if (Float.compare(that.floatNumber, floatNumber) != 0) return false;
        if (Double.compare(that.doubleNumber, doubleNumber) != 0) return false;
        if (firstName != null ? !firstName.equals(that.firstName) : that.firstName != null) return false;
        if (hobby != null ? !hobby.equals(that.hobby) : that.hobby != null) return false;
        return birthDate != null ? birthDate.equals(that.birthDate) : that.birthDate == null;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = firstName != null ? firstName.hashCode() : 0;
        result = 31 * result + (hobby != null ? hobby.hashCode() : 0);
        result = 31 * result + (birthDate != null ? birthDate.hashCode() : 0);
        result = 31 * result + (int) byteNumber;
        result = 31 * result + (int) shortNumber;
        result = 31 * result + age;
        result = 31 * result + (int) (longNumber ^ (longNumber >>> 32));
        result = 31 * result + (floatNumber != +0.0f ? Float.floatToIntBits(floatNumber) : 0);
        temp = Double.doubleToLongBits(doubleNumber);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    @Override
    public String toString() {
        return "TestCustomer{" +
                "firstName='" + firstName + '\'' +
                ", hobby='" + hobby + '\'' +
                ", birthDate=" + birthDate +
                ", byteNumber=" + byteNumber +
                ", shortNumber=" + shortNumber +
                ", age=" + age +
                ", longNumber=" + longNumber +
                ", floatNumber=" + floatNumber +
                ", doubleNumber=" + doubleNumber +
                '}';
    }
}
