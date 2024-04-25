package net.codejava.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TaskDAO {
    private Connection connection;

    public TaskDAO(Connection connection) {
        this.connection = connection;
    }

    public List<Task> getAllTasks() throws SQLException {
        List<Task> tasks = new ArrayList<>();
        String query = "SELECT * FROM Tasks";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Task task = new Task(
                        resultSet.getInt("id"),
                        resultSet.getString("description"),
                        resultSet.getInt("project_id")
                );
                tasks.add(task);
            }
        }
        return tasks;
    }
}