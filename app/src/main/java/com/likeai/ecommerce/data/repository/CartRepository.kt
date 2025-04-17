package com.likeai.ecommerce.data.repository

import com.likeai.ecommerce.data.model.Cart
import com.likeai.ecommerce.data.model.CartItem
import kotlinx.coroutines.flow.Flow

interface CartRepository {
    fun getCart(): Flow<Cart>
    suspend fun addToCart(item: CartItem): Result<Unit>
    suspend fun updateCartItem(item: CartItem): Result<Unit>
    suspend fun removeFromCart(productId: String): Result<Unit>
    suspend fun clearCart(): Result<Unit>
    suspend fun getCartItemCount(): Flow<Int>
} 