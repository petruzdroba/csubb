package com.domain;

public abstract class User{
    private final long id;
    private final String username;
    private final String email;
    private final String password;

    public User(long id, String username, String email, String password) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    /**
     * Called when this user is notified about an event.
     *
     * @param e the event that triggered the notification
     * @see Event#notifySubscribers()
     */
    public void notify(Event e) {
        System.out.println(this.getUsername() + e.notification());
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                '}';
    }

    public String getPassword() {
        return password;
    }
}