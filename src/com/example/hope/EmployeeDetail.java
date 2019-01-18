package com.example.hope;

import com.machinezoo.sourceafis.FingerprintTemplate;

import java.util.Date;

public class EmployeeDetail {
    private long id;
    private String firstName;
    private String lastName;
    private Date dob;
    private String sex;
    private long attendanceCount;
    private long basicSalaryPay;
    private long deduction;
    private String department;
    private String jobRole;
    private String address;
    private String phoneNumber;
    private FingerprintTemplate template;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
        this.dob = dob;
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

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getJobRole() {
        return jobRole;
    }

    public void setJobRole(String jobRole) {
        this.jobRole = jobRole;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
