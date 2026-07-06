using Avalonia.Controls;
using Avalonia.Markup.Xaml;
using moto_c.controllers;
using moto_c.repository;
using moto_c.service;

namespace moto_c;

public partial class MainWindow : Window
{
    public MainWindow()
    {
        AvaloniaXamlLoader.Load(this);

        var authView = this.FindControl<AuthViewController>("authView");

        var authRepo = UserRepositoryImpl.getInstance();
        var raceEventRepo =  RaceEventRepositoryImpl.getInstance();
        var racerRepo = RacerRepositoryImpl.getInstance();

        var authService = new AuthServiceImpl(authRepo);
        var racerService = new RacerServiceImpl(racerRepo, raceEventRepo);
        var raceEventService = new RaceEventServiceImpl(raceEventRepo);

        authView!.setAuthService(authService);
        authView!.setRacerService(racerService);
        authView!.setRaceEventService(raceEventService);
    }
}