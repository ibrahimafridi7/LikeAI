package com.likeai.ecommerce.data.repository

import com.google.firebase.auth.FirebaseUser
import com.likeai.ecommerce.data.model.User
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    val currentUser: FirebaseUser?
    val currentUserFlow: Flow<FirebaseUser?>
    
    suspend fun signUp(email: String, password: String, name: String): Result<FirebaseUser>
    suspend fun login(email: String, password: String): Result<FirebaseUser>
    suspend fun loginWithGoogle(idToken: String): Result<FirebaseUser>
    suspend fun resetPassword(email: String): Result<Unit>
    suspend fun logout()
    suspend fun updateUserProfile(user: User): Result<Unit>
    suspend fun getCurrentUserProfile(): Result<User>
} 