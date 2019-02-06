package com.example.zhouyuhong.step;


public class QuestionLibrary {

    private String mQuestions [] = {
            "Select your age range:",
            "To which gender identity do you most identify?:",
            "To which race/ethnicity do you most identify?",
            "Please indicate the range of your household income:",
            "Do you usually push a stroller or walk with a mobility aid (cane, wheelchair, walker, etc.)?"

    };






    public String getQuestion(int a) {
        String question = mQuestions[a];
        return question;
    }



}