package com.fathi.electricitybillapp.data.user


/**
 * Modul pengelolaan pengguna
 *
 * Komponen:
 * - User.kt: Data class untuk pengguna
 * - UserEntity.kt: Entitas database pengguna
 * - UserDao.kt: Interface akses data pengguna
 * - UserRepository.kt: Repository pattern untuk pengguna
 *
 * Kegunaan:
 * - Autentikasi pengguna (login/logout)
 * - Manajemen data pengguna (CRUD)
 * - Pembedaan role ADMIN dan CUSTOMER
 *
 * @author Fathi
 * @since 2025
 */
data class User(
    val id: Int = 0,
    val username: String,
    val password: String,
    val role: UserRole,
    val customerId: String? = null,
    val name: String,
    val email: String,
    val phone: String,
    val address: String = "",
    val createdAt: Long = System.currentTimeMillis()
)

enum class UserRole {
    ADMIN, CUSTOMER
}
