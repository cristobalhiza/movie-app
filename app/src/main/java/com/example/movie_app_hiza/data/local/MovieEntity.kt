package com.example.movie_app_hiza.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters

@Entity(tableName = "movies")
@TypeConverters(GenreIdsConverter::class)
data class MovieEntity(
    @PrimaryKey val id: Int,
    val adult: Boolean,
    val backdropPath: String,
    val genreIds: List<Int> = emptyList(),
    val originalLanguage: String,
    val originalTitle: String,
    val overview: String,
    val popularity: Double,
    val posterPath: String,
    val releaseDate: String,
    val title: String,
    val video: Boolean,
    val voteAverage: Double,
    val voteCount: Int,
    val isFavorite: Boolean = false
)

class GenreIdsConverter {
    @TypeConverter
    fun fromGenreIds(genreIds: List<Int>?): String {
        return genreIds?.joinToString(",") ?: ""
    }

    @TypeConverter
    fun toGenreIds(data: String?): List<Int> {
        return if (data.isNullOrEmpty()) {
            emptyList()
        } else {
            data.split(",").mapNotNull {
                it.toIntOrNull()
            }
        }
    }
}
