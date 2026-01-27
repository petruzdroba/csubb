package org.example;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.controllers.PatronViewController;
import org.example.controllers.StaffViewController;
import org.example.domain.DataBaseConfig;
import org.example.domain.Patron;
import org.example.repo.BookRepo;
import org.example.repo.BorrowRepo;
import org.example.repo.DatabaseConnection;
import org.example.repo.PatronRepo;
import org.example.service.BookService;
import org.example.service.BorrowService;
import org.example.service.PatronService;

public class MainApplication extends Application {
    private DataBaseConfig config;

    private PatronService patronService;
    private PatronRepo patronRepo;

    private BookService bookService;
    private BookRepo bookRepo;

    private BorrowService borrowService;
    private BorrowRepo borrowRepo;

    @Override
    public void start(Stage primaryStage) throws Exception {
        config = new DataBaseConfig(
                "jdbc:postgresql://localhost:5432/mappractic",
                "sn_user",
                "sn_pass"
        );

        patronRepo = new PatronRepo(config);
        patronService = new PatronService(patronRepo);

        bookRepo = new BookRepo(config);
        bookService = new BookService(bookRepo);

        borrowRepo = new BorrowRepo(config,bookRepo, patronRepo);
        borrowService = new BorrowService(borrowRepo);

        for(Patron patron: patronService.getAll()){
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/patron-view.fxml"));
            Scene scene = new Scene(loader.load());
            PatronViewController controller = loader.getController();
            controller.setBookService(bookService);
            controller.setPatron(patron);
            controller.setBorrowService(borrowService);

            Stage stage = new Stage();
            stage.setTitle(patron.getName());
            stage.setScene(scene);
            stage.show();
        }

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/staff-view.fxml"));
        Scene scene = new Scene(loader.load());
        StaffViewController controller = loader.getController();
        controller.setBorrowService(borrowService);

        Stage stage = new Stage();
        stage.setTitle("Library Staff");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
