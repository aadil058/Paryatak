package com.mobilecomputing.paryatak;

import com.google.firebase.auth.FirebaseAuth;

import java.util.List;
import java.util.Map;

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

    public AnswerModal() { }

    public void setAnswerID(String answerID) {
        AnswerID = answerID;
    }

    public String getAnswerID() {
        return AnswerID;
    }

    public void setUserID(String userID) {
        UserID = userID;
    }

    public String getUserID() {
        return UserID;
    }

    public void setQuestionID(String questionID) {
        QuestionID = questionID;
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

    public void setAnswerTime(String answerTime) {
        AnswerTime = answerTime;
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

    public static AnswerModal snapshotToAnswerModal(Map<String, Object> snapshot) {
        AnswerModal answer = new AnswerModal();
        if (snapshot.get("answerID") != null) answer.setAnswerID(snapshot.get("answerID").toString());
        if (snapshot.get("userID") != null) answer.setUserID(snapshot.get("userID").toString());
        if (snapshot.get("questionID") != null) answer.setQuestionID(snapshot.get("questionID").toString());
        if (snapshot.get("upvotes") != null) answer.setUpvotes(snapshot.get("upvotes").toString());
        if (snapshot.get("downvotes") != null) answer.setDownvotes(snapshot.get("downvotes").toString());
        if (snapshot.get("answerTime") != null) answer.setAnswerTime(snapshot.get("answerTime").toString());
        if (snapshot.get("answer") != null) answer.setAnswer(snapshot.get("answer").toString());
        return answer;
    }
}
