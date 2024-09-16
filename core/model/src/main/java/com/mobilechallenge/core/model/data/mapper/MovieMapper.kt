package com.mobilechallenge.core.model.data.mapper

import com.mobilechallenge.core.database.model.MovieEntity
import com.mobilechallenge.core.model.data.DatesDto
import com.mobilechallenge.core.model.data.DatesModel
import com.mobilechallenge.core.model.data.MovieDto
import com.mobilechallenge.core.model.data.MovieModel
import com.mobilechallenge.core.model.data.MovieResponseDto
import com.mobilechallenge.core.model.data.MovieResponseModel


fun MovieResponseDto.toDomainModel() =
    MovieResponseModel(
        dates?.toDomainModel(),
        page,
        results.map(MovieDto::toDomainModel),
        totalPages,
        totalResults
    )

fun DatesDto.toDomainModel() = DatesModel(maximum, minimum)

fun MovieDto.toDomainModel() = MovieModel(
    0,
    adult,
    backdropPath,
    genreIds,
    id,
    originalLanguage,
    originalTitle,
    overview,
    popularity,
    posterPath,
    releaseDate,
    title,
    video,
    voteAverage,
    voteCount
)

fun MovieModel.toEntity() = MovieEntity(
    primaryKey,
    adult,
    backdropPath,
    genreIds,
    id,
    originalLanguage,
    originalTitle,
    overview,
    popularity,
    posterPath,
    releaseDate,
    title,
    video,
    voteAverage,
    voteCount
)


