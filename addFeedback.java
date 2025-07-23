package ise503_final;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class addFeedback extends JFrame {
    private JComboBox<String> memberComboBox, sessionComboBox, trainerComboBox;
    private JTextField ratingField, dateField;
    private JTextArea commentsArea;

    public addFeedback() {
        setTitle("Collect Feedback");
        setSize(600, 550);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        backgrounPanel background = new backgrounPanel("/ise503_final/background3.png");
        background.setLayout(null);

        JLabel titleLabel = new JLabel("Collect Member Feedback");
        titleLabel.setBounds(150, 20, 300, 30);
        titleLabel.setForeground(Color.BLACK);
        titleLabel.setFont(new Font("Comic Sans MS", Font.BOLD, 20));
        background.add(titleLabel);

        JLabel memberLabel = new JLabel("Select Member:");
        memberLabel.setBounds(80, 70, 120, 25);
        memberLabel.setForeground(Color.BLACK);
        background.add(memberLabel);

        memberComboBox = new JComboBox<>();
        memberComboBox.setBounds(220, 70, 250, 25);
        loadMembers();
        background.add(memberComboBox);

        JLabel sessionLabel = new JLabel("Select Session:");
        sessionLabel.setBounds(80, 110, 120, 25);
        sessionLabel.setForeground(Color.BLACK);
        background.add(sessionLabel);

        sessionComboBox = new JComboBox<>();
        sessionComboBox.setBounds(220, 110, 250, 25);
        loadSessions();
        background.add(sessionComboBox);

        JLabel trainerLabel = new JLabel("Select Trainer:");
        trainerLabel.setBounds(80, 150, 120, 25);
        trainerLabel.setForeground(Color.BLACK);
        background.add(trainerLabel);

        trainerComboBox = new JComboBox<>();
        trainerComboBox.setBounds(220, 150, 250, 25);
        loadTrainers();
        background.add(trainerComboBox);

        JLabel ratingLabel = new JLabel("Rating (1-5):");
        ratingLabel.setBounds(80, 190, 120, 25);
        ratingLabel.setForeground(Color.BLACK);
        background.add(ratingLabel);

        ratingField = new JTextField();
        ratingField.setBounds(220, 190, 250, 25);
        background.add(ratingField);

        JLabel commentsLabel = new JLabel("Comments:");
        commentsLabel.setBounds(80, 230, 120, 25);
        commentsLabel.setForeground(Color.BLACK);
        background.add(commentsLabel);

        commentsArea = new JTextArea();
        JScrollPane scrollPane = new JScrollPane(commentsArea);
        scrollPane.setBounds(220, 230, 250, 100);
        background.add(scrollPane);

        JLabel dateLabel = new JLabel("Feedback Date (YYYY-MM-DD):");
        dateLabel.setBounds(80, 350, 220, 25);
        dateLabel.setForeground(Color.BLACK);
        background.add(dateLabel);

        dateField = new JTextField();
        dateField.setBounds(300, 350, 170, 25);
        background.add(dateField);

        JButton submitButton = new JButton("Submit");
        submitButton.setBounds(220, 400, 120, 40);
        styleButton(submitButton);
        background.add(submitButton);

        submitButton.addActionListener(e -> submitFeedback());

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
                memberComboBox.addItem(String.valueOf(rs.getInt("member_id")));
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
                sessionComboBox.addItem(String.valueOf(rs.getInt("session_id")));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void loadTrainers() {
        try (Connection conn = DBUtil.getConnection()) {
            String sql = "SELECT trainer_id FROM Trainer";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                trainerComboBox.addItem(String.valueOf(rs.getInt("trainer_id")));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void submitFeedback() {
        try (Connection conn = DBUtil.getConnection()) {
            String member = (String) memberComboBox.getSelectedItem();
            String session = (String) sessionComboBox.getSelectedItem();
            String trainer = (String) trainerComboBox.getSelectedItem();
            String ratingText = ratingField.getText();
            String comments = commentsArea.getText();
            String date = dateField.getText();

            if (member == null || session == null || trainer == null || ratingText.isEmpty() || comments.isEmpty() || date.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill all fields.");
                return;
            }

            int memberId = Integer.parseInt(member);
            int sessionId = Integer.parseInt(session);
            int trainerId = Integer.parseInt(trainer);
            int rating = Integer.parseInt(ratingText);

            String sql = "INSERT INTO Feedback (member_id, session_id, rating, comments, feedback_date, trainer_id) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, memberId);
            pstmt.setInt(2, sessionId);
            pstmt.setInt(3, rating);
            pstmt.setString(4, comments);
            pstmt.setString(5, date);
            pstmt.setInt(6, trainerId);

            int rows = pstmt.executeUpdate();
            JOptionPane.showMessageDialog(this, rows > 0 ? "Feedback recorded!" : "Insert failed.");
        } catch (NumberFormatException nfe) {
            JOptionPane.showMessageDialog(this, "Please enter valid numeric values for rating.");
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
