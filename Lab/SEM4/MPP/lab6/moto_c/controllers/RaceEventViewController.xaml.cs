using Avalonia.Controls;
using Avalonia.Interactivity;
using Avalonia.Markup.Xaml;
using System.Collections.ObjectModel;
using moto_c.entity;
using moto_c.exceptions;
using moto_c.service;

namespace moto_c.controllers
{
    public class RaceEventRow
    {
        public string EngineDisplay { get; set; } = "";
        public int ParticipantCount { get; set; }
    }

    public partial class RaceEventViewController : UserControl
    {
        private User? loggedInUser;
        private RaceEventService raceEventService = null!;
        private RacerService racerService = null!;

        private ObservableCollection<RaceEventRow> rows = new();

        public RaceEventViewController()
        {
            AvaloniaXamlLoader.Load(this);

            var list = this.FindControl<ListBox>("eventList");
            if (list != null) list.ItemsSource = rows;
        }

        public void setLoggedInUser(User user) => loggedInUser = user;
        public void setRacerService(RacerService rs) => racerService = rs;

        public void setRaceEventService(RaceEventService res)
        {
            raceEventService = res;
            loadData();
        }

        private void loadData()
        {
            rows.Clear();
            foreach (var e in raceEventService.getAll())
            {
                rows.Add(new RaceEventRow
                {
                    EngineDisplay = e.engine + "cc",
                    ParticipantCount = racerService.getAll(e.engine).Count
                });
            }
        }

        private void addEvent(object? sender, RoutedEventArgs e)
        {
            var engineField = this.FindControl<TextBox>("engineField");
            try
            {
                int engine = int.Parse(engineField?.Text ?? "");
                raceEventService.add(engine);
                loadData();
            }
            catch (FormatException)
            {
                showError("Engine must be a number!");
            }
            catch (AlreadyExistsException ex)
            {
                showError(ex.Message);
            }
            catch (Exception ex)
            {
                Console.WriteLine(ex.StackTrace);
                showError(ex.Message);
            }
            finally
            {
                if (engineField != null) engineField.Text = "";
            }
        }

        private void showError(string message)
        {
            var dialog = new Window
            {
                Title = "Error",
                Width = 300,
                Height = 150,
                WindowStartupLocation = WindowStartupLocation.CenterOwner,
                Content = new TextBlock
                {
                    Text = message,
                    HorizontalAlignment = Avalonia.Layout.HorizontalAlignment.Center,
                    VerticalAlignment = Avalonia.Layout.VerticalAlignment.Center,
                    TextWrapping = Avalonia.Media.TextWrapping.Wrap,
                    Margin = new Avalonia.Thickness(20)
                }
            };

            var parent = TopLevel.GetTopLevel(this) as Window;
            if (parent != null) dialog.ShowDialog(parent);
            else dialog.Show();
        }
    }
}