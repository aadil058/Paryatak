package com.mobilecomputing.paryatak;

import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

public class QuestionModal {
    private String QuestionID;
    private String WhoAskedItID;
    private String QuestionTime;
    private String Question;
    private String Upvotes;
    private String Downvotes;
    private List<String> Tags;
    private List<String> AnswersIDs;

    // Called when question is created for the first time
    public QuestionModal(String Question, List<String> Tags) {
        String Time = String.valueOf(System.currentTimeMillis());
        this.QuestionID = FirebaseAuth.getInstance().getCurrentUser().getUid() + Time;
        this.WhoAskedItID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        this.QuestionTime = Time;
        this.Question = Question;
        this.Tags = Tags;
        this.Upvotes = null;
        this.Downvotes = null;
        this.AnswersIDs = null;
    }

    public String getQuestionID() {
        return QuestionID;
    }

    public String getWhoAskedItID() {
        return WhoAskedItID;
    }

    public String getQuestionTime() {
        return QuestionTime;
    }

    public String getQuestion() {
        return Question;
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

    public void setTags(List<String> tags) {
        Tags = tags;
    }

    public List<String> getTags() {
        return Tags;
    }

    public List<String> getAnswersIDs() {
        return AnswersIDs;
    }

    public void setAnswersIDs(List<String> answersIDs) {
        AnswersIDs = answersIDs;
    }
}
