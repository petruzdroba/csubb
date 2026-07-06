package org.zdroba;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.zdroba.controllers.AuthViewController;
import org.zdroba.repository.*;
import org.zdroba.service.*;
import org.zdroba.sync.SocketNotifier;

public class App extends Application {

    public static int CLIENT_PORT = 5000;
    private static final int SERVER_PORT = 6000;

    @Override
    public void start(Stage primaryStage) throws Exception {
        SocketNotifier notifier = new SocketNotifier(CLIENT_PORT, "127.0.0.1", SERVER_PORT);
        notifier.start();

        Stage stage = createWindow("Race App - Client Port " + CLIENT_PORT, notifier);
        stage.show();
    }

    private Stage createWindow(String title, SocketNotifier notifier) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/auth-view.fxml"));
        Scene scene = new Scene(loader.load());

        UserRepository authRepo = new UserRepositoryImpl();
        RaceEventRepository raceEventRepo = new RaceEventRepositoryImpl();
        RacerRepository racerRepo = new RacerRepositoryImpl(raceEventRepo);

        AuthViewController controller = loader.getController();
        controller.setAuthService(new AuthServiceImpl(authRepo));
        controller.setRacerService(new RacerServiceImpl(racerRepo, raceEventRepo, notifier));
        controller.setRaceEventService(new RaceEventServiceImpl(raceEventRepo, notifier));
        controller.setNotifier(notifier);

        Stage stage = new Stage();
        stage.setTitle(title);
        stage.setScene(scene);
        return stage;
    }
}