package com.boardsquestion;

import javax.swing.*;
import java.awt.GridLayout;
import java.awt.BorderLayout;
import java.awt.Window;
import java.awt.event.KeyEvent;

public class SolutionsPanel extends JPanel {
    // this will be a JPanel that contains summary paragraphs of solutions to the questions, 
    // along with information on the best answer.
    // It will need 3 JTextAreas, one for the 2 AIs used, and one for the actual answer.
    // The constructor will need a file name passed in, the same one used in the writeProblemModelStringToFile method
    // of the InputPanel class. This will be the file that the information held in the JTextAreas will be written to. 
    // It will also need a button to clear the text fields and submit the answer to the file, and a button to go back to the input panel.
    // Just as in the InputPanel, the JTextAreas will need to be scrollable, and the text should wrap. 
    // The text areas should be editable, so that the user can make changes to the text before submitting it to the file. The submit button should write the text in the JTextAreas to the file, and then clear the text areas. The back button should take the user back to the input panel.

    // fields 
    private JTextArea ai1TextArea;
    private JTextArea ai2TextArea;
    private JTextArea userAnswerTextArea;

    private JButton clearButton;
    private JButton submitButton;
    private JButton backButton;

    // constructor
    public SolutionsPanel(String fileName) {
        // set layout
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        int ebGap = 10; // empty border gap
        setBorder(javax.swing.BorderFactory.createEmptyBorder(ebGap, ebGap, ebGap, ebGap));

        // create text areas
        ai1TextArea = new JTextArea(10, 50);
        ai1TextArea.setLineWrap(true);
        ai1TextArea.setWrapStyleWord(true);
        JScrollPane ai1ScrollPane = new JScrollPane(ai1TextArea);

        ai2TextArea = new JTextArea(10, 50);
        ai2TextArea.setLineWrap(true);
        ai2TextArea.setWrapStyleWord(true);
        JScrollPane ai2ScrollPane = new JScrollPane(ai2TextArea);

        userAnswerTextArea = new JTextArea(10, 50);
        userAnswerTextArea.setLineWrap(true);
        userAnswerTextArea.setWrapStyleWord(true);
        JScrollPane userAnswerScrollPane = new JScrollPane(userAnswerTextArea);

        // create buttons
        clearButton = new JButton("Clear");
        submitButton = new JButton("Submit");
        backButton = new JButton("Back");

        // add components to panel
        JPanel ai1Panel = new JPanel(new BorderLayout());
        ai1Panel.add(new JLabel("AI 1 Solution:"), BorderLayout.NORTH);
        ai1Panel.add(ai1ScrollPane, BorderLayout.CENTER);
        add(ai1Panel);

        JPanel ai2Panel = new JPanel(new BorderLayout());
        ai2Panel.add(new JLabel("AI 2 Solution:"), BorderLayout.NORTH);
        ai2Panel.add(ai2ScrollPane, BorderLayout.CENTER);
        add(ai2Panel);

        JPanel userAnswerPanel = new JPanel(new BorderLayout());
        userAnswerPanel.add(new JLabel("Correct Answer:"), BorderLayout.NORTH);
        userAnswerPanel.add(userAnswerScrollPane, BorderLayout.CENTER);
        add(userAnswerPanel);

        // empty space between text areas and buttons
        add(Box.createVerticalStrut(10));

        JPanel buttonPanel = new JPanel(new GridLayout(1, 0, 10, 0));
        buttonPanel.add(clearButton);
        buttonPanel.add(submitButton);
        buttonPanel.add(backButton);
        add(buttonPanel);

        // add action listeners to buttons
        clearButton.addActionListener(e -> clearTextAreas());
        clearButton.setMnemonic(KeyEvent.VK_C);
        submitButton.addActionListener(e -> submitToFile(fileName));
        submitButton.setMnemonic(KeyEvent.VK_S);
        backButton.addActionListener(e -> goBackToInputPanel());
        backButton.setMnemonic(KeyEvent.VK_B);
    }

    private void clearTextAreas() {
        ai1TextArea.setText("");
        ai2TextArea.setText("");
        userAnswerTextArea.setText("");
    }

    private void submitToFile(String fileName) {
        // write the text in the JTextAreas to the file
        try (java.io.FileWriter writer = new java.io.FileWriter(fileName, true)) {
            writer.write("AI 1 Solution:\n");
            writer.write(ai1TextArea.getText() + "\n\n");
            writer.write("AI 2 Solution:\n");
            writer.write(ai2TextArea.getText() + "\n\n");
            writer.write("Correct Answer:\n");
            writer.write(userAnswerTextArea.getText() + "\n\n");
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
    }

    private void goBackToInputPanel() {
        Window thisWindow = SwingUtilities.getWindowAncestor(this);
        if (thisWindow != null) {
            thisWindow.dispose();
        }
    }

    public static void main(String[] args) {
        System.out.println("Hello from Java 21 Maven project.");
        javax.swing.SwingUtilities.invokeLater(() -> {
            createAndShowGUI();
        });
    }

    private static void createAndShowGUI() {
        // create Swing JFrame
        javax.swing.JFrame frame = new javax.swing.JFrame("Boards Question Template");
        frame.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
        frame.add(new SolutionsPanel("exampleFileName.txt"));
        frame.pack();
        frame.setLocationByPlatform(true);
        frame.setVisible(true);
    }
}
