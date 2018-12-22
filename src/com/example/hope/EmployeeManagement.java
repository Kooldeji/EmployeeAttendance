package com.example.hope;

import java.util.List;

public class EmployeeManagement {
    private EmployeeDetail employeeDetail;

    public EmployeeManagement(EmployeeDetail employeeDetail) {
        this.employeeDetail = employeeDetail;
    }

    public void markPresent(){
        employeeDetail.setAttendanceCount(employeeDetail.getAttendanceCount()+1);
    }

    public static EmployeeDetail registerEmployee(EmployeeDetail employeeDetail){
        Repository repository = Repository.getInstance();
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
