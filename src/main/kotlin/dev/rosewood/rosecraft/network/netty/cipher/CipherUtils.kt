package dev.rosewood.rosecraft.network.netty.cipher

import java.security.Key
import java.security.KeyFactory
import java.security.spec.X509EncodedKeySpec
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec

object CipherUtils {

    fun getCipher(n: Int, key: Key): Cipher {
        val cipher = Cipher.getInstance("AES/CFB8/NoPadding")
        cipher.init(n, key, IvParameterSpec(key.encoded))
        return cipher
    }

    fun generateX509Key(base: Key): Key {
        val encodedKeySpec = X509EncodedKeySpec(base.encoded)
        val keyFactory = KeyFactory.getInstance("RSA")
        return keyFactory.generatePublic(encodedKeySpec)
    }

}
