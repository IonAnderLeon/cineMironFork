package com.example.cinemiron.tmp_movie.data.remote.models

import kotlinx.serialization.Serializable

@Serializable
data class MovieVideoResponseDto(
    val results: List<MovieVideoDto>
)

@Serializable
data class MovieVideoDto(
    val key: String,
    val site: String,
    val type: String
)
