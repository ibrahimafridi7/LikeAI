package com.likeai.ecommerce.data.repository

import com.likeai.ecommerce.data.model.Order
import kotlinx.coroutines.flow.Flow

interface OrderRepository {
    fun getUserOrders(): Flow<List<Order>>
    suspend fun getOrder(id: String): Result<Order>
    suspend fun createOrder(order: Order): Result<String>
    suspend fun updateOrderStatus(orderId: String, status: String): Result<Unit>
    suspend fun getAllOrders(): Flow<List<Order>>
    suspend fun deleteOrder(id: String): Result<Unit>
} 