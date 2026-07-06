using Avalonia;
using Avalonia.Controls.ApplicationLifetimes;
using Avalonia.Markup.Xaml;
using moto_c.controllers;
using moto_c.repository;
using moto_c.service;
using moto_c.sync;

namespace moto_c;

public partial class App : Application
{
    public override void Initialize()
    {
        AvaloniaXamlLoader.Load(this);
    }

    public override void OnFrameworkInitializationCompleted()
    {
        if (ApplicationLifetime is IClassicDesktopStyleApplicationLifetime desktop)
        {
            var notifier = new SocketNotifier(
                AppConfig.ClientPort,
                "127.0.0.1",
                6000
            );

            notifier.Start();

            var authRepo = new UserRepositoryImpl();
            var raceEventRepo = new RaceEventRepositoryImpl();
            var racerRepo = new RacerRepositoryImpl(raceEventRepo);

            var controller = new AuthViewController();
            controller.setAuthService(new AuthServiceImpl(authRepo));
            controller.setRacerService(new RacerServiceImpl(racerRepo, raceEventRepo, notifier));
            controller.setRaceEventService(new RaceEventServiceImpl(raceEventRepo, notifier));
            controller.setNotifier(notifier);

            var mainWindow = new MainWindow();
            mainWindow.AuthView.setAuthService(new AuthServiceImpl(authRepo));
            mainWindow.AuthView.setRacerService(new RacerServiceImpl(racerRepo, raceEventRepo, notifier));
            mainWindow.AuthView.setRaceEventService(new RaceEventServiceImpl(raceEventRepo, notifier));
            mainWindow.AuthView.setNotifier(notifier);
            desktop.MainWindow = mainWindow;
        }

        base.OnFrameworkInitializationCompleted();
    }
}