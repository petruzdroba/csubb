package com.ui.controllers;

import com.service.AbstractService;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public abstract class AbstractPagedViewController<K, T> {
    protected AbstractService<K,T> service;
    protected int pageCount = 1;
    protected final int pageSize = 5;

    @FXML protected Button prevButton;
    @FXML protected Button nextButton;
    @FXML protected Label pageLabel;

    protected void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    protected void updatePageButtons() {
        prevButton.setDisable(pageCount <= 1);
        nextButton.setDisable(pageCount >= service.pageCount(pageSize));
    }

    public abstract void loadCurrentPage();

    public void onPrevPage() {
        if (pageCount > 1) {
            pageCount--;
            loadCurrentPage();
        }
    }

    public void onNextPage() {
        if (pageCount < service.pageCount(pageSize)) {
            pageCount++;
            loadCurrentPage();
        }
    }
}
