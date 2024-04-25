package net.codejava.sql;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.sql.Date;



public class ProjectsWindowFrame extends JFrame {
    private Connection connection;
    private JTable table;
    private JTextField searchField;

    public ProjectsWindowFrame(Connection connection) {
        this.connection = connection;
        initialize();
    }

    void initialize() {
        setTitle("Projects");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout());
        setContentPane(mainPanel);

        JPanel searchPanel = new JPanel();
        searchField = new JTextField(20);
        JButton searchButton = new JButton("Search");
        searchButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                searchProjects();
            }
        });
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        mainPanel.add(searchPanel, BorderLayout.NORTH);

        JPanel projectsPanel = new JPanel();
        projectsPanel.setBackground(Color.WHITE);
        mainPanel.add(new JScrollPane(projectsPanel), BorderLayout.CENTER);

        displayProjects(projectsPanel);
    }

    private void displayProjects(JPanel projectsPanel) {
        try {
            List<Project> projects = getAllProjects();

            // Создаем массив данных для таблицы
            String[] columnNames = {"project_id", "name", "description", "status", "start_date", "end_date", "budget"};
            Object[][] data = new Object[projects.size()][columnNames.length];

            for (int i = 0; i < projects.size(); i++) {
                Project project = projects.get(i);
                data[i][0] = project.getId();
                data[i][1] = project.getName();
                data[i][2] = project.getDescription();
                data[i][3] = project.getStatus();
                data[i][4] = project.getStartDate();
                data[i][5] = project.getEndDate();
                data[i][6] = project.getBudget();
            }

            // Создаем таблицу с данными
            DefaultTableModel model = new DefaultTableModel(data, columnNames);
            table = new JTable(model);
            JScrollPane scrollPane = new JScrollPane(table);
            projectsPanel.setLayout(new BorderLayout());
            projectsPanel.add(scrollPane, BorderLayout.CENTER);

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading projects: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

        projectsPanel.revalidate();
        projectsPanel.repaint();
    }

    private void searchProjects() {
        String keyword = searchField.getText().trim();
        if (!keyword.isEmpty()) {
            try {
                List<Project> projects = searchProjects(keyword);
                updateTable(projects);
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error searching projects: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    public class Project {
        private int id;
        private String name;
        private String description;
        private String status;
        private Date startDate;
        private Date endDate;
        private double budget;

        // Геттеры
        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getDescription() {
            return description;
        }

        public String getStatus() {
            return status;
        }

        public Date getStartDate() {
            return startDate;
        }

        public Date getEndDate() {
            return endDate;
        }

        public double getBudget() {
            return budget;
        }

        // Сеттеры
        public void setId(int id) {
            this.id = id;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public void setStartDate(Date startDate) {
            this.startDate = startDate;
        }

        public void setEndDate(Date endDate) {
            this.endDate = endDate;
        }

        public void setBudget(double budget) {
            this.budget = budget;
        }
    }
    
    private void updateTable(List<Project> projects) {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0); // Очищаем таблицу перед обновлением данных
        for (Project project : projects) {
            Object[] row = {project.getId(), project.getName(), project.getDescription(), project.getStatus(),
                            project.getStartDate(), project.getEndDate(), project.getBudget()};
            model.addRow(row); // Добавляем строку с данными в таблицу
        }
    }

    private List<Project> getAllProjects() throws SQLException {
        List<Project> projects = new ArrayList<>();
        String query = "SELECT * FROM projects";
        try (PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                Project project = createProjectFromResultSet(resultSet);
                projects.add(project);
            }
        }
        return projects;
    }
    
    private List<Project> searchProjects(String keyword) throws SQLException {
        List<Project> projects = new ArrayList<>();
        String query = "SELECT * FROM projects WHERE name LIKE ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, "%" + keyword + "%");
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Project project = createProjectFromResultSet(resultSet);
                    projects.add(project);
                }
            }
        }
        return projects;
    }

    private Project createProjectFromResultSet(ResultSet resultSet) throws SQLException {
        Project project = new Project();
        project.setId(resultSet.getInt("project_id"));
        project.setName(resultSet.getString("name"));
        project.setDescription(resultSet.getString("description"));
        project.setStatus(resultSet.getString("status"));
        project.setStartDate(resultSet.getDate("start_date"));
        project.setEndDate(resultSet.getDate("end_date"));
        project.setBudget(resultSet.getDouble("budget"));
        return project;
    }
}
