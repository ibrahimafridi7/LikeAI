package com.likeai.ecommerce.data.repository.impl

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.likeai.ecommerce.data.model.Cart
import com.likeai.ecommerce.data.model.CartItem
import com.likeai.ecommerce.data.repository.CartRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await

class FirebaseCartRepository : CartRepository {
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val cartsCollection = firestore.collection("carts")

    private val currentUserId: String
        get() = auth.currentUser?.uid ?: throw IllegalStateException("User not logged in")

    override fun getCart(): Flow<Cart> = flow {
        val document = cartsCollection.document(currentUserId).get().await()
        val cart = document.toObject(Cart::class.java) ?: Cart(userId = currentUserId)
        emit(cart)
    }

    override suspend fun addToCart(item: CartItem): Result<Unit> = try {
        val cartRef = cartsCollection.document(currentUserId)
        val cart = cartRef.get().await().toObject(Cart::class.java) ?: Cart(userId = currentUserId)
        
        val existingItem = cart.items.find { it.productId == item.productId }
        if (existingItem != null) {
            existingItem.quantity += item.quantity
        } else {
            cart.items.add(item)
        }
        
        cartRef.set(cart.copy(updatedAt = System.currentTimeMillis())).await()
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun updateCartItem(item: CartItem): Result<Unit> = try {
        val cartRef = cartsCollection.document(currentUserId)
        val cart = cartRef.get().await().toObject(Cart::class.java) ?: Cart(userId = currentUserId)
        
        val index = cart.items.indexOfFirst { it.productId == item.productId }
        if (index != -1) {
            cart.items[index] = item
            cartRef.set(cart.copy(updatedAt = System.currentTimeMillis())).await()
            Result.success(Unit)
        } else {
            Result.failure(Exception("Item not found in cart"))
        }
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun removeFromCart(productId: String): Result<Unit> = try {
        val cartRef = cartsCollection.document(currentUserId)
        val cart = cartRef.get().await().toObject(Cart::class.java) ?: Cart(userId = currentUserId)
        
        cart.items.removeIf { it.productId == productId }
        cartRef.set(cart.copy(updatedAt = System.currentTimeMillis())).await()
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun clearCart(): Result<Unit> = try {
        val cartRef = cartsCollection.document(currentUserId)
        val cart = Cart(userId = currentUserId)
        cartRef.set(cart).await()
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun getCartItemCount(): Flow<Int> = flow {
        val document = cartsCollection.document(currentUserId).get().await()
        val cart = document.toObject(Cart::class.java) ?: Cart(userId = currentUserId)
        emit(cart.items.sumOf { it.quantity })
    }
} 