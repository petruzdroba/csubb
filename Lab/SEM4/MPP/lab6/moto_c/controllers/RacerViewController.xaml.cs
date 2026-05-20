using Avalonia.Controls;
using Avalonia.Interactivity;
using Avalonia.Markup.Xaml;
using System.Collections.ObjectModel;
using moto_c.entity;
using moto_c.exceptions;
using moto_c.service;

namespace moto_c.controllers
{
    public class RacerRow
    {
        public string Name { get; set; } = "";
        public string Cnp { get; set; } = "";
        public string Engine { get; set; } = "";
        public string Team { get; set; } = "";
        public long Id { get; set; }
        public Team RawTeam { get; set; }
    }

    public partial class RacerViewController : UserControl
    {
        private User? loggedInUser;
        private RacerService racerService = null!;
        private ObservableCollection<RacerRow> rows = new();
        private RacerRow? selectedRow;

        public RacerViewController()
        {
            AvaloniaXamlLoader.Load(this);

            var list = this.FindControl<ListBox>("racerList");
            if (list != null)
            {
                list.ItemsSource = rows;
                list.SelectionChanged += onSelectionChanged;
            }

            var teamValues = Enum.GetNames(typeof(Team)).ToList();
            var allTeams = new List<string> { "ALL" }.Concat(teamValues).ToList();

            var teamComboBox = this.FindControl<ComboBox>("teamComboBox");
            if (teamComboBox != null)
            {
                teamComboBox.ItemsSource = allTeams;
                teamComboBox.SelectedItem = "ALL";
            }

            var teamFormComboBox = this.FindControl<ComboBox>("teamFormComboBox");
            if (teamFormComboBox != null)
                teamFormComboBox.ItemsSource = teamValues;

            var modifyTeamComboBox = this.FindControl<ComboBox>("modifyTeamComboBox");
            if (modifyTeamComboBox != null)
                modifyTeamComboBox.ItemsSource = teamValues;
        }

        public void setLoggedInUser(User user) => loggedInUser = user;

        public void setRacerService(RacerService rs)
        {
            racerService = rs;
            loadData(racerService.getAll());
        }

        private void onSelectionChanged(object? sender, SelectionChangedEventArgs e)
        {
            var list = this.FindControl<ListBox>("racerList");
            selectedRow = list?.SelectedItem as RacerRow;
            if (selectedRow == null) return;

            var modifyNameField = this.FindControl<TextBox>("modifyNameField");
            var modifyCnpField = this.FindControl<TextBox>("modifyCnpField");
            var modifyEngineField = this.FindControl<TextBox>("modifyEngineField");
            var modifyTeamComboBox = this.FindControl<ComboBox>("modifyTeamComboBox");

            if (modifyNameField != null) modifyNameField.Text = selectedRow.Name;
            if (modifyCnpField != null) modifyCnpField.Text = selectedRow.Cnp;
            if (modifyEngineField != null) modifyEngineField.Text = selectedRow.Engine;
            if (modifyTeamComboBox != null) modifyTeamComboBox.SelectedItem = selectedRow.Team;
        }

        private void loadData(List<Racer> racers)
        {
            rows.Clear();
            foreach (var r in racers)
            {
                rows.Add(new RacerRow
                {
                    Id = r.id,
                    Name = r.name,
                    Cnp = r.cnp,
                    Engine = r.engine.engine.ToString() + "cc",
                    Team = r.team.ToString(),
                    RawTeam = r.team
                });
            }
        }

        private void saveRacer(object? sender, RoutedEventArgs e)
        {
            var nameField = this.FindControl<TextBox>("nameField");
            var cnpField = this.FindControl<TextBox>("cnpField");
            var engineField = this.FindControl<TextBox>("engineField");
            var teamFormComboBox = this.FindControl<ComboBox>("teamFormComboBox");

            try
            {
                string name = nameField?.Text ?? "";
                string cnp = cnpField?.Text ?? "";
                int engine = int.Parse(engineField?.Text ?? "");
                Team team = Enum.Parse<Team>(teamFormComboBox?.SelectedItem?.ToString() ?? "");

                racerService.add(name, cnp, engine, team);
                loadData(racerService.getAll());
            }
            catch (AlreadyExistsException ex) { showError(ex.Message); }
            catch (NotFoundException ex) { showError(ex.Message); }
            catch (Exception ex)
            {
                Console.WriteLine(ex.StackTrace);
                showError(ex.Message);
            }
            finally
            {
                if (nameField != null) nameField.Text = "";
                if (cnpField != null) cnpField.Text = "";
                if (engineField != null) engineField.Text = "";
            }
        }

        private void modifyRacer(object? sender, RoutedEventArgs e)
        {
            var modifyTeamComboBox = this.FindControl<ComboBox>("modifyTeamComboBox");
            var modifyNameField = this.FindControl<TextBox>("modifyNameField");
            var modifyCnpField = this.FindControl<TextBox>("modifyCnpField");
            var modifyEngineField = this.FindControl<TextBox>("modifyEngineField");
            var list = this.FindControl<ListBox>("racerList");

            if (selectedRow == null)
            {
                showError("Please select a racer to modify!");
                return;
            }

            try
            {
                Team team = Enum.Parse<Team>(modifyTeamComboBox?.SelectedItem?.ToString() ?? "");
                racerService.modify(selectedRow.Id, team);
                loadData(racerService.getAll());
            }
            catch (NotFoundException ex) { showError(ex.Message); }
            catch (Exception ex)
            {
                Console.WriteLine(ex.StackTrace);
                showError(ex.Message);
            }
            finally
            {
                if (modifyNameField != null) modifyNameField.Text = "";
                if (modifyCnpField != null) modifyCnpField.Text = "";
                if (modifyEngineField != null) modifyEngineField.Text = "";
                if (modifyTeamComboBox != null) modifyTeamComboBox.SelectedItem = null;
                if (list != null) list.SelectedItem = null;
                selectedRow = null;
            }
        }

        private void searchByTeam(object? sender, RoutedEventArgs e)
        {
            var teamComboBox = this.FindControl<ComboBox>("teamComboBox");
            string? selected = teamComboBox?.SelectedItem?.ToString();

            if (selected == null || selected == "ALL")
            {
                loadData(racerService.getAll());
                return;
            }

            Team team = Enum.Parse<Team>(selected);
            loadData(racerService.find(team));
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