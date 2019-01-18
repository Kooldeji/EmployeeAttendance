package com.example.hope;

import java.util.List;

public class EmployeeManagement {
    private EmployeeDetail employeeDetail;
    private static Repository repository;

    public EmployeeManagement(EmployeeDetail employeeDetail) {
        this.employeeDetail = employeeDetail;
        repository = Repository.getInstance();
    }

    public void markPresent(){
        employeeDetail.setAttendanceCount(employeeDetail.getAttendanceCount()+1);
    }

    public static EmployeeDetail registerEmployee(EmployeeDetail employeeDetail){
        return repository.saveEmployeeDetail(employeeDetail);
    }

    public static List<EmployeeDetail> getAllEmployees(){
        return repository.getAllEmployeeDetails();
    }

    public long getSalary(int noOfDays){
        return employeeDetail.getBasicSalaryPay()-(employeeDetail.getDeduction()*(noOfDays-employeeDetail.getAttendanceCount()));
    }

    public void saveEmployee(){
        EmployeeDetail employeeDetail = repository.saveEmployeeDetail(this.employeeDetail);
        if (employeeDetail != null){
            this.employeeDetail = employeeDetail;
        }
    }
}
