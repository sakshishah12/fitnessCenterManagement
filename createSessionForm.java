package ise503_final;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class createSessionForm extends JFrame {
    private JComboBox<String> activityComboBox, trainerComboBox, roomComboBox;
    private JTextField sessionDateField, sessionTypeField;

    public createSessionForm() {
        setTitle("Create Session");
        setSize(550, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        backgrounPanel background = new backgrounPanel("/ise503_final/background3.png");
        background.setLayout(null);

        JLabel titleLabel = new JLabel("Create New Session");
        titleLabel.setBounds(160, 30, 300, 30);
        titleLabel.setForeground(Color.BLACK);
        titleLabel.setFont(new Font("Comic Sans MS", Font.BOLD, 20));
        background.add(titleLabel);

        JLabel activityLabel = new JLabel("Select Activity:");
        activityLabel.setBounds(80, 100, 120, 25);
        activityLabel.setForeground(Color.BLACK);
        background.add(activityLabel);

        activityComboBox = new JComboBox<>();
        activityComboBox.setBounds(220, 100, 200, 25);
        loadActivities();
        background.add(activityComboBox);

        JLabel trainerLabel = new JLabel("Select Trainer:");
        trainerLabel.setBounds(80, 150, 120, 25);
        trainerLabel.setForeground(Color.BLACK);
        background.add(trainerLabel);

        trainerComboBox = new JComboBox<>();
        trainerComboBox.setBounds(220, 150, 200, 25);
        loadTrainers();
        background.add(trainerComboBox);

        JLabel roomLabel = new JLabel("Select Room:");
        roomLabel.setBounds(80, 200, 120, 25);
        roomLabel.setForeground(Color.BLACK);
        background.add(roomLabel);

        roomComboBox = new JComboBox<>();
        roomComboBox.setBounds(220, 200, 200, 25);
        loadRooms();
        background.add(roomComboBox);

        JLabel dateLabel = new JLabel("Session Date (YYYY-MM-DD):");
        dateLabel.setBounds(80, 250, 200, 25);
        dateLabel.setForeground(Color.BLACK);
        background.add(dateLabel);

        sessionDateField = new JTextField();
        sessionDateField.setBounds(280, 250, 140, 25);
        background.add(sessionDateField);

        JLabel typeLabel = new JLabel("Session Type (Individual/Group):");
        typeLabel.setBounds(80, 300, 220, 25);
        typeLabel.setForeground(Color.BLACK);
        background.add(typeLabel);

        sessionTypeField = new JTextField();
        sessionTypeField.setBounds(300, 300, 120, 25);
        background.add(sessionTypeField);

        JButton submitButton = new JButton("Submit");
        submitButton.setBounds(200, 370, 120, 40);
        styleButton(submitButton);
        background.add(submitButton);

        submitButton.addActionListener(e -> createSession());

        setContentPane(background);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void loadActivities() {
        try (Connection conn = DBUtil.getConnection()) {
            String sql = "SELECT activity_id, activity_name FROM Activity";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                int id = rs.getInt("activity_id");
                String name = rs.getString("activity_name");
                activityComboBox.addItem(id + " - " + name);
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
                int id = rs.getInt("trainer_id");
                trainerComboBox.addItem(String.valueOf(id));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void loadRooms() {
        try (Connection conn = DBUtil.getConnection()) {
            String sql = "SELECT room_id FROM Room";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                int id = rs.getInt("room_id");
                roomComboBox.addItem(String.valueOf(id));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void createSession() {
        try (Connection conn = DBUtil.getConnection()) {
            String selectedActivity = (String) activityComboBox.getSelectedItem();
            String selectedTrainer = (String) trainerComboBox.getSelectedItem();
            String selectedRoom = (String) roomComboBox.getSelectedItem();
            String sessionDate = sessionDateField.getText();
            String sessionType = sessionTypeField.getText().toLowerCase();

            if (selectedActivity == null || selectedTrainer == null || selectedRoom == null || sessionDate.isEmpty() || sessionType.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill all fields.");
                return;
            }

            int activityId = Integer.parseInt(selectedActivity.split(" - ")[0]);
            int trainerId = Integer.parseInt(selectedTrainer);
            int roomId = Integer.parseInt(selectedRoom);

            // Insert into Session table
            String sessionSql = "INSERT INTO Session (activity_id, trainer_id, room_id, session_date) VALUES (?, ?, ?, ?)";
            PreparedStatement sessionPstmt = conn.prepareStatement(sessionSql, Statement.RETURN_GENERATED_KEYS);
            sessionPstmt.setInt(1, activityId);
            sessionPstmt.setInt(2, trainerId);
            sessionPstmt.setInt(3, roomId);
            sessionPstmt.setString(4, sessionDate);

            int rows = sessionPstmt.executeUpdate();

            if (rows > 0) {
                ResultSet generatedKeys = sessionPstmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    int sessionId = generatedKeys.getInt(1);

                    // Insert into IndividualSession or GroupSession
                    String typeSql = "";
                    if (sessionType.equals("individual")) {
                        typeSql = "INSERT INTO IndividualSession (session_id) VALUES (?)";
                    } else if (sessionType.equals("group")) {
                        typeSql = "INSERT INTO GroupSession (session_id) VALUES (?)";
                    } else {
                        JOptionPane.showMessageDialog(this, "Invalid session type. Use 'Individual' or 'Group'.");
                        return;
                    }

                    PreparedStatement typePstmt = conn.prepareStatement(typeSql);
                    typePstmt.setInt(1, sessionId);
                    typePstmt.executeUpdate();

                    JOptionPane.showMessageDialog(this, "Session created successfully!");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Session insert failed.");
            }
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
