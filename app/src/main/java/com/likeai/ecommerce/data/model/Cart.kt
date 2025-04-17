package com.likeai.ecommerce.data.model

data class Cart(
    val userId: String = "",
    val items: MutableList<CartItem> = mutableListOf(),
    val updatedAt: Long = System.currentTimeMillis()
) {
    // Empty constructor for Firebase
    constructor() : this("", mutableListOf(), 0)

    fun getTotalAmount(): Double {
        return items.sumOf { it.quantity * it.price }
    }
}

data class CartItem(
    val productId: String = "",
    val productName: String = "",
    var quantity: Int = 0,
    val price: Double = 0.0,
    val imageUrl: String = ""
) {
    // Empty constructor for Firebase
    constructor() : this("", "", 0, 0.0, "")
} 