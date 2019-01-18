package com.example.hope;

import com.machinezoo.sourceafis.FingerprintTemplate;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Repository {

    private static Repository instance;
    private static final String DATABASE_NAME = "employee_management";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "root" ;
    private static final String SETTINGS_TABLE = "settings_table";
    private static final String EMPLOYEE_TABLE = "employee_table";
    private static final String ATTENDANCE_RECORD_TABLE = "attendance_record_table";
    private Connection con;

    private PreparedStatement insertEmployeePs;
    private PreparedStatement insertSettingsPs;
    private PreparedStatement insertAttendancePs;
    private PreparedStatement updateAttendanceTimeoutPs;
    private PreparedStatement getEmployeeAttendanceRecordInDateRangePs;
    private PreparedStatement getAttendanceRecordsPs;
    private PreparedStatement getEmployeeAttendanceRecordOnDatePs;

    public static Repository getInstance(){
        if (instance == null) instance = new Repository();
        return instance;
    }

    public Repository() {
        connect();
        initializeDB();
    }

    private void initializeDB() {
        if (getSettings()==null) {
            Settings settings = new Settings();
            settings.setDpi(500);
            settings.setMatchThreshold(40);
            saveSettings(settings);
        }
    }

    public void connect(){
        try {
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection(String.format("jdbc:mysql://localhost:3306/%s", DATABASE_NAME), USERNAME, PASSWORD);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    List<EmployeeDetail> getAllEmployeeDetails(){
        List<EmployeeDetail> employeeDetails = new ArrayList<>();
        String query = String.format("SELECT * FROM %s", EMPLOYEE_TABLE);
        try {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()){
                employeeDetails.add(getEmployeeDetail(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return employeeDetails;
    }


    private EmployeeDetail getEmployeeDetail(ResultSet rs) throws SQLException {
        EmployeeDetail employeeDetail = new EmployeeDetail();
        employeeDetail.setId(rs.getLong("id"));
        employeeDetail.setFirstName(rs.getString("first_name"));
        employeeDetail.setLastName(rs.getString("last_name"));
        employeeDetail.setDob(rs.getDate("dob"));
        employeeDetail.setAttendanceCount(rs.getLong("attendance_count"));
        employeeDetail.setDeduction(rs.getLong("deduction"));
        employeeDetail.setBasicSalaryPay(rs.getLong("basic_salary"));
        employeeDetail.setSex(rs.getString("sex"));
        employeeDetail.setDepartment(rs.getString("department"));
        employeeDetail.setJobRole(rs.getString("job_role"));
        employeeDetail.setAddress(rs.getString("address"));
        employeeDetail.setPhoneNumber(rs.getString("phone_nos"));
        String templateJson = rs.getString("template");
        if (templateJson != null && !templateJson.isEmpty()){
            FingerprintTemplate fingerprintTemplate = new FingerprintTemplate().deserialize(templateJson);
            employeeDetail.setTemplate(fingerprintTemplate);
        }
        return employeeDetail;
    }

    EmployeeDetail getEmployee(long id){
        String query =  String.format("SELECT * FROM %s where id = %s", EMPLOYEE_TABLE, id);
        try {
            Statement statement = con.createStatement();
            ResultSet rs = statement.executeQuery(query);
            if (rs.next()){
                return getEmployeeDetail(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    EmployeeDetail saveEmployeeDetail(EmployeeDetail employeeDetail) {
        String query;
        if (employeeDetail.getId() <= 0)
            query = String.format("INSERT INTO %s (first_name, last_name, dob, deduction, attendance_count, basic_salary, sex, template, department, job_role, address, phone_nos) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)", EMPLOYEE_TABLE);
        else
            query = String.format("UPDATE %s SET first_name = ?, last_name = ?, dob = ?, deduction = ?, attendance_count = ?, " +
                    "basic_salary = ?, sex = ?, template = ? , department = ?, job_role = ?, address = ?, phone_nos = ? WHERE id = ?", EMPLOYEE_TABLE);
        try {
            insertEmployeePs = con.prepareStatement(query);

            insertEmployeePs.setString(1, employeeDetail.getFirstName());
            insertEmployeePs.setString(2, employeeDetail.getLastName());
            insertEmployeePs.setDate(3, new Date(employeeDetail.getDob().getTime()));
            insertEmployeePs.setLong(4, employeeDetail.getDeduction());
            insertEmployeePs.setLong(5, 0);
            insertEmployeePs.setLong(6, employeeDetail.getBasicSalaryPay());
            insertEmployeePs.setString(7, employeeDetail.getSex());
            if (employeeDetail.getTemplate() != null) insertEmployeePs.setString(8, employeeDetail.getTemplate().serialize());
            else
                insertEmployeePs.setString(8, null);
            insertEmployeePs.setString(9, employeeDetail.getDepartment());
            insertEmployeePs.setString(10, employeeDetail.getJobRole());
            insertEmployeePs.setString(11, employeeDetail.getAddress());
            insertEmployeePs.setString(12, employeeDetail.getPhoneNumber());
            if (employeeDetail.getId() > 0) insertEmployeePs.setLong(13, employeeDetail.getId());
            insertEmployeePs.execute();
            ResultSet rs = insertEmployeePs.getGeneratedKeys();
            if (rs.next()){
                return getEmployee(rs.getLong(1));
            }
            return employeeDetail;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    Settings getSettings() {
        String query = String.format("SELECT * FROM %s ", SETTINGS_TABLE);
        try{
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()){
                Settings settings = new Settings();
                settings.setDpi(rs.getLong("dpi"));
                settings.setMatchThreshold(rs.getLong("threshold"));
                return settings;
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    boolean saveSettings(Settings settings){
        String query;
        if (getSettings() == null)
            query = String.format("INSERT INTO %s (dpi, threshold) VALUES (?, ?)", SETTINGS_TABLE);
        else
            query = String.format("UPDATE %s SET dpi = ?, threshold = ? WHERE id=1", SETTINGS_TABLE);
        try{
            if (insertSettingsPs == null) insertSettingsPs = con.prepareStatement(query);
            insertSettingsPs.setLong(1, settings.getDpi());
            insertSettingsPs.setLong(2, settings.getMatchThreshold());
            return insertSettingsPs.execute();
        }catch (SQLException e){
            e.printStackTrace();
        }

        return false;
    }

    void clearEmployees() {
        try {
            String query = String.format("TRUNCATE TABLE %s", EMPLOYEE_TABLE);
            Statement statement = con.createStatement();
            statement.execute(query);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    boolean saveAttendanceRecord(AttendanceRecord attendanceRecord){
        try {
            String query = String.format("INSERT INTO %s (employee_id, date, time_in, time_out) " +
                    "VALUES (?, ?, ?, ?, ?)", ATTENDANCE_RECORD_TABLE);
            if (insertAttendancePs == null) {
                insertAttendancePs = con.prepareStatement(query);
            }
            insertAttendancePs.setLong(1, attendanceRecord.getEmployeeId());
            insertAttendancePs.setDate(2, new Date(attendanceRecord.getDate().getTime()));
            insertAttendancePs.setTime(3, new Time(attendanceRecord.getTimeIn().getTime()));
            insertAttendancePs.setTime(4, new Time(attendanceRecord.getTimeOut().getTime()));
            return insertAttendancePs.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    boolean updateAttendanceRecordTimeout(long employeeId, java.util.Date date, java.util.Date timeout){
        try {
            if (updateAttendanceTimeoutPs == null) {
                String query = String.format("UPDATE %s SET time_out = ? WHERE date = ? AND employee_id = ?", ATTENDANCE_RECORD_TABLE);
                updateAttendanceTimeoutPs = con.prepareStatement(query);
            }
            updateAttendanceTimeoutPs.setTime(1, new Time(timeout.getTime()));
            updateAttendanceTimeoutPs.setDate(2, new Date(date.getTime()));
            updateAttendanceTimeoutPs.setLong(3, employeeId);
            return updateAttendanceTimeoutPs.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    List<AttendanceRecord> getEmployeeAttendanceRecordInDateRange(long employeeId, java.util.Date startDate, java.util.Date endDate){
        try {
            if (getEmployeeAttendanceRecordInDateRangePs == null) {
                String query = String.format("SELECT * FROM %s WHERE date BETWEEN ? AND ?", ATTENDANCE_RECORD_TABLE);
                getEmployeeAttendanceRecordInDateRangePs = con.prepareStatement(query);
            }
            getEmployeeAttendanceRecordInDateRangePs.setDate(1, new Date(startDate.getTime()));
            getEmployeeAttendanceRecordInDateRangePs.setDate(2, new Date(endDate.getTime()));
            List<AttendanceRecord > records = new ArrayList<>();
            ResultSet rs = getEmployeeAttendanceRecordInDateRangePs.executeQuery();
            while (rs.next()){
                AttendanceRecord attendanceRecord = getAttendanceRecord(rs);
                records.add(attendanceRecord);
            }
            return records;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return  null;
    }

    List<AttendanceRecord> getAttendanceRecords(){
        try {
            if (getAttendanceRecordsPs == null) {
                String query = String.format("SELECT * FROM %s", ATTENDANCE_RECORD_TABLE);
                getAttendanceRecordsPs = con.prepareStatement(query);
            }
            List<AttendanceRecord > records = new ArrayList<>();
            ResultSet rs = getAttendanceRecordsPs.executeQuery();
            while (rs.next()){
                AttendanceRecord attendanceRecord = getAttendanceRecord(rs);
                records.add(attendanceRecord);
            }
            return records;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return  null;
    }

    AttendanceRecord getEmployeeAttendanceRecordOnDate(long employeeId, java.util.Date date){
        try {
            if (getEmployeeAttendanceRecordOnDatePs == null) {
                String query = String.format("SELECT * FROM %s WHERE date = ?", ATTENDANCE_RECORD_TABLE);
                getEmployeeAttendanceRecordOnDatePs = con.prepareStatement(query);
            }
            getEmployeeAttendanceRecordOnDatePs.setDate(1, new Date(date.getTime()));
            List<AttendanceRecord > records = new ArrayList<>();
            ResultSet rs = getEmployeeAttendanceRecordOnDatePs.executeQuery();
            while (rs.next()){
                AttendanceRecord attendanceRecord = getAttendanceRecord(rs);
                return attendanceRecord;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return  null;
    }

    private AttendanceRecord getAttendanceRecord(ResultSet rs) throws SQLException {
        AttendanceRecord attendanceRecord = new AttendanceRecord();
        attendanceRecord.setId(rs.getLong("ID"));
        attendanceRecord.setEmployeeId(rs.getLong("employee_id"));
        attendanceRecord.setDate(rs.getDate("date"));
        attendanceRecord.setTimeIn(rs.getTime("time_in"));
        attendanceRecord.setTimeOut(rs.getTime("time_out"));
        return attendanceRecord;
    }
}
