package com.example.hope;

public class SettingsService {
    public Settings getSettings(){
        Repository repository = Repository.getInstance();
        return repository.getSettings();
    }
    public void saveSettings(Settings settings){
        Repository repository = Repository.getInstance();
        repository.saveSettings(settings);
    }
}
