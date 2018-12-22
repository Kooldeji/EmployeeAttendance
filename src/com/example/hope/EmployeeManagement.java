package com.example.hope;

import java.util.List;

public class EmployeeManagement {
    private EmployeeDetail employeeDetail;
    private Repository repository

    public EmployeeManagement(EmployeeDetail employeeDetail) {
        this.employeeDetail = employeeDetail;
        repository = Repository.getInstance();
    }

    public void markPresent(){
        employeeDetail.setAttendanceCount(employeeDetail.getAttendanceCount()+1);
        saveEmployee();
    }
    
    public static EmployeeDetail resetAttendanceCount(EmployeeDetail employeeDetail){
        employeeDetail.setAttendanceCount(0);
        saveEmployee();
    }
    
    private void saveEmployee(){
        return repository.saveEmployeeDetail(employeeDetail);
    }

    public static EmployeeDetail registerEmployee(EmployeeDetail employeeDetail){
        return repository.saveEmployeeDetail(employeeDetail);
    }
    public static List<EmployeeDetail> getAllEmployees(){
        Repository repository = Repository.getInstance();
        return repository.getAllEmployeeDetails();
    }

    public long getSalary(int noOfDays){
        return employeeDetail.getBasicSalaryPay()-(employeeDetail.getDeduction()*(noOfDays-employeeDetail.getAttendanceCount()));
    }
}
