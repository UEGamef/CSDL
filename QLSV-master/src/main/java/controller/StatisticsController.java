package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import service.StatisticsDao;

public class StatisticsController {

    @FXML private Label lblDepartments;
    @FXML private Label lblMajors;
    @FXML private Label lblClasses;
    @FXML private Label lblStudents;
    @FXML private Label lblLecturers;
    @FXML private Label lblSubjects;

    private final StatisticsDao statisticsDao = new StatisticsDao();

    @FXML
    public void initialize() {
        loadStatistics();
    }

    private void loadStatistics() {
        lblDepartments.setText(String.valueOf(statisticsDao.countDepartments()));
        lblMajors.setText(String.valueOf(statisticsDao.countMajors()));
        lblClasses.setText(String.valueOf(statisticsDao.countClasses()));
        lblStudents.setText(String.valueOf(statisticsDao.countStudents()));
        lblLecturers.setText(String.valueOf(statisticsDao.countLecturers()));
        lblSubjects.setText(String.valueOf(statisticsDao.countSubjects()));
    }
}