package com.likeai.ecommerce.data.model

data class User(
    val id: String = "",
    val email: String = "",
    val name: String = "",
    val phone: String = "",
    val address: String = "",
    val profileImageUrl: String = ""
) {
    // Empty constructor for Firebase
    constructor() : this("", "", "", "", "", "")
} 