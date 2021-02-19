package dev.rosewood.rosecraft.auth

import java.util.UUID

data class SessionData(
    val id: UUID,
    val name: String,
    val textureValue: String,
    val textureSignature: String
)
