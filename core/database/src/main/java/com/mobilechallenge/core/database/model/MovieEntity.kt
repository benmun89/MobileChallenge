package com.mobilechallenge.core.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@TypeConverters(ListConverter::class)
@Entity(tableName = "movie")
data class MovieEntity(
    @PrimaryKey
    val id: Long,
    val adult: Boolean,
    val backdropPath: String? = "",
    val genreIds: List<Int>,
    val originalLanguage: String,
    val originalTitle: String,
    val overview: String,
    val popularity: Double,
    val posterPath: String?,
    val releaseDate: String?,
    val title: String,
    val video: Boolean,
    val voteAverage: Double,
    val voteCount: Int
)

class ListConverter {
    @TypeConverter
    fun encode(value: List<Int>) = Json.encodeToString(value)

    @TypeConverter
    fun decode(value: String) = Json.decodeFromString<List<Int>>(value)
}