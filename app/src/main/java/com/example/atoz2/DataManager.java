package com.example.atoz2;


import android.content.Intent;
import android.util.Log;
import android.widget.Toast;


import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.logging.Level;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
//import com.google.firebase.firestore.auth.User;
import com.google.firebase.storage.FirebaseStorage;


public class DataManager {
    private final FirebaseAuth firebaseAuth;
    private final FirebaseFirestore dbFireStore;
    private final FirebaseStorage storage;


    private User currentUser;
    private static DataManager single_instance = null;

    private DataManager(){
        firebaseAuth = FirebaseAuth.getInstance();
        dbFireStore = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
    }

    public static DataManager getInstance() {
        return single_instance;
    }

    public static DataManager initHelper() {
        if (single_instance == null) {
            single_instance = new DataManager();
        }
        return single_instance;
    }

    public FirebaseAuth getFirebaseAuth() {
        return firebaseAuth;
    }

    public FirebaseFirestore getDbFireStore() {
        return dbFireStore;
    }

    public FirebaseStorage getStorage() {
        return storage;
    }


    public User getCurrentUser() {
        return currentUser;
    }

    public DataManager setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
        return this;
    }

    public void storeUserInDB(User userToStore) {
        dbFireStore.collection("Users")
                .document(userToStore.getUserId())
                .set(userToStore)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d("pttt", "DocumentSnapshot Successfully written!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("pttt", "Error adding document", e);
                    }
                });
    }

    public int findUserInDB(String email, String password){
        final int[] answer = {0};
        CollectionReference myRef = dbFireStore.collection("Users");
        myRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        User loadedUser = document.toObject(User.class);
                        if (loadedUser.getEmail().equals(email)) {
                            if (loadedUser.getPassword().equals(password)) {
                                DataManager.getInstance().setCurrentUser(loadedUser);
                                Log.d("pttt", document.getId() + " => " + document.getData());
                            } else{
                                answer[0] = 2;
                            }
                        } else{
                          answer[0] = 1;
                    }
                }
                } else {
                    Log.d("pttt", "Error getting documents: ", task.getException());
                }
            }
        });
        return answer[0];
    }




//    public static ArrayList<Level> generateLevels() {
//        ArrayList<Level> levels = new ArrayList<>();
//
//        levels.add(new Level(1, 4,4));
//        levels.add(new Level(2, 5,5));
//        levels.add(new Level(3, 6,6));
//        levels.add(new Level(4, 7,7));
//        levels.add(new Level(5, 8,8));
//        levels.add(new Level(6, 9,9));
//        levels.add(new Level(7, 10,10));
//        levels.add(new Level(8, 11,11));
//        return levels;
//    }



}