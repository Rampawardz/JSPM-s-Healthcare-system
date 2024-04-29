package PatientRegistrationForm;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class PatientRegistrationFormGUI extends JFrame {
    private static final String url = "jdbc:mysql://localhost:3306/Healthcamp";
    private static final String username = "root";
    private static final String password = "RAM3211";

    private Connection connection;

    public PatientRegistrationFormGUI() {
        setTitle("JSPM's HEALTHCARE SYSTEM");
        setSize(800, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JButton addPatientButton = new JButton("Add Patient");
        JButton viewPatientsButton = new JButton("View Patients");
        JButton exitButton = new JButton("Exit");

        addPatientButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addPatient();
            }
        });

        viewPatientsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                viewPatients();
            }
        });

        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("THANK YOU! FOR USING JSPM's HEALTHCARE SYSTEM!!");
                dispose();
            }
        });

        JPanel panel = new JPanel(new GridLayout(3, 1));
        panel.add(addPatientButton);
        panel.add(viewPatientsButton);
        panel.add(exitButton);

        add(panel);
        setVisible(true);

        try {
            connection = DriverManager.getConnection(url, username, password);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void addPatient() {
        String name = JOptionPane.showInputDialog("Enter Patient Name:");
        String ageStr = JOptionPane.showInputDialog("Enter Patient Age:");
        int age = Integer.parseInt(ageStr);
        String gender = JOptionPane.showInputDialog("Enter Patient Gender:");

        try {
            String query = "INSERT INTO patient(name, age, gender) VALUES(?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, name);
            preparedStatement.setInt(2, age);
            preparedStatement.setString(3, gender);
            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows > 0) {
                JOptionPane.showMessageDialog(this, "Patient Added Successfully!!");
            } else {
                JOptionPane.showMessageDialog(this, "Failed to add Patient!!");
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void viewPatients() {
        String query = "select * from patient";
        StringBuilder result = new StringBuilder();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();
            result.append("Patients: \n");
            result.append("+------------+--------------------+----------+------------+\n");
            result.append("| Patient Id | Name               | Age      | Gender     |\n");
            result.append("+------------+--------------------+----------+------------+\n");
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                int age = resultSet.getInt("age");
                String gender = resultSet.getString("gender");
                result.append(String.format("| %-10s | %-18s | %-8s | %-10s |\n", id, name, age, gender));
                result.append("+------------+--------------------+----------+------------+\n");
            }
            JOptionPane.showMessageDialog(this, result.toString());

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new PatientRegistrationFormGUI();
            }
        });
    }
}
