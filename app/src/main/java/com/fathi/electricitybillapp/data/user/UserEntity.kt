package com.fathi.electricitybillapp.data.user

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val username: String,
    val password: String,
    val role: String,
    val customerId: String?,
    val name: String,
    val email: String,
    val phone: String,
    val address: String,
    val createdAt: Long
)