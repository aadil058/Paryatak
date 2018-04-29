package com.mobilecomputing.paryatak;

import com.google.firebase.auth.FirebaseAuth;

public class AnswerModal {
    private String AnswerID;
    private String UserID;
    private String QuestionID;
    private String Upvotes;
    private String Downvotes;
    private String AnswerTime;
    private String Answer;

    public AnswerModal(String QuestionID, String Answer) {
        String Time = String.valueOf(System.currentTimeMillis());
        this.AnswerID = FirebaseAuth.getInstance().getCurrentUser().getUid() + Time;
        this.UserID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        this.QuestionID = QuestionID;
        this.Upvotes = null;
        this.Downvotes = null;
        this.AnswerTime = Time;
        this.Answer = Answer;
    }

    public String getAnswerID() {
        return AnswerID;
    }

    public String getUserID() {
        return UserID;
    }

    public String getQuestionID() {
        return QuestionID;
    }

    public void setUpvotes(String upvotes) {
        Upvotes = upvotes;
    }

    public void setDownvotes(String downvotes) {
        Downvotes = downvotes;
    }

    public String getUpvotes() {
        return Upvotes;
    }

    public String getDownvotes() {
        return Downvotes;
    }

    public String getAnswerTime() {
        return AnswerTime;
    }

    public void setAnswer(String answer) {
        Answer = answer;
    }

    public String getAnswer() {
        return Answer;
    }
}
