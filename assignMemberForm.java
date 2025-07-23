package ise503_final;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class assignMemberForm extends JFrame {
    private JComboBox<String> memberComboBox, sessionComboBox;

    public assignMemberForm() {
        setTitle("Assign Member to Session");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        backgrounPanel background = new backgrounPanel("/ise503_final/background3.png");
        background.setLayout(null);

        JLabel titleLabel = new JLabel("Assign Member to Session");
        titleLabel.setBounds(120, 30, 300, 30);
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

        JLabel sessionLabel = new JLabel("Select Session:");
        sessionLabel.setBounds(80, 150, 120, 25);
        sessionLabel.setForeground(Color.BLACK);
        background.add(sessionLabel);

        sessionComboBox = new JComboBox<>();
        sessionComboBox.setBounds(200, 150, 200, 25);
        loadSessions();
        background.add(sessionComboBox);

        JButton submitButton = new JButton("Submit");
        submitButton.setBounds(180, 220, 120, 40);
        styleButton(submitButton);
        background.add(submitButton);

        submitButton.addActionListener(e -> assignMember());

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

    private void loadSessions() {
        try (Connection conn = DBUtil.getConnection()) {
            String sql = "SELECT session_id FROM Session";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                int id = rs.getInt("session_id");
                sessionComboBox.addItem(String.valueOf(id));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void assignMember() {
        try (Connection conn = DBUtil.getConnection()) {
            String selectedMember = (String) memberComboBox.getSelectedItem();
            String selectedSession = (String) sessionComboBox.getSelectedItem();

            if (selectedMember == null || selectedSession == null) {
                JOptionPane.showMessageDialog(this, "Please select both member and session.");
                return;
            }

            int memberId = Integer.parseInt(selectedMember);
            int sessionId = Integer.parseInt(selectedSession);

            String sql = "INSERT INTO SessionParticipation (member_id, session_id) VALUES (?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, memberId);
            pstmt.setInt(2, sessionId);

            int rows = pstmt.executeUpdate();
            JOptionPane.showMessageDialog(this, rows > 0 ? "Member assigned!" : "Insert failed.");

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
