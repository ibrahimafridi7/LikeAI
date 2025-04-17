package com.likeai.ecommerce.data.repository

import com.likeai.ecommerce.data.model.Product
import kotlinx.coroutines.flow.Flow

interface ProductRepository {
    fun getProducts(): Flow<List<Product>>
    fun getProductsByCategory(category: String): Flow<List<Product>>
    suspend fun getProduct(id: String): Result<Product>
    suspend fun searchProducts(query: String): Result<List<Product>>
    suspend fun getFeaturedProducts(): Result<List<Product>>
    suspend fun addProduct(product: Product): Result<String>
    suspend fun updateProduct(product: Product): Result<Unit>
    suspend fun deleteProduct(id: String): Result<Unit>
    suspend fun uploadProductImage(imageUri: String): Result<String>
} 