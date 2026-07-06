package org.zdroba.ui;

import javafx.application.Application;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import org.zdroba.entity.Employee;
import org.zdroba.paginators.OffsetPaginator;
import org.zdroba.paginators.KeysetPaginator;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class PaginationApp extends Application {

    private final OffsetPaginator offset = new OffsetPaginator();
    private final KeysetPaginator keyset = new KeysetPaginator();

    private int offsetPage = 0;

    private int keysetCursor = 0;
    private final List<Employee> keysetBuffer = new ArrayList<>();
    private int keysetPageIndex = 0;

    private String currentMode = "OFFSET";

    @Override
    public void start(Stage stage) {

        TableView<Employee> table = new TableView<>();
        Label timeLabel = new Label();
        Label pageInfo = new Label("Page: 0");

        ComboBox<String> mode = new ComboBox<>();
        mode.getItems().addAll("OFFSET", "KEYSET");
        mode.setValue("OFFSET");

        ComboBox<Integer> pageSize = new ComboBox<>();
        pageSize.getItems().addAll(10, 25, 50, 100);
        pageSize.setValue(50);

        TableColumn<Employee, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(d -> new SimpleObjectProperty<>(d.getValue().getId()));

        TableColumn<Employee, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(d -> new SimpleObjectProperty<>(d.getValue().getName()));

        TableColumn<Employee, String> emailCol = new TableColumn<>("Email");
        emailCol.setCellValueFactory(d -> new SimpleObjectProperty<>(d.getValue().getEmail()));

        TableColumn<Employee, BigDecimal> salaryCol = new TableColumn<>("Salary");
        salaryCol.setCellValueFactory(d -> new SimpleObjectProperty<>(d.getValue().getSalary()));

        TableColumn<Employee, Integer> deptCol = new TableColumn<>("Dept");
        deptCol.setCellValueFactory(d -> new SimpleObjectProperty<>(d.getValue().getDepartmentId()));

        table.getColumns().addAll(idCol, nameCol, emailCol, salaryCol, deptCol);

        Button first = new Button("First");
        Button prev = new Button("Prev");
        Button next = new Button("Next");

        Runnable resetOffset = () -> {
            offsetPage = 0;
        };

        Runnable resetKeyset = () -> {
            keysetCursor = 0;
            keysetBuffer.clear();
            keysetPageIndex = 0;
        };

        Runnable loadOffset = () -> {
            int size = pageSize.getValue();
            var page = offset.getEmployeesPage(offsetPage, size);
            table.getItems().setAll(page.getContent());
            pageInfo.setText("Page: " + offsetPage);
        };

        Runnable loadKeysetFirst = () -> {
            int size = pageSize.getValue();

            keysetCursor = 0;
            keysetBuffer.clear();
            keysetPageIndex = 0;

            var page = keyset.getEmployeesAfter(0, size);
            List<Employee> content = page.getContent();

            if (!content.isEmpty()) {
                keysetCursor = content.get(content.size() - 1).getId();
                keysetBuffer.addAll(content);
                keysetPageIndex = 1;
            }

            table.getItems().setAll(keysetBuffer);
            pageInfo.setText("Showing 1–" + keysetBuffer.size());
        };

        Runnable loadKeysetNext = () -> {
            int size = pageSize.getValue();

            var page = keyset.getEmployeesAfter(keysetCursor, size);
            List<Employee> content = page.getContent();

            if (!content.isEmpty()) {
                keysetCursor = content.get(content.size() - 1).getId();
                keysetBuffer.addAll(content);
                keysetPageIndex++;

                table.getItems().setAll(keysetBuffer);
                pageInfo.setText("Showing 1–" + keysetBuffer.size());
            }
        };

        Runnable loadKeysetPrev = () -> {
            int size = pageSize.getValue();

            if (keysetPageIndex > 1) {
                keysetPageIndex--;

                int from = (keysetPageIndex - 1) * size;
                int to = Math.min(from + size, keysetBuffer.size());

                table.getItems().setAll(keysetBuffer.subList(from, to));
                pageInfo.setText("Page: " + keysetPageIndex);
            }
        };

        mode.setOnAction(e -> {

            table.getItems().clear();
            timeLabel.setText("");

            currentMode = mode.getValue();

            if (currentMode.equals("OFFSET")) {
                resetOffset.run();
                loadOffset.run();
            } else {
                resetKeyset.run();
                loadKeysetFirst.run();
            }
        });

        first.setOnAction(e -> {
            long start = System.currentTimeMillis();
            int size = pageSize.getValue();

            if (currentMode.equals("OFFSET")) {
                offsetPage = 0;
                loadOffset.run();
            } else {
                loadKeysetFirst.run();
            }

            long end = System.currentTimeMillis();
            timeLabel.setText((end - start) + " ms");
        });

        next.setOnAction(e -> {
            long start = System.currentTimeMillis();
            int size = pageSize.getValue();

            if (currentMode.equals("OFFSET")) {
                offsetPage++;
                loadOffset.run();
            } else {
                loadKeysetNext.run();
            }

            long end = System.currentTimeMillis();
            timeLabel.setText((end - start) + " ms");
        });

        prev.setOnAction(e -> {
            long start = System.currentTimeMillis();
            int size = pageSize.getValue();

            if (currentMode.equals("OFFSET")) {
                offsetPage = Math.max(0, offsetPage - 1);
                loadOffset.run();
            } else {
                loadKeysetPrev.run();
            }

            long end = System.currentTimeMillis();
            timeLabel.setText((end - start) + " ms");
        });

        VBox root = new VBox(
                mode,
                new HBox(new Label("Size"), pageSize),
                new HBox(first, prev, next),
                pageInfo,
                table,
                timeLabel
        );

        stage.setScene(new Scene(root, 900, 600));
        stage.setTitle("Pagination Benchmark UI");
        stage.show();

        loadOffset.run();
    }

    public static void main(String[] args) {
        launch();
    }
}