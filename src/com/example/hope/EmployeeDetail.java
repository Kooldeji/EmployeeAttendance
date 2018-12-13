package com.example.hope;

import com.machinezoo.sourceafis.FingerprintTemplate;

public class EmployeeDetail {
    private long id;
    private String firstName;
    private String lastName;
    private int age;
    private String sex;
    private long attendanceCount;
    private long basicSalaryPay;
    private long deduction;
    private FingerprintTemplate template;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public FingerprintTemplate getTemplate() {
        return template;
    }

    public long getAttendanceCount() {
        return attendanceCount;
    }

    public void setAttendanceCount(long attendanceCount) {
        this.attendanceCount = attendanceCount;
    }

    public long getBasicSalaryPay() {
        return basicSalaryPay;
    }

    public void setBasicSalaryPay(long basicSalaryPay) {
        this.basicSalaryPay = basicSalaryPay;
    }

    public long getDeduction() {
        return deduction;
    }

    public void setDeduction(long deduction) {
        this.deduction = deduction;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setTemplate(FingerprintTemplate template) {
        this.template = template;
    }
}
