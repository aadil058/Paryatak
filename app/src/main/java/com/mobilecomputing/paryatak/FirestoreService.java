/*
 *
 * FirestoreService is our app's way into firestore (firebase storage service).
 *
 */

package com.mobilecomputing.paryatak;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

public class FirestoreService {

    private static FirestoreService firestoreService = null;
    private static FirebaseFirestore firebaseFirestore = null;
    private static String userDocumentID = null;

    private FirestoreService() {
        firebaseFirestore = FirebaseFirestore.getInstance();
    }

    public static FirestoreService getInstance() {
        if(firestoreService == null)
            firestoreService = new FirestoreService();
        return firestoreService;
    }

    // Reset FireStore Object
    public static void reset() {
        firestoreService = null;
        firebaseFirestore = null;
        setUserDocumentID(null);
    }

    /********************************/
    /***** User Related Methods *****/
    /********************************/

    // Store User Object
    public Task<DocumentReference> storeUser(UserModal user) {
        return firebaseFirestore.collection("users").add(user);
    }

    // Get User Object
    public Task<QuerySnapshot> getCurrentUser() {
        CollectionReference users = firebaseFirestore.collection("users");
        Query query = users.whereEqualTo("uid", FirebaseAuth.getInstance().getCurrentUser().getUid().toString());
        return query.get();
    }

    // Set Current User's Document ID
    public static void setUserDocumentID(String id) {
        userDocumentID = id;
    }

    // Get Current User's Document ID
    public static String getUserDocumentID() {
        return userDocumentID;
    }

    /**********************************/
    /***** Places Related Methods *****/
    /**********************************/

    // Get the list of (All) Places
    public Task<QuerySnapshot> getPlaces() {
        return firebaseFirestore.collection("Places").get();
    }

    // Get All Places Within a City
    public Task<DocumentSnapshot> getCityPlaces(String City) {
        return firebaseFirestore.collection("Places").document(City).get();
    }

    /*********************************/
    /***** Forum Related Methods *****/
    /*********************************/

    // Add a new question
    public Task<DocumentReference> AskQuestion(QuestionModal question) {
        CollectionReference forum = firebaseFirestore.collection("Forum");
        return forum.add(question);
    }

    // Get Questions Ordered By Time
    public Task<QuerySnapshot> getQuestions() {
        CollectionReference forum = firebaseFirestore.collection("Forum");
        return forum.orderBy("questionTime").get();
    }

    // Update an existing question
    public Task<Void> updateQuestion(QuestionModal question, String documentID) {
        return firebaseFirestore.collection("Forum")
                .document(documentID)
                .set(question, SetOptions.merge());
    }

    public Task<DocumentReference> WriteAnswer(AnswerModal answer) {
        return firebaseFirestore.collection("Answers")
                .add(answer);
    }

    public Task<QuerySnapshot> getAnswers(String QuestionID) {
        return firebaseFirestore.collection("Answers")
                .whereEqualTo("questionID", QuestionID)
                .get();
    }

    public Task<Void> updateAnswer(AnswerModal answer, String documentID) {
        return firebaseFirestore.collection("Answers")
                .document(documentID)
                .set(answer, SetOptions.merge());
    }

    /*********************************/
    /***** Posts Related Methods *****/
    /*********************************/


}
