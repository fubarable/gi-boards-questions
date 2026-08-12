package com.boardsquestion;

import java.awt.BorderLayout;
import javax.swing.JPanel;

public class MainPanel extends JPanel {
    private ClinicalProblemModel clinicalProblemModel;
    private InputPanel inputPanel;

    public MainPanel() {
        int ebGap = 10; // empty border gap
        setBorder(javax.swing.BorderFactory.createEmptyBorder(ebGap, ebGap, ebGap, ebGap));
        setLayout(new BorderLayout());

        inputPanel = new InputPanel();
        add(inputPanel, BorderLayout.CENTER);
    }

    public void setClinicalProblemModel(ClinicalProblemModel clinicalProblemModel) {
        this.clinicalProblemModel = clinicalProblemModel;
    }

    public ClinicalProblemModel getClinicalProblemModel() {
        return clinicalProblemModel;
    }
}