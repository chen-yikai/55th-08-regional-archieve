package com.example.compsoe0324player_service_prepare

const val host = "https://skills-music-api-v2.eliaschen.dev"

data class MusicSchema(
    val id: Int,
    val name: String,
    val description: String,
    val tags: List<String>,
    val audio: String,
    val cover: String
)