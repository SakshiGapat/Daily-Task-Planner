import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;

class Task {
    private String title;
    private String dueDate;
    private String priority;

    public Task(String title, String dueDate, String priority) {
        this.title = title;
        this.dueDate = dueDate;
        this.priority = priority;
    }

    public String getTitle() {
        return title;
    }

    public String getDueDate() {
        return dueDate;
    }

    public String getPriority() {
        return priority;
    }
}

public class DailyPlanner extends JFrame {
    private JTable taskTable;
    private DefaultTableModel tableModel;
    private java.util.List<Task> taskList;

    public DailyPlanner() {
        setTitle("Daily Planner");
        setSize(600, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Custom Styling
        Font font = new Font("Segoe UI", Font.PLAIN, 14);
        UIManager.put("Table.font", font);
        UIManager.put("Table.foreground", Color.BLACK);
        UIManager.put("Table.background", Color.WHITE);
        UIManager.put("Table.selectionBackground", new Color(0xADD8E6));
        UIManager.put("Table.gridColor", Color.GRAY);
        UIManager.put("Label.font", font);
        UIManager.put("Button.font", font);

        taskList = new ArrayList<>();
        tableModel = new DefaultTableModel(new Object[]{"Title", "Due Date", "Priority"}, 0);
        taskTable = new JTable(tableModel);
        taskTable.setRowHeight(25);
        taskTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 15));
        taskTable.getTableHeader().setBackground(new Color(0x003366));
        taskTable.getTableHeader().setForeground(Color.WHITE);
        taskTable.setShowGrid(true);

        JScrollPane scrollPane = new JScrollPane(taskTable);

        JButton addButton = new JButton("➕ Add Task");
        JButton deleteButton = new JButton("🗑 Delete Task");
        JButton sortButton = new JButton("📅 Sort by Due Date");

        addButton.setBackground(new Color(0x28a745));
        addButton.setForeground(Color.WHITE);
        deleteButton.setBackground(new Color(0xdc3545));
        deleteButton.setForeground(Color.WHITE);
        sortButton.setBackground(new Color(0x007bff));
        sortButton.setForeground(Color.WHITE);

        addButton.addActionListener(e -> addTask());
        deleteButton.addActionListener(e -> deleteTask());
        sortButton.addActionListener(e -> sortTasksByDate());

        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        panel.setBackground(new Color(0xf8f9fa));
        panel.add(addButton);
        panel.add(deleteButton);
        panel.add(sortButton);

        add(scrollPane, BorderLayout.CENTER);
        add(panel, BorderLayout.SOUTH);
    }

    private void addTask() {
        JTextField titleField = new JTextField();
        JComboBox<String> priorityBox = new JComboBox<>(new String[]{"Low", "Medium", "High"});
        JSpinner dateSpinner = new JSpinner(new SpinnerDateModel());
        dateSpinner.setEditor(new JSpinner.DateEditor(dateSpinner, "yyyy-MM-dd"));

        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("Title:"));
        panel.add(titleField);
        panel.add(new JLabel("Due Date:"));
        panel.add(dateSpinner);
        panel.add(new JLabel("Priority:"));
        panel.add(priorityBox);

        int result = JOptionPane.showConfirmDialog(null, panel, "Add Task", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            String title = titleField.getText();
            LocalDate dueDate = ((java.util.Date) dateSpinner.getValue()).toInstant()
                    .atZone(java.time.ZoneId.systemDefault()).toLocalDate();
            String priority = (String) priorityBox.getSelectedItem();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            Task task = new Task(title, dueDate.format(formatter), priority);
            taskList.add(task);
            tableModel.addRow(new Object[]{task.getTitle(), task.getDueDate(), task.getPriority()});
        }
    }

    private void deleteTask() {
        int selectedRow = taskTable.getSelectedRow();
        if (selectedRow >= 0) {
            taskList.remove(selectedRow);
            tableModel.removeRow(selectedRow);
        } else {
            JOptionPane.showMessageDialog(this, "Please select a task to delete.");
        }
    }

    private void sortTasksByDate() {
        taskList.sort(Comparator.comparing(Task::getDueDate));
        tableModel.setRowCount(0);
        for (Task task : taskList) {
            tableModel.addRow(new Object[]{task.getTitle(), task.getDueDate(), task.getPriority()});
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new DailyPlanner().setVisible(true));
    }
}
