package com.likeai.ecommerce.data.repository.impl

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.likeai.ecommerce.data.model.Order
import com.likeai.ecommerce.data.model.OrderStatus
import com.likeai.ecommerce.data.repository.OrderRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await

class FirebaseOrderRepository : OrderRepository {
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val ordersCollection = firestore.collection("orders")

    private val currentUserId: String
        get() = auth.currentUser?.uid ?: throw IllegalStateException("User not logged in")

    override fun getUserOrders(): Flow<List<Order>> = flow {
        val snapshot = ordersCollection
            .whereEqualTo("userId", currentUserId)
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .get()
            .await()
        val orders = snapshot.toObjects(Order::class.java)
        emit(orders)
    }

    override suspend fun getOrder(id: String): Result<Order> = try {
        val document = ordersCollection.document(id).get().await()
        document.toObject(Order::class.java)?.let {
            if (it.userId == currentUserId || isAdmin()) {
                Result.success(it)
            } else {
                Result.failure(Exception("Unauthorized access"))
            }
        } ?: Result.failure(Exception("Order not found"))
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun createOrder(order: Order): Result<String> = try {
        val orderWithUserId = order.copy(
            userId = currentUserId,
            createdAt = System.currentTimeMillis()
        )
        val documentRef = ordersCollection.document()
        val finalOrder = orderWithUserId.copy(id = documentRef.id)
        documentRef.set(finalOrder).await()
        Result.success(documentRef.id)
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun updateOrderStatus(orderId: String, status: String): Result<Unit> = try {
        val order = ordersCollection.document(orderId).get().await()
            .toObject(Order::class.java) ?: throw Exception("Order not found")

        if (!isAdmin() && order.userId != currentUserId) {
            throw Exception("Unauthorized access")
        }

        ordersCollection.document(orderId)
            .update("status", OrderStatus.valueOf(status))
            .await()
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun getAllOrders(): Flow<List<Order>> = flow {
        if (!isAdmin()) {
            throw Exception("Unauthorized access")
        }
        val snapshot = ordersCollection
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .get()
            .await()
        val orders = snapshot.toObjects(Order::class.java)
        emit(orders)
    }

    override suspend fun deleteOrder(id: String): Result<Unit> = try {
        val order = ordersCollection.document(id).get().await()
            .toObject(Order::class.java) ?: throw Exception("Order not found")

        if (!isAdmin() && order.userId != currentUserId) {
            throw Exception("Unauthorized access")
        }

        ordersCollection.document(id).delete().await()
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }

    private suspend fun isAdmin(): Boolean {
        val userDoc = firestore.collection("users")
            .document(currentUserId)
            .get()
            .await()
        return userDoc.getBoolean("isAdmin") ?: false
    }
} 