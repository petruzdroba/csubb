package org.zdroba;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.zdroba.controllers.AuthViewController;
import org.zdroba.repository.*;
import org.zdroba.service.AuthServiceImpl;
import org.zdroba.service.RaceEventServiceImpl;
import org.zdroba.service.RacerServiceImpl;

public class App extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/auth-view.fxml"));
        Scene scene = new Scene(loader.load());

        UserRepository authRepo = UserRepositoryImpl.getInstance();
        RacerRepository racerRepo = RacerRepositoryImpl.getInstance();
        RaceEventRepository raceEventRepo = RaceEventRepositoryImpl.getInstance();

        AuthViewController controller = loader.getController();
        controller.setAuthService(new AuthServiceImpl(authRepo));
        controller.setRacerService(new RacerServiceImpl(racerRepo, raceEventRepo));
        controller.setRaceEventService(new RaceEventServiceImpl(raceEventRepo));

        primaryStage.setTitle("Race App");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}