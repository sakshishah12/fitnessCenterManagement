package ise503_final;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class addTrainer extends JFrame {
    private JComboBox<String> personComboBox;
    private JTextField specialtyField;

    public addTrainer() {
        setTitle("Add Trainer");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        backgrounPanel background = new backgrounPanel("/ise503_final/background3.png");
        background.setLayout(null);

        JLabel titleLabel = new JLabel("Add New Trainer");
        titleLabel.setBounds(150, 30, 300, 30);
        titleLabel.setForeground(Color.BLACK);
        titleLabel.setFont(new Font("Comic Sans MS", Font.BOLD, 20));
        background.add(titleLabel);

        JLabel personLabel = new JLabel("Select Person:");
        personLabel.setBounds(80, 100, 120, 25);
        personLabel.setForeground(Color.BLACK);
        background.add(personLabel);

        personComboBox = new JComboBox<>();
        personComboBox.setBounds(200, 100, 200, 25);
        loadPersons();
        background.add(personComboBox);

        JLabel specialtyLabel = new JLabel("Specialty:");
        specialtyLabel.setBounds(80, 150, 120, 25);
        specialtyLabel.setForeground(Color.BLACK);
        background.add(specialtyLabel);

        specialtyField = new JTextField();
        specialtyField.setBounds(200, 150, 200, 25);
        background.add(specialtyField);

        JButton submitButton = new JButton("Submit");
        submitButton.setBounds(180, 220, 120, 40);
        styleButton(submitButton);
        background.add(submitButton);

        submitButton.addActionListener(e -> addTrainer());

        setContentPane(background);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void loadPersons() {
        try (Connection conn = DBUtil.getConnection()) {
            String sql = "SELECT person_id, first_name, family_name FROM Person";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                int id = rs.getInt("person_id");
                String name = rs.getString("first_name") + " " + rs.getString("family_name");
                personComboBox.addItem(id + " - " + name);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void addTrainer() {
        try (Connection conn = DBUtil.getConnection()) {
            String selectedPerson = (String) personComboBox.getSelectedItem();
            if (selectedPerson == null || specialtyField.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please select a person and enter specialty.");
                return;
            }

            int personId = Integer.parseInt(selectedPerson.split(" - ")[0]);
            String specialty = specialtyField.getText();

            String sql = "INSERT INTO Trainer (person_id, specialty) VALUES (?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, personId);
            pstmt.setString(2, specialty);

            int rows = pstmt.executeUpdate();
            JOptionPane.showMessageDialog(this, rows > 0 ? "Trainer added!" : "Insert failed.");
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void styleButton(JButton button) {
        button.setBackground(new Color(255, 182, 193));
        button.setForeground(Color.BLACK);
        button.setFont(new Font("Verdana", Font.BOLD, 14));

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(255, 105, 180));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(255, 182, 193));
            }
        });
    }
}
