package com.fathi.electricitybillapp.data.user

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.fathi.electricitybillapp.data.user.UserEntity

@Dao
interface UserDao {

    /**
     * Fungsi autentikasi pengguna
     *
     * @param username Nama pengguna
     * @param password Kata sandi
     * @return User? Objek pengguna jika berhasil, null jika gagal
     *
     * Fungsi:
     * - Memvalidasi kredensial pengguna
     * - Mengkonversi UserEntity ke User
     */
    @Query("SELECT * FROM users WHERE username = :username AND password = :password")
    suspend fun login(username: String, password: String): UserEntity?

    @Query("SELECT * FROM users WHERE id = :id")
    suspend fun getUserById(id: Int): UserEntity?

    @Query("SELECT * FROM users WHERE role = 'CUSTOMER'")
    suspend fun getAllCustomers(): List<UserEntity>

    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insertUser(user: UserEntity): Long

    @Update
    suspend fun updateUser(user: UserEntity)

    @Delete
    suspend fun deleteUser(user: UserEntity)
}