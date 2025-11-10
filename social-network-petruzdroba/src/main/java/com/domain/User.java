package com.domain;

public abstract class User{
    private long id;
    private String username;
    private String email;
    private String password;

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

    public String getPassword() {
        return password;
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
}