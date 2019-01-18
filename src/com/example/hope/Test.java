package com.example.hope;

import java.io.File;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class Test {
    private static EmployeeDetail timi;

    public static void main(String[] args) {
        timi = new EmployeeDetail();
        timi.setFirstName("Timi");
        timi.setLastName("Fasipe");
        timi.setSex("Male");
        timi.setDob(new GregorianCalendar(1997, 12, 11).getTime());
        timi.setBasicSalaryPay(50000);
        timi.setDeduction(100);
        Repository.getInstance().clearEmployees();
        identifyNoEmployee();
    }

    public static void register(){
        timi = EmployeeManagement.registerEmployee(timi);
    }

    public static void  registerFingerPrint(){
        register();
        File file = new File("/Users/dakande/Documents/code/FingerPrint/probe.png");
        EmployeeIdentification employeeIdentification = EmployeeIdentification.getInstance();
        timi = employeeIdentification.registerIdentification(timi, file);
    }
    public static void identifyEmployee(){
        registerFingerPrint();
        File file = new File("/Users/dakande/Documents/code/FingerPrint/matching.png");
        EmployeeIdentification employeeIdentification = EmployeeIdentification.getInstance();
        EmployeeDetail p = employeeIdentification.identifyEmployee(file);
        System.out.print("Identify Employee: ");
        System.out.println(p.getLastName().equals(timi.getLastName()));
    }
    public static void identifyNoEmployee(){
        registerFingerPrint();
        File file = new File("/Users/dakande/Documents/code/FingerPrint/nonmatching.png");
        EmployeeIdentification employeeIdentification = EmployeeIdentification.getInstance();
        EmployeeDetail p = employeeIdentification.identifyEmployee(file);
        System.out.print("Don't identify Employee: ");
        System.out.println(p == null);
    }

    public static void takeAttendance(){
        registerFingerPrint();
        long c = timi.getAttendanceCount();
        File file = new File("/Users/dakande/Documents/code/FingerPrint/matching.png");
        EmployeeIdentification employeeIdentification = EmployeeIdentification.getInstance();
        EmployeeDetail p = employeeIdentification.identifyEmployee(file);
        EmployeeManagement employeeManagement = new EmployeeManagement(p);
        employeeManagement.markPresent();
        System.out.print("Take attendance: ");
        System.out.println(p.getAttendanceCount() == timi.getAttendanceCount()+1);
    }
}
