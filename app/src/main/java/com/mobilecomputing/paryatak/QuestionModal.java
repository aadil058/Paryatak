package com.mobilecomputing.paryatak;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

    public QuestionModal() {}

    public void setQuestionID(String questionID) {
        QuestionID = questionID;
    }

    public String getQuestionID() {
        return QuestionID;
    }

    public void setWhoAskedItID(String whoAskedItID) {
        WhoAskedItID = whoAskedItID;
    }

    public String getWhoAskedItID() {
        return WhoAskedItID;
    }

    public void setQuestionTime(String questionTime) {
        QuestionTime = questionTime;
    }

    public String getQuestionTime() {
        return QuestionTime;
    }

    public void setQuestion(String question) {
        Question = question;
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

    public static QuestionModal snapshotToQuestionModal(Map<String, Object> snapshot) {
        QuestionModal questionModal = new QuestionModal();
        if(snapshot.get("questionID") != null) questionModal.setQuestionID(snapshot.get("questionID").toString());
        if(snapshot.get("whoAskedItID") != null) questionModal.setWhoAskedItID(snapshot.get("whoAskedItID").toString());
        if(snapshot.get("questionTime") != null) questionModal.setQuestionTime(snapshot.get("questionTime").toString());
        if(snapshot.get("question") != null) questionModal.setQuestion(snapshot.get("question").toString());
        if(snapshot.get("upvotes") != null) questionModal.setUpvotes(snapshot.get("upvotes").toString());
        if(snapshot.get("downvotes") != null) questionModal.setDownvotes(snapshot.get("downvotes").toString());
        if(snapshot.get("tags") != null) questionModal.setTags((List<String>) snapshot.get("tags"));
        if(snapshot.get("answersIDs") != null) questionModal.setAnswersIDs((List<String>) snapshot.get("answersIDs"));
        return questionModal;
    }
}
