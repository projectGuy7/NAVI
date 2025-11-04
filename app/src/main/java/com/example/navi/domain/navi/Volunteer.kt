package com.example.navi.domain.navi

data class Volunteer(
    override val firstName: String,
    override val lastName: String,
    override val age: Byte,
    override val email: String,
    val rating: Double,
    val numberOfSupport: Int
): User(
    firstName,
    lastName,
    age,
    email
)
