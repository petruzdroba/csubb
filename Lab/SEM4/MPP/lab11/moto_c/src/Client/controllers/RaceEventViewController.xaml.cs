using System;
using Avalonia.Controls;
using Avalonia.Interactivity;
using Avalonia.Markup.Xaml;
using System.Collections.ObjectModel;
using Avalonia.Threading;
using moto_c.Common.sync;
using moto_c.entity;
using moto_c.exceptions;
using moto_c.service;
using moto_c.sync;

namespace moto_c.controllers
{
    public class RaceEventRow
    {
        public string EngineDisplay { get; set; } = "";
        public int ParticipantCount { get; set; }
    }

    public partial class RaceEventViewController : UserControl, Observer
    {
        private User? loggedInUser;
        private RaceEventService raceEventService = null!;
        private RacerService racerService = null!;
        private SocketNotifier notifier = null!;

        private ObservableCollection<RaceEventRow> rows = new();

        public RaceEventViewController()
        {
            AvaloniaXamlLoader.Load(this);
            DataContext = this;

            var list = this.FindControl<ListBox>("eventList");
            if (list != null) list.ItemsSource = rows;
        }

        public void setLoggedInUser(User user) => loggedInUser = user;
        public void setRacerService(RacerService rs) => racerService = rs;
        
        public void setNotifier(SocketNotifier n)
        {
            notifier = n;
            
            this.notifier.OnUpdate(message =>
            {
                Avalonia.Threading.Dispatcher.UIThread.Post(() =>
                {
                    HandleMessage(message);
                });
            });
            
            this.notifier.Start();
        }
        

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

        private void HandleMessage(string message)
        {
            if (TreatResponse(message)) return;

            Request request = GetRequest(message);
            if (request == null) return;

            switch (request.type)
            {
                case RequestType.RACER_ADD:
                case RequestType.EVENT_ADD:
                    Dispatcher.UIThread.Post(() => loadData());
                    break;

                case RequestType.RACER_UPDATE:
                    Dispatcher.UIThread.Post(() =>
                        Console.WriteLine("Event added: " + request.message)
                    );
                    break;

                case RequestType.AUTH_LOGIN:
                case RequestType.AUTH_LOGOUT:
                    Dispatcher.UIThread.Post(() =>
                        Console.WriteLine("Auth response: " + request.message)
                    );
                    break;

                default:
                    Console.WriteLine("Unknown request: " + request);
                    break;
            }

            notifier.Respond(request.type.ToString(), ResponseType.OK, "Processed successfully");
        }

        private Request? GetRequest(string message)
        {
            try
            {
                return Request.FromString(message);
            }
            catch (ArgumentException)
            {
                Console.Error.WriteLine("Invalid request format: " + message);
                notifier.Respond("Invalid", ResponseType.ERROR, "Invalid Format");
                return null;
            }
        }

        private static bool TreatResponse(string message)
        {
            if (message.StartsWith("RESPONSE:"))
            {
                try
                {
                    Response response = Response.FromString(message);
                    Console.WriteLine("[Response] " +
                                      response.requestType + " -> " +
                                      response.type + " : " +
                                      response.message);
                }
                catch (Exception)
                {
                    Console.Error.WriteLine("Invalid response format: " + message);
                }
                return true;
            }

            return false;
        }

        public void update(string message)
        {
            loadData();
        }
    }
}