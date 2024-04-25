package net.codejava.sql;

public class Project {
    private String name;
    private String description;
    private String deadline;
    private String price;

    public Project(String name, String description, String deadline, String string) {
        this.name = name;
        this.description = description;
        this.deadline = deadline;
        this.price = string;
    }

    // Геттеры и сеттеры для полей класса

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "Project{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", deadline='" + deadline + '\'' +
                ", price=" + price +
                '}';
    }
}
