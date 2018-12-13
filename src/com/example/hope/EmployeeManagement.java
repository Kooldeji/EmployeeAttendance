package com.example.hope;

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

    public long getSalary(int noOfDays){
        return employeeDetail.getBasicSalaryPay()-(employeeDetail.getDeduction()*(noOfDays-employeeDetail.getAttendanceCount()));
    }
}
