package ise503_final;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class FitnessDashboard {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Fitness Center Dashboard");
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Background image panel
        backgrounPanel background = new backgrounPanel("/ise503_final/background3.png");
        background.setLayout(null);

        // Title Label
        JLabel titleLabel = new JLabel("Conan Fitness Center");
        titleLabel.setBounds(250, 30, 400, 30);
        titleLabel.setForeground(new Color(34, 70, 34));  // Deep forest green
        titleLabel.setFont(new Font("Comic Sans MS", Font.BOLD, 24));

        // Centralized column positions
        int leftX = 220;
        int rightX = 420;
        int startY = 100;
        int gap = 50;

        // Left Column Buttons
        JButton addPersonButton = new JButton("Add Person");
        addPersonButton.setBounds(leftX, startY, 170, 40);
        styleButton(addPersonButton);

        JButton registerMemberButton = new JButton("Register Member");
        registerMemberButton.setBounds(leftX, startY + gap, 170, 40);
        styleButton(registerMemberButton);

        JButton addTrainerButton = new JButton("Add Trainer");
        addTrainerButton.setBounds(leftX, startY + 2 * gap, 170, 40);
        styleButton(addTrainerButton);

        JButton addActivityButton = new JButton("Add Activity");
        addActivityButton.setBounds(leftX, startY + 3 * gap, 170, 40);
        styleButton(addActivityButton);

        // Right Column Buttons
        JButton createSessionButton = new JButton("Create Session");
        createSessionButton.setBounds(rightX, startY, 170, 40);
        styleButton(createSessionButton);

        JButton assignMemberButton = new JButton("Assign Member");
        assignMemberButton.setBounds(rightX, startY + gap, 170, 40);
        styleButton(assignMemberButton);

        JButton paymentButton = new JButton("Record Payment");
        paymentButton.setBounds(rightX, startY + 2 * gap, 170, 40);
        styleButton(paymentButton);

        JButton feedbackButton = new JButton("Collect Feedback");
        feedbackButton.setBounds(rightX, startY + 3 * gap, 170, 40);
        styleButton(feedbackButton);

        // Bottom Exit Button
        JButton exitButton = new JButton("Exit");
        exitButton.setBounds(325, startY + 4 * gap + 30, 150, 45);
        styleButton(exitButton);

        // Button Actions (Placeholders for now)
        addPersonButton.addActionListener(e -> new AddPersonsForm());
        registerMemberButton.addActionListener(e -> new RegisterMemberForm());
        addTrainerButton.addActionListener(e -> new addTrainer());
        createSessionButton.addActionListener(e -> new createSessionForm());
        assignMemberButton.addActionListener(e -> new assignMemberForm());
        paymentButton.addActionListener(e -> new addPaymentForm());
        feedbackButton.addActionListener(e -> new addFeedback());
        exitButton.addActionListener(e -> System.exit(0));

        // Add all components to background
        background.add(titleLabel);
        background.add(addPersonButton);
        background.add(registerMemberButton);
        background.add(addTrainerButton);
        background.add(addActivityButton);
        background.add(createSessionButton);
        background.add(assignMemberButton);
        background.add(paymentButton);
        background.add(feedbackButton);
        background.add(exitButton);

        frame.setContentPane(background);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    // Button styling
    private static void styleButton(JButton button) {
        Color normalColor = new Color(168, 195, 164);  // Soft sage green
        Color hoverColor = new Color(110, 139, 99);    // Dark olive green

        button.setBackground(normalColor);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Verdana", Font.BOLD, 14));
        button.setFocusPainted(false);
        button.setBorderPainted(false);

        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                button.setBackground(hoverColor);
            }

            public void mouseExited(MouseEvent evt) {
                button.setBackground(normalColor);
            }
        });
    }
}
