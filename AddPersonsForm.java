
package ise503_final;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class AddPersonsForm extends JFrame {
    private JTextField firstNameField, lastNameField, birthDateField;

    public AddPersonsForm() {
        setTitle("Add Person");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        backgrounPanel background = new backgrounPanel("/ise503_final/background3.png");
        background.setLayout(null);

        JLabel titleLabel = new JLabel("Add New Person");
        titleLabel.setBounds(150, 30, 300, 30);
        titleLabel.setForeground(Color.BLACK);
        titleLabel.setFont(new Font("Comic Sans MS", Font.BOLD, 20));
        background.add(titleLabel);

        JLabel firstNameLabel = new JLabel("First Name:");
        firstNameLabel.setBounds(80, 100, 120, 25);
        firstNameLabel.setForeground(Color.BLACK);
        background.add(firstNameLabel);

        firstNameField = new JTextField();
        firstNameField.setBounds(200, 100, 200, 25);
        background.add(firstNameField);

        JLabel lastNameLabel = new JLabel("Last Name:");
        lastNameLabel.setBounds(80, 150, 120, 25);
        lastNameLabel.setForeground(Color.BLACK);
        background.add(lastNameLabel);

        lastNameField = new JTextField();
        lastNameField.setBounds(200, 150, 200, 25);
        background.add(lastNameField);

        JLabel birthDateLabel = new JLabel("Birth Date (YYYY-MM-DD):");
        birthDateLabel.setBounds(80, 200, 200, 25);
        birthDateLabel.setForeground(Color.BLACK);
        background.add(birthDateLabel);

        birthDateField = new JTextField();
        birthDateField.setBounds(250, 200, 150, 25);
        background.add(birthDateField);

        JButton submitButton = new JButton("Submit");
        submitButton.setBounds(180, 270, 120, 40);
        styleButton(submitButton);
        background.add(submitButton);

        submitButton.addActionListener(e -> insertPerson());

        setContentPane(background);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void insertPerson() {
        try (Connection conn = DBUtil.getConnection()) {
            String sql = "INSERT INTO Person (first_name, family_name, birth_date) VALUES (?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, firstNameField.getText());
            pstmt.setString(2, lastNameField.getText());
            pstmt.setString(3, birthDateField.getText());

            int rows = pstmt.executeUpdate();
            JOptionPane.showMessageDialog(this, rows > 0 ? "Person added!" : "Insert failed.");
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
