package net.codejava.sql;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.awt.*;
import java.sql.*;

public class TasksWindow extends JFrame {
    private Connection connection;
    private JTable table;

    public TasksWindow(Connection connection) {
        this.connection = connection;
        initialize();
    }

    private void loadTasks() {
        TaskDAO taskDAO = new TaskDAO(connection);
        try {
            java.util.List<Task> tasks = taskDAO.getAllTasks();
            DefaultTableModel model = new DefaultTableModel(new String[]{"ID", "Description", "Project ID"}, 0);
            for (Object obj : tasks) {
                Task task = (Task) obj;
                Object[] row = {task.getId(), task.getDescription(), task.getProjectId()};
                model.addRow(row);
            }
            table.setModel(model);
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading tasks: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }    
    private void initialize() {
        setTitle("Tasks");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null); // Center the window on the screen

        JPanel mainPanel = new JPanel(new BorderLayout());
        setContentPane(mainPanel);

        // Создаем дерево для отображения проектов и связанных с ними задач
        JTree projectTree = new JTree();
        mainPanel.add(new JScrollPane(projectTree), BorderLayout.CENTER);

        // Загружаем и отображаем проекты и задачи
        loadProjectsAndTasks(projectTree);
    }

    private void loadProjectsAndTasks(JTree projectTree) {
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("Projects");

        try {
            String query = "SELECT * FROM Projects";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int projectId = resultSet.getInt("project_id");
                String projectName = resultSet.getString("name");
                DefaultMutableTreeNode projectNode = new DefaultMutableTreeNode(projectName);

                // Загружаем задачи для каждого проекта
                loadTasksForProject(projectNode, projectId);

                root.add(projectNode);
            }

            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        projectTree.setModel(new DefaultTreeModel(root));
    }

    private void loadTasksForProject(DefaultMutableTreeNode projectNode, int projectId) {
        try {
            String query = "SELECT * FROM Tasks WHERE project_id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, projectId);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                String taskName = resultSet.getString("task_name");
                String taskDescription = resultSet.getString("task_description");
                DefaultMutableTreeNode taskNode = new DefaultMutableTreeNode(taskName + ": " + taskDescription);

                projectNode.add(taskNode);
            }

            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                Connection connection = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;instanceName=meirbek\\sqlexpress;databaseName=IT_management_system;encrypt=true;trustServerCertificate=true", "sa", "mikotanir");
                TasksWindow tasksWindow = new TasksWindow(connection); 
                tasksWindow.setVisible(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }
}
