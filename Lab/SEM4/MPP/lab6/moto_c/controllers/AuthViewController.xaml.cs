using Avalonia.Controls;
using Avalonia.Interactivity;
using Avalonia.Markup.Xaml;
using moto_c.entity;
using moto_c.exceptions;
using moto_c.service;

namespace moto_c.controllers
{
    public partial class AuthViewController : UserControl
    {
        private User? loggedInUser;
        private AuthService authService = null!;
        private RacerService racerService = null!;
        private RaceEventService raceEventService = null!;

        public AuthViewController()
        {
            AvaloniaXamlLoader.Load(this);
        }

        public void setAuthService(AuthService auth) => authService = auth;
        public void setRacerService(RacerService rs) => racerService = rs;
        public void setRaceEventService(RaceEventService res) => raceEventService = res;

        private async void logIn(object? sender, RoutedEventArgs e)
        {
            var emailField = this.FindControl<TextBox>("loginEmailField");
            var passwordField = this.FindControl<TextBox>("loginPasswordField");

            string email = emailField?.Text ?? "";
            string password = passwordField?.Text ?? "";

            try
            {
                loggedInUser = authService.logIn(email, password);
                Console.WriteLine(loggedInUser);

                openRacerView(loggedInUser);
                openRaceEventView(loggedInUser);
            }
            catch (InvalidPasswordException ex) { await showError(ex.Message); }
            catch (NotFoundException ex) { await showError(ex.Message); }
            catch (Exception ex)
            {
                Console.WriteLine(ex.StackTrace);
                await showError("Unexpected error: " + ex.Message);
            }
            finally
            {
                if (emailField != null) emailField.Text = "";
                if (passwordField != null) passwordField.Text = "";
            }
        }

        private async void register(object? sender, RoutedEventArgs e)
        {
            var emailField = this.FindControl<TextBox>("registerEmailField");
            var passwordField = this.FindControl<TextBox>("registerPasswordField");

            string email = emailField?.Text ?? "";
            string password = passwordField?.Text ?? "";

            try
            {
                loggedInUser = authService.register(email, password);
                Console.WriteLine(loggedInUser);

                openRacerView(loggedInUser);
                openRaceEventView(loggedInUser);
            }
            catch (AlreadyExistsException ex) { await showError(ex.Message); }
            catch (Exception ex)
            {
                Console.WriteLine(ex.StackTrace);
                await showError("Unexpected error: " + ex.Message);
            }
            finally
            {
                if (emailField != null) emailField.Text = "";
                if (passwordField != null) passwordField.Text = "";
            }
        }

        private async Task showError(string message)
        {
            Console.WriteLine(message);
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
            if (parent != null)
                await dialog.ShowDialog(parent);
            else
                dialog.Show();
        }

        private void openRacerView(User user)
        {
            Console.WriteLine("rege");
            var window = new Window
            {
                Title = "Racer View",
                Width = 800,
                Height = 600
            };
            
            var controller = new RacerViewController();
            controller.setLoggedInUser(user);
            controller.setRacerService(racerService);
            
            window.Content = controller;
            window.Show();
        }

        private void openRaceEventView(User user)
        {
            var controller = new RaceEventViewController();
            controller.setLoggedInUser(user);
            controller.setRacerService(racerService);

            var window = new Window
            {
                Title = "Race Event View",
                Width = 800,
                Height = 600,
                Content = controller
            };

            controller.setRaceEventService(raceEventService);
            window.Show();
        }
    }
}