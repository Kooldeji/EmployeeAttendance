package com.example.hope;

import java.util.Date;
import java.util.List;

public class AttendanceManagement {
    private final Repository repository;

    public AttendanceManagement() {
        repository = Repository.getInstance();
    }
    public List<AttendanceRecord> getAttendanceRecords(){
        return repository.getAttendanceRecords();
    }
    public List<AttendanceRecord> getEmployeeAttendanceRecordsInDateRange(long employeeID, Date start, Date end){
        return repository.getEmployeeAttendanceRecordInDateRange(employeeID, start, end);
    }
    public AttendanceRecord getEmployeeAttendanceRecordOnDate(long employeeID, Date date){
        return repository.getEmployeeAttendanceRecordOnDate(employeeID, date);
    }
    public boolean createAttendanceRecord(AttendanceRecord attendanceRecord) {
        return repository.saveAttendanceRecord(attendanceRecord);
    }
    public boolean updateTimeout(long employeeId, Date date, Date timeOut) {
        return repository.updateAttendanceRecordTimeout(employeeId, date, timeOut);
    }
}
