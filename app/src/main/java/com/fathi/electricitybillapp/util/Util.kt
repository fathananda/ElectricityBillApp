package com.fathi.electricitybillapp.util

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Mengkonversi nomor bulan ke nama bulan dalam bahasa Indonesia
 *
 * @param month Nomor bulan (1-12)
 * @return String Nama bulan dalam bahasa Indonesia
 *
 * Fungsi:
 * - Mapping nomor bulan ke nama bulan
 * - Lokalisasi dalam bahasa Indonesia
 */

fun getMonthName(month: Int): String {
    return when (month) {
        1 -> "Januari"
        2 -> "Februari"
        3 -> "Maret"
        4 -> "April"
        5 -> "Mei"
        6 -> "Juni"
        7 -> "Juli"
        8 -> "Agustus"
        9 -> "September"
        10 -> "Oktober"
        11 -> "November"
        12 -> "Desember"
        else -> "Unknown"
    }
}

/**
 * Memformat timestamp ke format tanggal yang mudah dibaca
 *
 * @param timestamp Timestamp dalam milidetik
 * @return String Tanggal dalam format dd/MM/yyyy
 *
 * Fungsi:
 * - Konversi timestamp ke objek Date
 * - Format ke string dengan pola dd/MM/yyyy
 *
 * Exceptions:
 * - IllegalArgumentException: Jika timestamp tidak valid
 */

fun formatDate(timestamp: Long): String {
    val date = Date(timestamp)
    val format = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    return format.format(date)
}
