package net.codejava.sql;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import org.jdatepicker.JDatePanel;



public class MainMenu extends JFrame {

    private static final long serialVersionUID = 1L;
    private Connection connection;

    public MainMenu() {
        initialize();
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    void initialize() {
        setTitle("Main Menu");
        setSize(500, 100);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // центрирование окна на экране

        JPanel mainPanel = new JPanel(new BorderLayout());
        setContentPane(mainPanel);

        // Создаем панель для кнопок навигации
        JPanel navigationPanel = new JPanel();
        JButton projectsButton = new JButton("Projects");
        JButton tasksButton = new JButton("Tasks");
        JButton addProjectButton = new JButton("Add Project");
        JButton addTaskButton = new JButton("Add Task");
        JButton exitButton = new JButton("Exit");

        // Добавляем обработчики событий для кнопок навигации
        projectsButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                openProjectsWindow();
            }
        });
        tasksButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                openTasksWindow();
            }
        });
        addProjectButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showAddProjectForm();
            }
        });
        addTaskButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showAddTaskForm();
            }
        });
        exitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                exitApplication();
            }
        });

        // Добавляем кнопки на панель навигации
        navigationPanel.add(projectsButton);
        navigationPanel.add(tasksButton);
        navigationPanel.add(addProjectButton);
        navigationPanel.add(addTaskButton);
        navigationPanel.add(exitButton);

        // Добавляем панель навигации на главную панель
        mainPanel.add(navigationPanel, BorderLayout.NORTH);

        // Создаем панель для отображения списка проектов
          JPanel projectsPanel = new JPanel(new GridLayout(0, 1));
//        projectsPanel.setBackground(Color.WHITE);
//        JLabel projectLabel = new JLabel("Main Menu");
//        projectLabel.setFont(new Font("Tahoma", Font.BOLD, 20));
//        projectsPanel.add(projectLabel);

        // Вместо жесткого кодирования информации о проекте используем базу данных
        // Здесь можно добавить цикл для загрузки данных о проектах из базы данных и создания соответствующих кнопок для каждого проекта

        // Пример для двух проектов
       

        // Добавляем панель списка проектов на главную панель
        mainPanel.add(projectsPanel, BorderLayout.CENTER);
    }

    private void openProjectsWindow() {
        try {
            ProjectsWindowFrame projectsWindow = new ProjectsWindowFrame(this.connection);
            projectsWindow.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error opening projects window: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void openTasksWindow() {
        // Создаем экземпляр окна задач и отображаем его
        TasksWindow tasksWindow = new TasksWindow(connection); // замените null на ваше подключение к базе данных
        tasksWindow.setVisible(true);
    }

    private void showAddProjectForm() {
        JFrame addProjectFrame = new JFrame("Add New Project");
        addProjectFrame.setSize(300, 200);
        addProjectFrame.setLayout(new GridLayout(5, 2));

        JLabel nameLabel = new JLabel("Name:");
        JTextField nameField = new JTextField();
        JLabel descriptionLabel = new JLabel("Description:");
        JTextField descriptionField = new JTextField();
        JLabel startDateLabel = new JLabel("Start Date:");
        JTextField startDate = new JTextField();
        JLabel endDateLabel = new JLabel("End Date:");
        JTextField endDate = new JTextField();
        JButton submitButton = new JButton("Submit");

        submitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Получаем выбранные даты
                

                // Создаем новый проект, передавая выбра	нные даты
                Project newProject = new Project(nameField.getText(), descriptionField.getText(), startDate.getText(), endDate.getText());
                addProjectToDatabase(newProject);

                // Закрываем окно после добавления проекта
                addProjectFrame.dispose();
            }
        });

        addProjectFrame.add(nameLabel);
        addProjectFrame.add(nameField);
        addProjectFrame.add(descriptionLabel);
        addProjectFrame.add(descriptionField);
        addProjectFrame.add(startDateLabel);
        addProjectFrame.add(startDate);
        addProjectFrame.add(endDateLabel);
        addProjectFrame.add(endDate);
        addProjectFrame.add(new JLabel()); // Пустая ячейка для выравнивания
        addProjectFrame.add(submitButton);

        addProjectFrame.setVisible(true);
    }

    private void addProjectToDatabase(Project project) {
        // Реализация добавления объекта Project в базу данных
    }

    private void showAddTaskForm() {
        JFrame addTaskFrame = new JFrame("Add New Task");
        addTaskFrame.setSize(300, 150);
        addTaskFrame.setLayout(new GridLayout(3, 2));

        JLabel projectLabel = new JLabel("Select Project:");
        JComboBox<Project> projectDropdown = createProjectsDropdown();
        JLabel taskLabel = new JLabel("Task Description:");
        JTextField taskField = new JTextField();

        JButton submitButton = new JButton("Submit");
        submitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Получаем выбранный проект и описание задачи и добавляем задачу к проекту
                Project selectedProject = (Project) projectDropdown.getSelectedItem();
                String taskDescription = taskField.getText();
                addTaskToProject(selectedProject, taskDescription);
                addTaskFrame.dispose(); // Закрываем окно после добавления задачи
            }
        });

        addTaskFrame.add(projectLabel);
        addTaskFrame.add(projectDropdown);
        addTaskFrame.add(taskLabel);
        addTaskFrame.add(taskField);
        addTaskFrame.add(new JLabel()); // Пустая ячейка для выравнивания
        addTaskFrame.add(submitButton);

        addTaskFrame.setVisible(true);
    }

    private JComboBox<Project> createProjectsDropdown() {
        // Получаем список проектов из базы данных
        List<Project> projects = getProjectsFromDatabase();
        JComboBox<Project> projectsDropdown = new JComboBox<>(new Vector<>(projects));
        return projectsDropdown;
    }

    private List<Project> getProjectsFromDatabase() {
        // Реализация получения списка проектов из базы данных
        return new ArrayList<>(); // Возвращаем пустой список для примера
    }

    private void addTaskToProject(Project selectedProject, String taskDescription) {
        // Реализация добавления задачи к выбранному проекту в базу данных
    }

    private void exitApplication() {
        // Реализация выхода из приложения
        int option = JOptionPane.showConfirmDialog(this, "Are you sure you want to exit?", "Exit", JOptionPane.YES_NO_OPTION);
        if (option == JOptionPane.YES_OPTION) {
            dispose(); // Закрыть окно
        }
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                // Initialize your connection here
                Connection connection = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;instanceName=meirbek\\sqlexpress;databaseName=IT_management_system;encrypt=true;trustServerCertificate=true", "sa", "mikotanir");

                // Create an instance of MainMenu and set the connection
                MainMenu mainMenu = new MainMenu();
                mainMenu.setConnection(connection);

                // Make the main menu visible
                mainMenu.setVisible(true);
            } catch (SQLException e) {
                e.printStackTrace();
                // Handle connection error
                JOptionPane.showMessageDialog(null, "Error connecting to the database: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

	public void showMainMenu() {
		// TODO Auto-generated method stub
		
	}
}
