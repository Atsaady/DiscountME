package com.example.discountme.Auth;

import android.annotation.SuppressLint;
import android.util.Log;

import androidx.annotation.NonNull;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


import java.util.Map;

public class AuthFirebase
{
    final static String USERS_COLLECTION = "users";
    private static String userId;
    private static Map<String, Object> user;
    private static User u;
    public static boolean flag = false;

    @SuppressLint("RestrictedApi")
    public static void addUser(User user , final UserModel.Listener<Boolean> listener) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(USERS_COLLECTION).document(user.getUid()).set(user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (listener!=null){
                    listener.onComplete(task.isSuccessful());
                }
            }
        });
    }

    public static void getUserFromFirebase() {
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        DocumentReference documentReference = firebaseFirestore.collection(USERS_COLLECTION).document(userId);
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {

            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d("TAG", "User Data: " + document.getData());
                        user = document.getData();
                        u = new User(user.get("uid").toString(), user.get("name").toString(), user.get("email").toString(), user.get("password").toString(), user.get("AIO").toString());
                    } else {
                        Log.d("TAG", "User not exists");
                    }
                } else {
                    Log.d("TAG", "User Get Failed ");
                }
            }
        });
    }

}