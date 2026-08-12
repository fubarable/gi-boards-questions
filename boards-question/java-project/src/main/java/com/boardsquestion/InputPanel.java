package com.boardsquestion;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.KeyEvent;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JPanel;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.text.PlainDocument;
import javax.swing.text.DocumentFilter;

public class InputPanel extends JPanel {
    private static final int NUM_POSSIBLE_ANSWERS = 5;
    private static final int TEXT_FIELD_COLUMNS = 30;
    private ClinicalProblemModel clinicalProblemModel;
    // Swing fields to get input from the user
    private JTextField ageField;
    private JRadioButton maleRadioButton;
    private JRadioButton femaleRadioButton;
    private ButtonGroup genderGroup;
    private JTextArea problemTxtArea;
    private JTextField pmhField;
    private JTextField examinationField;
    private JTextField labField;
    private JTextArea investigationsTxtArea;
    private JTextField inAdditionToField;
    private JTextField rationaleTextField;
    private JComboBox<String> answerPromptComboBox;

    private JTextField[] possibleAnswers;
    private ButtonGroup possibleAnswersGroup;
    private JButton submitButton;
    private JButton clearButton;
    private JButton exitButton;

    public InputPanel() {
        ageField = new JTextField(TEXT_FIELD_COLUMNS);
        PlainDocument ageDocument = (PlainDocument) ageField.getDocument();
        ageDocument.setDocumentFilter(new DocumentFilter() {
            @Override
            public void insertString(FilterBypass fb, int offset, String string, javax.swing.text.AttributeSet attr)
                    throws javax.swing.text.BadLocationException {
                if (string.matches("\\d*")) {
                    super.insertString(fb, offset, string, attr);
                }
            }

            @Override
            public void replace(FilterBypass fb, int offset, int length, String text,
                    javax.swing.text.AttributeSet attrs) throws javax.swing.text.BadLocationException {
                if (text.matches("\\d*")) {
                    super.replace(fb, offset, length, text, attrs);
                }
            }
        });
        maleRadioButton = new JRadioButton("Male");
        maleRadioButton.setSelected(true);
        maleRadioButton.setActionCommand("Male");
        maleRadioButton.setMnemonic(KeyEvent.VK_M);
        femaleRadioButton = new JRadioButton("Female");
        femaleRadioButton.setActionCommand("Female");
        femaleRadioButton.setMnemonic(KeyEvent.VK_F);
        JPanel genderPanel = new JPanel(new java.awt.GridLayout(1, 0));
        genderPanel.add(maleRadioButton);
        genderPanel.add(femaleRadioButton);
        genderPanel.setBorder(BorderFactory.createTitledBorder("Gender"));

        genderGroup = new ButtonGroup();
        genderGroup.add(maleRadioButton);
        genderGroup.add(femaleRadioButton);

        problemTxtArea = new JTextArea(4, 20);
        problemTxtArea.setLineWrap(true);
        problemTxtArea.setWrapStyleWord(true);
        bindTabToTransferFocus(problemTxtArea);
        JScrollPane problemScrollPane = new JScrollPane(problemTxtArea);
        problemScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        pmhField = new JTextField(TEXT_FIELD_COLUMNS);
        examinationField = new JTextField(TEXT_FIELD_COLUMNS);
        labField = new JTextField(TEXT_FIELD_COLUMNS);
        investigationsTxtArea = new JTextArea(4, 20);
        investigationsTxtArea.setLineWrap(true);
        investigationsTxtArea.setWrapStyleWord(true);
        bindTabToTransferFocus(investigationsTxtArea);
        JScrollPane investigationsScrollPane = new JScrollPane(investigationsTxtArea);
        investigationsScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        inAdditionToField = new JTextField(TEXT_FIELD_COLUMNS);
        rationaleTextField = new JTextField(TEXT_FIELD_COLUMNS);
        answerPromptComboBox = new JComboBox<>(loadAnswerPrompts());
        submitButton = new JButton("Submit");
        submitButton.setMnemonic(KeyEvent.VK_S);
        clearButton = new JButton("Clear");
        clearButton.setMnemonic(KeyEvent.VK_C);
        exitButton = new JButton("Exit");
        exitButton.setMnemonic(KeyEvent.VK_X);
        submitButton.addActionListener(e -> handleSubmitAction());
        clearButton.addActionListener(e -> clearAllInputFields());
        exitButton.addActionListener(e -> System.exit(0));
        JPanel buttonPanel = new JPanel(new GridLayout(1, 3, 2, 2));
        buttonPanel.add(submitButton);
        buttonPanel.add(clearButton);
        buttonPanel.add(exitButton);
        JPanel possibleAnswersPanel = new JPanel(new GridBagLayout());
        possibleAnswers = new JTextField[NUM_POSSIBLE_ANSWERS];
        possibleAnswersGroup = new ButtonGroup();
        possibleAnswersPanel.setBorder(BorderFactory.createTitledBorder("Possible Answers"));
        java.awt.GridBagConstraints gbc = new java.awt.GridBagConstraints();
        gbc.insets = new java.awt.Insets(5, 5, 5, 5);

        for (int i = 0; i < NUM_POSSIBLE_ANSWERS; i++) {
            possibleAnswers[i] = new JTextField(20);
            JRadioButton thisAnswerButton = new JRadioButton();
            String answerLetter = String.valueOf((char) ('A' + i));
            thisAnswerButton.setText(answerLetter);
            thisAnswerButton.setActionCommand(answerLetter);
            gbc.gridx = 0;
            gbc.gridy = i;
            gbc.weightx = 0;
            gbc.fill = java.awt.GridBagConstraints.HORIZONTAL;
            possibleAnswersGroup.add(thisAnswerButton);
            possibleAnswersPanel.add(thisAnswerButton, gbc);
            gbc.insets = new java.awt.Insets(2, 2, 2, 2);
            gbc.gridx = 1;
            gbc.weightx = 1.0;
            possibleAnswersPanel.add(possibleAnswers[i], gbc);
        }
        // Use explicit row indices so each left label aligns with its right input
        // field.
        setLayout(new java.awt.GridBagLayout());

        int row = 0;

        java.awt.GridBagConstraints left = new java.awt.GridBagConstraints();
        left.insets = new java.awt.Insets(5, 5, 5, 5);
        left.gridx = 0;
        left.anchor = java.awt.GridBagConstraints.WEST;

        java.awt.GridBagConstraints right = new java.awt.GridBagConstraints();
        right.insets = new java.awt.Insets(5, 5, 5, 5);
        right.gridx = 1;
        right.weightx = 1.0;
        right.fill = java.awt.GridBagConstraints.HORIZONTAL;

        left.gridy = row;
        right.gridy = row++;
        add(new JLabel("Gender:"), left);
        add(genderPanel, right);

        left.gridy = row;
        right.gridy = row++;
        add(new JLabel("Age:"), left);
        add(ageField, right);

        left.gridy = row;
        right.gridy = row++;
        add(new JLabel("HPI:"), left);
        right.ipady = 60;
        add(problemScrollPane, right);
        right.ipady = 0;

        left.gridy = row;
        right.gridy = row++;
        add(new javax.swing.JLabel("PMHx:"), left);
        add(pmhField, right);

        left.gridy = row;
        right.gridy = row++;
        add(new javax.swing.JLabel("Exam:"), left);
        add(examinationField, right);

        left.gridy = row;
        right.gridy = row++;
        add(new javax.swing.JLabel("Labs:"), left);
        add(labField, right);

        left.gridy = row;
        right.gridy = row++;
        add(new javax.swing.JLabel("WorkUp:"), left);
        right.ipady = 60;
        add(investigationsScrollPane, right);
        right.ipady = 0;

        left.gridy = row;
        right.gridy = row++;
        add(new javax.swing.JLabel("In Addn To:"), left);
        add(inAdditionToField, right);

        left.gridy = row;
        right.gridy = row++;
        add(new javax.swing.JLabel("Prompt:"), left);
        add(answerPromptComboBox, right);

        java.awt.GridBagConstraints bottom = new java.awt.GridBagConstraints();
        bottom.insets = new java.awt.Insets(5, 5, 5, 5);
        bottom.gridx = 0;
        bottom.gridy = row;
        bottom.gridwidth = 2;
        bottom.weightx = 1.0;
        bottom.fill = java.awt.GridBagConstraints.HORIZONTAL;
        add(possibleAnswersPanel, bottom);

        row++;
        left.gridy = row;
        right.gridy = row;
        add(new javax.swing.JLabel("Rationale:"), left);
        add(rationaleTextField, right);

        row++;
        java.awt.GridBagConstraints buttonRow = new java.awt.GridBagConstraints();
        buttonRow.insets = new java.awt.Insets(5, 5, 5, 5);
        buttonRow.gridx = 0;
        buttonRow.gridy = row;
        buttonRow.gridwidth = 2;
        buttonRow.fill = java.awt.GridBagConstraints.HORIZONTAL;
        add(buttonPanel, buttonRow);
    }

    public void setClinicalProblemModel(ClinicalProblemModel clinicalProblemModel) {
        this.clinicalProblemModel = clinicalProblemModel;
    }

    public ClinicalProblemModel getClinicalProblemModel() {
        return clinicalProblemModel;
    }

    private void bindTabToTransferFocus(JTextArea textArea) {
        String actionKey = "transferFocusOnTab";
        textArea.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_TAB, 0), actionKey);
        textArea.getActionMap().put(actionKey, new AbstractAction() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                textArea.transferFocus();
            }
        });
    }

    private void copyToClipboard(String text) {
        StringSelection selection = new StringSelection(text);
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(selection, null);
    }

    private void writeProblemModelStringToFile(String text) {
        String fileName = "boards-questions-" + LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE) + ".txt";
        Path outputFile = Paths.get(System.getProperty("user.dir"), fileName);
        String textToWrite = text.endsWith(System.lineSeparator()) ? text : text + System.lineSeparator();

        try {
            Files.writeString(
                    outputFile,
                    textToWrite,
                    StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.APPEND);
        } catch (Exception e) {
            System.err.println("Unable to write problem model string to file: " + e.getMessage());
        }
    }

    private void clearAllInputFields() {
        ageField.setText("");
        maleRadioButton.setSelected(true);
        problemTxtArea.setText("");
        pmhField.setText("");
        examinationField.setText("");
        labField.setText("");
        investigationsTxtArea.setText("");
        inAdditionToField.setText("");
        rationaleTextField.setText("");
        if (answerPromptComboBox.getItemCount() > 0) {
            answerPromptComboBox.setSelectedIndex(0);
        }
        for (JTextField answerField : possibleAnswers) {
            answerField.setText("");
        }
        possibleAnswersGroup.clearSelection();
    }

    private void handleSubmitAction() {
        String gender = maleRadioButton.isSelected() ? "Male" : "Female";
        int age = ageField.getText().isBlank() ? 0 : Integer.parseInt(ageField.getText());
        String hpi = problemTxtArea.getText().trim();
        String pmhx = pmhField.getText().trim();
        String exam = examinationField.getText().trim();
        String labs = labField.getText().trim();
        String workup = investigationsTxtArea.getText().trim();
        String inAdditionTo = inAdditionToField.getText().trim();
        Object selectedPrompt = answerPromptComboBox.getSelectedItem();
        String answerPrompt = selectedPrompt == null ? "" : selectedPrompt.toString();

        List<String> allPossibleAnswers = new ArrayList<>();
        for (JTextField answerField : possibleAnswers) {
            allPossibleAnswers.add(answerField.getText().trim());
        }

        String myAnswer = "";
        if (possibleAnswersGroup.getSelection() != null) {
            String actionCommand = possibleAnswersGroup.getSelection().getActionCommand();
            int selectedIndex = actionCommand.charAt(0) - 'A';
            if (selectedIndex >= 0 && selectedIndex < possibleAnswers.length) {
                myAnswer = possibleAnswers[selectedIndex].getText().trim();
            }
        }

        String myExplanation = rationaleTextField.getText().trim();

        ClinicalProblemModel model = new ClinicalProblemModel(
                gender,
                age,
                hpi,
                pmhx,
                exam,
                labs,
                workup,
                inAdditionTo,
                answerPrompt,
                allPossibleAnswers,
                myAnswer,
                myExplanation);

        setClinicalProblemModel(model);
        String problemModelString = createProblemModelString(model);
        copyToClipboard(problemModelString);
        writeProblemModelStringToFile(problemModelString);
        System.out.print(problemModelString);
    }

    private String createProblemModelString(ClinicalProblemModel model) {
        StringBuilder output = new StringBuilder();
        output.append("=========== Clinical Problem ===========\n");
        output.append("Gender: ").append(model.gender()).append("\n");
        output.append("Age: ").append(model.age()).append("\n");
        output.append("HPI: ").append(model.hpi()).append("\n");
        if (model.pmhx() != null && !model.pmhx().trim().isEmpty()) {
            output.append("Past Medical History: ").append(model.pmhx()).append(" otherwise the past medical history is negative.\n");
        }
        // only append the exam findings if it is not empty
        if (model.exam() != null && !model.exam().trim().isEmpty()) {
            output.append("Exam Findings: ").append(model.exam()).append(" otherwise the examination was unremarkable\n");
        }
        // only append the lab results if it is not empty
        if (model.labs() != null && !model.labs().trim().isEmpty()) {
            output.append("Lab Results: ").append(model.labs()).append(" otherwise the lab results are normal.\n");
        }
        output.append("Workup & course: ").append(model.workup()).append("\n");
        // only append the inAdditionTo field if it is not empty
        if (model.inAdditionTo() != null && !model.inAdditionTo().trim().isEmpty()) {
            output.append("In Addition To ").append(model.inAdditionTo()).append("\n");
        }
        output.append("Prompt: ").append(model.answerPrompt()).append("\n");
        output.append("Possible Answers:\n");

        List<String> answers = model.possibleAnswers();
        for (int i = 0; i < answers.size(); i++) {
            String answer = answers.get(i);
            if (answer == null || answer.trim().isEmpty()) {
                continue;
            }
            output.append("  ").append(Character.toString((char) ('A' + i))).append(". ").append(answer).append("\n");
        }

        if (model.myAnswer() != null && !model.myAnswer().trim().isEmpty()) {
            output.append("My selected Answer: ").append(model.myAnswer()).append("\n");
        } else {
            output.append("My selected Answer: I'm not sure\n");
        }
        output.append("Rationale: ").append(model.myExplanation()).append("\n\n");
        output.append("What answer do you think is best and why?\n");
        output.append("========================================\n\n");

        return output.toString();
    }

    private String[] loadAnswerPrompts() {
        List<String> prompts = new ArrayList<>();

        try (InputStream stream = InputPanel.class.getResourceAsStream("answerPrompts.txt")) {
            if (stream != null) {
                try (BufferedReader reader = new BufferedReader(
                        new InputStreamReader(stream, StandardCharsets.UTF_8))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        String trimmed = line.trim();
                        if (!trimmed.isEmpty()) {
                            prompts.add(trimmed);
                        }
                    }
                }
            }
        } catch (Exception e) {
            // Fall through to filesystem fallback.
        }

        if (prompts.isEmpty()) {
            Path filePath = Paths.get("src", "main", "java", "com", "boardsquestion", "answerPrompts.txt");
            if (Files.exists(filePath)) {
                try {
                    for (String line : Files.readAllLines(filePath, StandardCharsets.UTF_8)) {
                        String trimmed = line.trim();
                        if (!trimmed.isEmpty()) {
                            prompts.add(trimmed);
                        }
                    }
                } catch (Exception e) {
                    // Keep defaults when file cannot be read.
                }
            }
        }

        if (prompts.isEmpty()) {
            prompts.add("Select answer prompt");
        }

        return prompts.toArray(new String[0]);
    }
}
