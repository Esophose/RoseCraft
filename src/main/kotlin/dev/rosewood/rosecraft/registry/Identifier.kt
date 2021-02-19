package dev.rosewood.rosecraft.registry

import java.util.*

data class Identifier(var namespace: String, var identifier: String) {

    init {
        if (identifier.contains("[^0123456789abcdefghijklmnopqrstuvwxyz\\-_]"))
            throw IllegalArgumentException("Identifier may only contain 0-9, a-z, hyphens (-), and underscores (_)")
    }

    override fun toString() = "$namespace:$identifier"

    companion object {
        const val DEFAULT_NAMESPACE = "minecraft"

        fun of(identifier: String): Identifier {
            return if (identifier.contains(":")) {
                val split = identifier.split(':', limit = 2)
                Identifier(split[0], split[1])
            } else {
                Identifier(DEFAULT_NAMESPACE, identifier)
            }
        }
    }

}
