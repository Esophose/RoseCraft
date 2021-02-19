package dev.rosewood.rosecraft.network.player

data class ClientSettings(
    val locale: String,
    val viewDistance: Int,
    val chatMode: ChatMode,
    val chatColors: Boolean,
    val displayedSkinParts: DisplayedSkinParts,
    val preferredHand: HandType
)

// https://wiki.vg/Chat#Processing_chat
enum class ChatMode(private val id: Int) {
    ENABLED(0),
    COMMANDS_ONLY(1),
    HIDDEN(2);

    companion object {
        fun fromId(id: Int): ChatMode {
            for (chatMode in values())
                if (chatMode.id == id)
                    return chatMode
            throw IllegalArgumentException("Invalid ID")
        }
    }
}

class DisplayedSkinParts(bitMask: Int) {
    val capeEnabled = bitMask and 1 == 1
    val jacketEnabled = (bitMask shr 1) and 1 == 1
    val leftSleeveEnabled = (bitMask shr 2) and 1 == 1
    val rightSleeveEnabled = (bitMask shr 3) and 1 == 1
    val leftPantsLegEnabled = (bitMask shr 4) and 1 == 1
    val rightPantsLegEnabled = (bitMask shr 5) and 1 == 1
    val hatEnabled = (bitMask shr 6) and 1 == 1
}

enum class HandType(private val id: Int) {
    LEFT(0),
    RIGHT(1);

    companion object {
        fun fromId(id: Int): HandType {
            for (chatMode in values())
                if (chatMode.id == id)
                    return chatMode
            throw IllegalArgumentException("Invalid ID")
        }
    }
}
