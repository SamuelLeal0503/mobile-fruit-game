package com.example.myapplication;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.concurrent.atomic.AtomicReference;

public class FirestoreHelper {
    private static final String COLLECTION_NAME = "users";

    private FirebaseFirestore db;
    private CollectionReference usersCollection;

    public FirestoreHelper() {
        db = FirebaseFirestore.getInstance();
        usersCollection = db.collection(COLLECTION_NAME);
    }

    public User saveUserScore(String username, int score) {
        DocumentReference userDoc = usersCollection.document(username);

        try {
            userDoc.get().addOnSuccessListener(documentSnapshot -> {
                if (documentSnapshot.exists()) {
                    int currentScore = documentSnapshot.getLong("score") != null ? documentSnapshot.getLong("score").intValue() : 0;

                    if (score > currentScore) {
                        userDoc.update("score", score).addOnSuccessListener(aVoid -> {
                            System.out.println("Best score updated for user: " + username);
                        }).addOnFailureListener(e -> {
                            System.out.println("Failed to update best score for user: " + username);
                        });
                    }
                } else {
                    userDoc.set(new User(username, score)).addOnSuccessListener(aVoid -> {
                        System.out.println("New score inserted for user: " + username);
                    }).addOnFailureListener(e -> {
                        System.out.println("Failed to insert new score for user: " + username);
                    });
                }
            }).addOnFailureListener(e -> {
                System.out.println("Failed to retrieve user score: " + e.getMessage());
            });

            return new User(username, score);
        } catch (Exception e) {
            System.out.println("Failed to save user score: " + e.getMessage());
            return null;
        }
    }

    public interface GetHighestScoreCallback {
        void onSuccess(User user);
        void onFailure(String errorMessage);
    }

    public void getHighestScore(GetHighestScoreCallback callback) {
        Query query = usersCollection.orderBy("score", Query.Direction.DESCENDING).limit(1);

        query.get().addOnSuccessListener(querySnapshot -> {
            if (!querySnapshot.isEmpty()) {
                DocumentSnapshot documentSnapshot = querySnapshot.getDocuments().get(0);
                User user = documentSnapshot.toObject(User.class);
                if (user != null) {
                    System.out.println("Highest score: " + user.getScore());
                    callback.onSuccess(user);
                }
            } else {
                System.out.println("No users found");
                callback.onFailure("No users found");
            }
        }).addOnFailureListener(e -> {
            String errorMessage = "Failed to retrieve highest score: " + e.getMessage();
            System.out.println(errorMessage);
            callback.onFailure(errorMessage);
        });
    }



}

