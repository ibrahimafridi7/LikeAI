package com.likeai.ecommerce.data.repository.impl

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.likeai.ecommerce.data.model.User
import com.likeai.ecommerce.data.repository.AuthRepository
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class FirebaseAuthRepository : AuthRepository {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val usersCollection = firestore.collection("users")

    override val currentUser: FirebaseUser?
        get() = auth.currentUser

    override val currentUserFlow: Flow<FirebaseUser?> = callbackFlow {
        val listener = FirebaseAuth.AuthStateListener { auth ->
            trySend(auth.currentUser)
        }
        auth.addAuthStateListener(listener)
        awaitClose { auth.removeAuthStateListener(listener) }
    }

    override suspend fun signUp(email: String, password: String, name: String): Result<FirebaseUser> = try {
        val result = auth.createUserWithEmailAndPassword(email, password).await()
        result.user?.let { firebaseUser ->
            val user = User(
                id = firebaseUser.uid,
                email = email,
                name = name
            )
            usersCollection.document(firebaseUser.uid).set(user).await()
            Result.success(firebaseUser)
        } ?: Result.failure(Exception("User creation failed"))
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun login(email: String, password: String): Result<FirebaseUser> = try {
        val result = auth.signInWithEmailAndPassword(email, password).await()
        result.user?.let {
            Result.success(it)
        } ?: Result.failure(Exception("Login failed"))
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun loginWithGoogle(idToken: String): Result<FirebaseUser> = try {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        val result = auth.signInWithCredential(credential).await()
        result.user?.let { firebaseUser ->
            // Check if user exists in Firestore
            val userDoc = usersCollection.document(firebaseUser.uid).get().await()
            if (!userDoc.exists()) {
                // Create new user document
                val user = User(
                    id = firebaseUser.uid,
                    email = firebaseUser.email ?: "",
                    name = firebaseUser.displayName ?: "",
                    profileImageUrl = firebaseUser.photoUrl?.toString() ?: ""
                )
                usersCollection.document(firebaseUser.uid).set(user).await()
            }
            Result.success(firebaseUser)
        } ?: Result.failure(Exception("Google sign in failed"))
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun resetPassword(email: String): Result<Unit> = try {
        auth.sendPasswordResetEmail(email).await()
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun logout() {
        auth.signOut()
    }

    override suspend fun updateUserProfile(user: User): Result<Unit> = try {
        usersCollection.document(user.id).set(user).await()
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun getCurrentUserProfile(): Result<User> = suspendCoroutine { continuation ->
        currentUser?.let { firebaseUser ->
            usersCollection.document(firebaseUser.uid).get()
                .addOnSuccessListener { document ->
                    if (document != null && document.exists()) {
                        val user = document.toObject(User::class.java)
                        user?.let {
                            continuation.resume(Result.success(it))
                        } ?: continuation.resume(Result.failure(Exception("Failed to parse user data")))
                    } else {
                        continuation.resume(Result.failure(Exception("User not found")))
                    }
                }
                .addOnFailureListener { e ->
                    continuation.resume(Result.failure(e))
                }
        } ?: continuation.resume(Result.failure(Exception("No user logged in")))
    }
} 