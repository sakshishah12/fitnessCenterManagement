package ise503_final;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class addPaymentForm extends JFrame {
    private JComboBox<String> memberComboBox;
    private JTextField amountField, paymentDateField;

    public addPaymentForm() {
        setTitle("Record Payment");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        backgrounPanel background = new backgrounPanel("/ise503_final/background3.png");
        background.setLayout(null);

        JLabel titleLabel = new JLabel("Record Member Payment");
        titleLabel.setBounds(130, 30, 300, 30);
        titleLabel.setForeground(Color.BLACK);
        titleLabel.setFont(new Font("Comic Sans MS", Font.BOLD, 20));
        background.add(titleLabel);

        JLabel memberLabel = new JLabel("Select Member:");
        memberLabel.setBounds(80, 100, 120, 25);
        memberLabel.setForeground(Color.BLACK);
        background.add(memberLabel);

        memberComboBox = new JComboBox<>();
        memberComboBox.setBounds(200, 100, 200, 25);
        loadMembers();
        background.add(memberComboBox);

        JLabel amountLabel = new JLabel("Amount:");
        amountLabel.setBounds(80, 150, 120, 25);
        amountLabel.setForeground(Color.BLACK);
        background.add(amountLabel);

        amountField = new JTextField();
        amountField.setBounds(200, 150, 200, 25);
        background.add(amountField);

        JLabel dateLabel = new JLabel("Payment Date (YYYY-MM-DD):");
        dateLabel.setBounds(80, 200, 200, 25);
        dateLabel.setForeground(Color.BLACK);
        background.add(dateLabel);

        paymentDateField = new JTextField();
        paymentDateField.setBounds(280, 200, 120, 25);
        background.add(paymentDateField);

        JButton submitButton = new JButton("Submit");
        submitButton.setBounds(180, 270, 120, 40);
        styleButton(submitButton);
        background.add(submitButton);

        submitButton.addActionListener(e -> recordPayment());

        setContentPane(background);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void loadMembers() {
        try (Connection conn = DBUtil.getConnection()) {
            String sql = "SELECT member_id FROM Member";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                int id = rs.getInt("member_id");
                memberComboBox.addItem(String.valueOf(id));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void recordPayment() {
        try (Connection conn = DBUtil.getConnection()) {
            String selectedMember = (String) memberComboBox.getSelectedItem();
            String amountText = amountField.getText();
            String paymentDate = paymentDateField.getText();

            if (selectedMember == null || amountText.isEmpty() || paymentDate.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill all fields.");
                return;
            }

            int memberId = Integer.parseInt(selectedMember);
            double amount = Double.parseDouble(amountText);

            String sql = "INSERT INTO Payment (member_id, amount, payment_date) VALUES (?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, memberId);
            pstmt.setDouble(2, amount);
            pstmt.setString(3, paymentDate);

            int rows = pstmt.executeUpdate();
            JOptionPane.showMessageDialog(this, rows > 0 ? "Payment recorded!" : "Insert failed.");

        } catch (NumberFormatException nfe) {
            JOptionPane.showMessageDialog(this, "Please enter a valid amount.");
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
