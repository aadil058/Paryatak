package com.mobilecomputing.paryatak;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.dmoral.toasty.Toasty;

public class ForumActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forum);

        // loadFakeDataForTestingBecauseUIisNotReady();

        // List<String> Tags = new ArrayList<>();
        // Tags.add("Taj Mahal");
        // Tags.add("Agra Fort");
        // getQuestionsOrderByTimeAndWhereTagsMatches(Tags);
    }

    // LOAD FAKE DATA
    public void loadFakeDataForTestingBecauseUIisNotReady() {
        FirestoreService firestoreService = FirestoreService.getInstance();

        String Question = "Question 1 " + FirebaseAuth.getInstance().getCurrentUser().getUid();
        List<String> Tags = new ArrayList<>();
        Tags.add("Taj Mahal");
        QuestionModal question = new QuestionModal(Question, Tags);
        firestoreService.AskQuestion(question);

        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Question = "Question 2 " + FirebaseAuth.getInstance().getCurrentUser().getUid();
        Tags = new ArrayList<>();
        Tags.add("Agra Fort");
        question = new QuestionModal(Question, Tags);
        firestoreService.AskQuestion(question);
    }

    // RUN GET QUESTIONS TEST ON FAKEDATA
    public void getQuestionsOrderByTimeAndWhereTagsMatches(final List<String> Tags) {
        final FirestoreService firestoreService = FirestoreService.getInstance();
        firestoreService.getQuestions()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot documentSnapshots) {
                        List<DocumentSnapshot> questionsALL = documentSnapshots.getDocuments();
                        Map<String, QuestionModal> questionsFiltered = new HashMap<>();

                        for(DocumentSnapshot snapshot : questionsALL) {
                            Map<String, Object> data = snapshot.getData();
                            QuestionModal question = QuestionModal.snapshotToQuestionModal(data);

                            // Log.i("INFO ", String.valueOf(question.getQuestionID()) + " " + question.getUpvotes());

                            if(intersection(question.getTags(), Tags).size() > 0)
                                questionsFiltered.put(snapshot.getId(), question);
                        }

                        // questionsFiltered is now ready.
                        // Key will be used when question updates and Value is question object itself.

                        // TEMP CODE FOR TESTING
                        boolean first = true;
                        for (Map.Entry<String, QuestionModal> entry : questionsFiltered.entrySet()) {
                            if(first)
                                // updateQuestion(entry.getKey(), entry.getValue());
                                // loadFakeAnswer(entry.getValue().getQuestionID());
                                getAnswers(entry.getValue().getQuestionID());
                            first = false;
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toasty.error(getApplicationContext(), "Some error occurred while fetching questions.", Toast.LENGTH_LONG).show();
                    }
                });
    }

    // UPDATE QUESTION TEST ON FAKE DATA
    public void updateQuestion(String key, QuestionModal question) {
        FirestoreService firestoreService = FirestoreService.getInstance();
        question.setUpvotes(String.valueOf(10));
        firestoreService.updateQuestion(question, key);
    }

    // Load Fake Answer Test
    public void loadFakeAnswer(String QuestionID) {
        AnswerModal answer = new AnswerModal(QuestionID, "Answer " + FirebaseAuth.getInstance().getCurrentUser().getUid());
        FirestoreService firestoreService = FirestoreService.getInstance();
        firestoreService.WriteAnswer(answer)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(getApplicationContext(), "Success - Answer Test", Toast.LENGTH_LONG).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "Error - Answer Test", Toast.LENGTH_LONG).show();
                    }
                });
    }

    // Get All Answers For Given Question
    public void getAnswers(final String QuestionID) {
        FirestoreService firestoreService = FirestoreService.getInstance();
        firestoreService.getAnswers(QuestionID)
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot documentSnapshots) {
                        List<DocumentSnapshot> documents = documentSnapshots.getDocuments();
                        Map<String, AnswerModal> answers = new HashMap<>();

                        for(DocumentSnapshot snapshot : documents) {
                            Map<String, Object> data = snapshot.getData();
                            AnswerModal answer = AnswerModal.snapshotToAnswerModal(data);
                            answers.put(snapshot.getId(), answer);
                        }

                        // TEMP CODE FOR TESTING
                        boolean first = true;
                        for (Map.Entry<String, AnswerModal> entry : answers.entrySet()) {
                            if(first)
                                // Log.i("INFO ", entry.getValue().getAnswer());
                                updateAnswer(entry.getKey(), entry.getValue());

                            first = false;
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "Error - Answer GET Test", Toast.LENGTH_LONG).show();
                    }
                });
    }

    public void updateAnswer(String documentID, AnswerModal answer) {
        answer.setUpvotes(String.valueOf(10));
        FirestoreService firestoreService = FirestoreService.getInstance();
        firestoreService.updateAnswer(answer, documentID);
    }

    public List<String> intersection(List<String> listA, List<String> listB) {
        List<String> list = new ArrayList<String>();

        try {
            for (String string : listA) {
                if (listB.contains(string))
                    list.add(string);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }
}