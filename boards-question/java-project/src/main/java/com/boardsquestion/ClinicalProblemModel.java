package com.boardsquestion;

import java.util.List;

public record ClinicalProblemModel(
    String gender, 
    int age, 
    String hpi, 
    String pmhx,
    String exam, 
    String labs, 
    String workup, 
    String inAdditionTo,
    String answerPrompt, 
    List<String> possibleAnswers, 
    String myAnswer,
    String myExplanation
    ) {
}
