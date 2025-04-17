package com.likeai.ecommerce.data.repository.impl

import android.net.Uri
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.storage.FirebaseStorage
import com.likeai.ecommerce.data.model.Product
import com.likeai.ecommerce.data.repository.ProductRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import java.util.UUID

class FirebaseProductRepository : ProductRepository {
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val storage: FirebaseStorage = FirebaseStorage.getInstance()
    private val productsCollection = firestore.collection("products")

    override fun getProducts(): Flow<List<Product>> = flow {
        val snapshot = productsCollection
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .get()
            .await()
        val products = snapshot.toObjects(Product::class.java)
        emit(products)
    }

    override fun getProductsByCategory(category: String): Flow<List<Product>> = flow {
        val snapshot = productsCollection
            .whereEqualTo("category", category)
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .get()
            .await()
        val products = snapshot.toObjects(Product::class.java)
        emit(products)
    }

    override suspend fun getProduct(id: String): Result<Product> = try {
        val document = productsCollection.document(id).get().await()
        document.toObject(Product::class.java)?.let {
            Result.success(it)
        } ?: Result.failure(Exception("Product not found"))
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun searchProducts(query: String): Result<List<Product>> = try {
        val snapshot = productsCollection
            .orderBy("name")
            .startAt(query)
            .endAt(query + '\uf8ff')
            .get()
            .await()
        Result.success(snapshot.toObjects(Product::class.java))
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun getFeaturedProducts(): Result<List<Product>> = try {
        val snapshot = productsCollection
            .orderBy("rating", Query.Direction.DESCENDING)
            .limit(10)
            .get()
            .await()
        Result.success(snapshot.toObjects(Product::class.java))
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun addProduct(product: Product): Result<String> = try {
        val documentRef = productsCollection.document()
        val productWithId = product.copy(id = documentRef.id)
        documentRef.set(productWithId).await()
        Result.success(documentRef.id)
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun updateProduct(product: Product): Result<Unit> = try {
        productsCollection.document(product.id).set(product).await()
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun deleteProduct(id: String): Result<Unit> = try {
        productsCollection.document(id).delete().await()
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun uploadProductImage(imageUri: String): Result<String> = try {
        val uri = Uri.parse(imageUri)
        val filename = UUID.randomUUID().toString()
        val ref = storage.reference.child("products/$filename")
        val uploadTask = ref.putFile(uri).await()
        val downloadUrl = ref.downloadUrl.await()
        Result.success(downloadUrl.toString())
    } catch (e: Exception) {
        Result.failure(e)
    }
} 