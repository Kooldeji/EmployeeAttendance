package com.example.hope;

import com.machinezoo.sourceafis.FingerprintMatcher;
import com.machinezoo.sourceafis.FingerprintTemplate;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class EmployeeIdentification {
    private final Repository repository;
    private Settings settings;
    private static EmployeeIdentification instance;

    public EmployeeIdentification() {
        repository = Repository.getInstance();
        settings = repository.getSettings();
    }

    public static EmployeeIdentification getInstance(){
        if (instance == null) instance = new EmployeeIdentification();
        return instance;
    }

    public EmployeeDetail registerIdentification(EmployeeDetail employeeDetail, File file){
        if (employeeDetail.getId() <= 0) throw new IllegalArgumentException("Employee must have an ID");
        FingerprintTemplate fingerprintTemplate = getTemplateFromFile(file);
        employeeDetail.setTemplate(fingerprintTemplate);
        return repository.saveEmployeeDetail(employeeDetail);
    }

    public EmployeeDetail identifyEmployee(File file){
        FingerprintTemplate fingerprintTemplate = getTemplateFromFile(file);
        List<EmployeeDetail> employeeDetails = repository.getAllEmployeeDetails();
        EmployeeDetail match = getMatch(fingerprintTemplate, employeeDetails);
        return match;
    }

    private EmployeeDetail getMatch(FingerprintTemplate fingerprintTemplate, List<EmployeeDetail> employeeDetails) {
        FingerprintMatcher fingerprintMatcher = new FingerprintMatcher().index(fingerprintTemplate);
        double highScore = 0;
        EmployeeDetail match = null;
        for (EmployeeDetail e : employeeDetails) {
            if (e.getTemplate()==null) continue;
            double score = fingerprintMatcher.match(e.getTemplate());
            if (score > highScore) {
                highScore = score;
                match = e;
            }
        }
        if (highScore < settings.getMatchThreshold()) return null;
        return match;
    }

    private FingerprintTemplate getTemplateFromFile(File file) {
        try (InputStream is = new FileInputStream(file)) {
            ArrayList<Byte> byteList = new ArrayList<>();
            int b;
            while ((b = is.read())>-1){
                byteList.add((byte) b);
            }
            byte[] imageBytes = new byte[byteList.size()];
            for (int i = 0; i < byteList.size(); i++) {
                imageBytes[i] = byteList.get(i);
            }
            return new FingerprintTemplate()
                    .dpi(500)
                    .create(imageBytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


}
