package com.example.instagramclonejetpackfirebase.viewmodel

import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.instagramclonejetpackfirebase.data.Event
import com.example.instagramclonejetpackfirebase.model.UserData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import com.google.firebase.storage.FirebaseStorage
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

const val USERS = "users"

@HiltViewModel
class IgViewModel @Inject constructor(
    val auth: FirebaseAuth,
    val db: FirebaseFirestore,
    val storage: FirebaseStorage

) : ViewModel() {

    val signedIn = mutableStateOf(false);
    val inProcess = mutableStateOf(false);
    val userData = mutableStateOf<UserData?>(null);
    val popupNotification = mutableStateOf<Event<String>?>(null);

    init {
        // signOut automatically when app is starting
        //  auth.signOut()
        val currentUser = auth.currentUser
        signedIn.value = currentUser != null
//        currentUser?.uid?.let { uid ->
//            getUserData(uid)
//        }
    }

    fun onSignup(username: String, email: String, pass: String): Unit {
        if (username.isEmpty() || email.isEmpty() or pass.isEmpty()) {
            handleException(custumMessage = "Please fill in all fields")
            return
        }
        inProcess.value = true;
        Handler(Looper.getMainLooper()).postDelayed({
            // Fake waiting 5s

            db.collection(USERS).whereEqualTo("username", username)
                .get()
                .addOnSuccessListener { documents ->
                    if (documents.size() > 0) {
                        handleException(custumMessage = "Username already exists")
                        inProcess.value = false
                    } else {
                        auth.createUserWithEmailAndPassword(email, pass)
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    signedIn.value = true
                                    createOrUpdateProfile(username = username)
                                } else {
                                    handleException(task.exception, "Signup failed")
                                    signedIn.value = false
                                }
                                inProcess.value = false;

                            }
                    }
                }
        }, 5000)
    }

    private fun createOrUpdateProfile(
        name: String? = null,
        username: String? = null,
        bio: String? = null,
        imageUrl: String? = null
    ) {
        var uid = auth.currentUser?.uid;
        val userData = UserData(
            userId = uid,
            name = name ?: userData.value?.name,
            username = username ?: userData.value?.username,
            bio = bio ?: userData.value?.bio,
            imageUrl = imageUrl ?: userData.value?.imageUrl,
            following = userData.value?.following
        )

        uid?.let {
            inProcess.value = false;
            db.collection(USERS).document(uid).get()
                .addOnSuccessListener {
                    if (it.exists()) {
                        Log.i("userData", userData.toMap().toString())
                        it.reference.update(userData.toMap())
                            .addOnSuccessListener {
                                this.userData.value = userData
                            }
                            .addOnFailureListener {
                                handleException(it, "Cannot update user")
                            }
                    } else {
                        db.collection(USERS).document(uid).set(userData)
                        getUserData(uid)
                        inProcess.value = false;
                    }
                }
        }
    }

    private fun getUserData(uid: String) {
        inProcess.value = false;
        db.collection(USERS).document(uid).get().addOnSuccessListener {
            val user = it.toObject<UserData>()
            userData.value = user;
            inProcess.value = false;

            popupNotification.value = Event(userData.value?.username ?: "")
        }.addOnFailureListener {
            handleException(it, "Cannot retrieve user data")
            inProcess.value = false;
        }
    }

    fun handleException(exception: Exception? = null, custumMessage: String = ""): Unit {
        exception?.printStackTrace();
        val errorMsg = exception?.localizedMessage ?: "";
        val message = if (custumMessage.isEmpty()) errorMsg else "$custumMessage: $errorMsg"
        popupNotification.value = Event(message)
    }

    fun onSignIn(email: String, pass: String) {
        if (email.isEmpty() or pass.isEmpty()) {
            handleException(custumMessage = "Please fill in all fields")
            return;
        }

        inProcess.value = true;

        Handler(Looper.getMainLooper()).postDelayed({
            auth.signInWithEmailAndPassword(email, pass).addOnCompleteListener {
                if (it.isSuccessful) {
                    signedIn.value = true;
                    inProcess.value = false;
                    auth.currentUser?.uid?.let {
                        getUserData(it)
                    }
                } else {
                    handleException(it.exception, "Login Failed")
                    inProcess.value = false; }
            }.addOnFailureListener {
                handleException(it, "Login Failed")
                inProcess.value = false;
            }
        }, 5000)
    }
}