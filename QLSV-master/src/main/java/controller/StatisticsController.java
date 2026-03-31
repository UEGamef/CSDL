package controller;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.util.Pair;
import service.StatisticsDao;

public class StatisticsController {

    @FXML private Label lblDepartments;
    @FXML private Label lblClasses;
    @FXML private Label lblStudents;
    @FXML private Label lblSubjects;
    @FXML private Label lblGrades;
    @FXML private ListView<String> lvTopGpa;
    @FXML private ListView<String> lvStudentsByClass;
    @FXML private ListView<String> lvStudentsByDepartment;

    private final StatisticsDao statisticsDao = new StatisticsDao();

    @FXML
    public void initialize() {
        loadStatistics();
        loadTopGpa();
        loadStudentsByClass();
        loadStudentsByDepartment();
    }

    private void loadStatistics() {
        lblDepartments.setText(String.valueOf(statisticsDao.countDepartments()));
        lblClasses.setText(String.valueOf(statisticsDao.countClasses()));
        lblStudents.setText(String.valueOf(statisticsDao.countStudents()));
        lblSubjects.setText(String.valueOf(statisticsDao.countSubjects()));
        lblGrades.setText(String.valueOf(statisticsDao.countGrades()));
    }

    private void loadTopGpa() {
        ObservableList<Pair<String, Double>> topGpa = statisticsDao.getTop5Gpa();
        for (Pair<String, Double> item : topGpa) {
            lvTopGpa.getItems().add(String.format("%s - GPA: %.2f", item.getKey(), item.getValue()));
        }
    }

    private void loadStudentsByClass() {
        lvStudentsByClass.getItems().addAll(statisticsDao.getStudentsByClass());
    }

    private void loadStudentsByDepartment() {
        lvStudentsByDepartment.getItems().addAll(statisticsDao.getStudentsByDepartment());
    }
}