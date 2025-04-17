package com.likeai.ecommerce.data.model

data class Order(
    val id: String = "",
    val userId: String = "",
    val items: List<OrderItem> = listOf(),
    val totalAmount: Double = 0.0,
    val shippingAddress: String = "",
    val status: OrderStatus = OrderStatus.PENDING,
    val createdAt: Long = System.currentTimeMillis()
) {
    // Empty constructor for Firebase
    constructor() : this("", "", listOf(), 0.0, "", OrderStatus.PENDING, 0)
}

data class OrderItem(
    val productId: String = "",
    val productName: String = "",
    val quantity: Int = 0,
    val price: Double = 0.0,
    val imageUrl: String = ""
) {
    // Empty constructor for Firebase
    constructor() : this("", "", 0, 0.0, "")
}

enum class OrderStatus {
    PENDING,
    CONFIRMED,
    SHIPPED,
    DELIVERED,
    CANCELLED
} 