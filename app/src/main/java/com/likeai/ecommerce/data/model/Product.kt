package com.likeai.ecommerce.data.model

data class Product(
    val id: String = "",
    val name: String = "",
    val description: String = "",
    val price: Double = 0.0,
    val category: String = "",
    val imageUrls: List<String> = listOf(),
    val rating: Float = 0f,
    val reviewCount: Int = 0,
    val stockQuantity: Int = 0,
    val createdAt: Long = System.currentTimeMillis()
) {
    // Empty constructor for Firebase
    constructor() : this("", "", "", 0.0, "", listOf(), 0f, 0, 0, 0)
} 