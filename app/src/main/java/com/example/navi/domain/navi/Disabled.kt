package com.example.navi.domain.navi

data class Disabled(
    override val firstName: String,
    override val lastName: String,
    override val age: Byte,
    override val email: String,
    val problems: String
): User(
    firstName,
    lastName,
    age,
    email
)