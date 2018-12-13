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
    private Connection con;

    private PreparedStatement insertEmployeePs;
    private PreparedStatement insertSettingsPs;

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

    public List<EmployeeDetail> getAllEmployeeDetails(){
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
        employeeDetail.setAge(rs.getInt("age"));
        employeeDetail.setAttendanceCount(rs.getLong("attendance_count"));
        employeeDetail.setDeduction(rs.getLong("deduction"));
        employeeDetail.setBasicSalaryPay(rs.getLong("basic_salary"));
        employeeDetail.setSex(rs.getString("sex"));
        String templateJson = rs.getString("template");
        if (templateJson != null && !templateJson.isEmpty()){
            FingerprintTemplate fingerprintTemplate = new FingerprintTemplate().deserialize(templateJson);
            employeeDetail.setTemplate(fingerprintTemplate);
        }
        return employeeDetail;
    }

    public EmployeeDetail getEmployee(long id){
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
    public EmployeeDetail saveEmployeeDetail(EmployeeDetail employeeDetail) {
        String query;
        if (employeeDetail.getId() <= 0)
            query = String.format("INSERT INTO %s (first_name, last_name, age, deduction, attendance_count, basic_salary, sex, template) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)", EMPLOYEE_TABLE);
        else
            query = String.format("UPDATE %s SET first_name = ?, last_name = ?, age = ?, deduction = ?, attendance_count = ?, " +
                    "basic_salary = ?, sex = ?, template = ? WHERE id = ?", EMPLOYEE_TABLE);
        try {
            if (insertEmployeePs == null) {
                insertEmployeePs = con.prepareStatement(query);
            }
            insertEmployeePs.setString(1, employeeDetail.getFirstName());
            insertEmployeePs.setString(2, employeeDetail.getLastName());
            insertEmployeePs.setInt(3, employeeDetail.getAge());
            insertEmployeePs.setLong(4, employeeDetail.getDeduction());
            insertEmployeePs.setLong(5, 0);
            insertEmployeePs.setLong(6, employeeDetail.getBasicSalaryPay());
            insertEmployeePs.setString(7, employeeDetail.getSex());
            if (employeeDetail.getTemplate() != null) insertEmployeePs.setString(8, employeeDetail.getTemplate().serialize());
            else
                insertEmployeePs.setString(8, null);
            if (employeeDetail.getId() > 0) insertEmployeePs.setLong(9, employeeDetail.getId());
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

    public Settings getSettings() {
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

    public boolean saveSettings(Settings settings){
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

    public void clearEmployees() {
        try {
            String query = String.format("TRUNCATE TABLE %s", EMPLOYEE_TABLE);
            Statement statement = con.createStatement();
            statement.execute(query);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
