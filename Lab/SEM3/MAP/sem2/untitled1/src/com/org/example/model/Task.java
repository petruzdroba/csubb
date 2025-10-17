package com.org.example.model;

public abstract class Task {
    private final String taskID;
    private String description ;

    protected Task(String taskID, String description) {
        this.taskID = taskID;
        this.description = description;
    }

    public abstract void execute();

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    public String getTaskID() {
        return taskID;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public String toString() {
        return "[%s]->%s".formatted(taskID, description);
    }
}
