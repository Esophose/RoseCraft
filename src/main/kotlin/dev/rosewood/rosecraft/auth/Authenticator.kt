package dev.rosewood.rosecraft.auth

import com.google.gson.Gson
import com.google.gson.JsonObject
import dev.rosewood.rosecraft.RoseCraft
import java.io.BufferedReader
import java.io.InputStreamReader
import java.math.BigInteger
import java.net.HttpURLConnection
import java.net.InetAddress
import java.net.URI
import java.net.URL
import java.net.URLEncoder
import java.security.Key
import java.security.KeyFactory
import java.security.KeyPair
import java.security.KeyPairGenerator
import java.security.MessageDigest
import java.security.SecureRandom
import java.security.spec.X509EncodedKeySpec
import java.util.UUID
import javax.crypto.SecretKey
import javax.net.ssl.HttpsURLConnection

object Authenticator {

    val keyPair: KeyPair

    private val secureRandom: SecureRandom = SecureRandom()
    private val gson = Gson()

    init {
        val kpg = KeyPairGenerator.getInstance("RSA")
        kpg.initialize(1024)
        keyPair = kpg.generateKeyPair()
    }

    fun generateSessionId(): String {
        return secureRandom.nextLong().toString(16).trim()
    }

    fun generateVerifyToken(): ByteArray {
        val token = ByteArray(4)
        secureRandom.nextBytes(token)
        return token
    }

    fun verify(username: String, sessionId: String, sharedSecret: SecretKey, sessionAddress: InetAddress): SessionData? {
        try {
            val digest = MessageDigest.getInstance("SHA-1")
            digest.update(sessionId.toByteArray())
            digest.update(sharedSecret.encoded)
            digest.update(keyPair.public.encoded)
            val hash = BigInteger(digest.digest()).toString(16)

            var url = "https://sessionserver.mojang.com/session/minecraft/hasJoined?username=$username&serverId=$hash"
            if (RoseCraft.shouldPreventProxy)
                url += "&ip=${URLEncoder.encode(sessionAddress.hostAddress, "UTF-8")}"

            val connection = URL(url).openConnection() as HttpsURLConnection
            val inputStream = connection.inputStream
            val inputStreamReader = InputStreamReader(inputStream)
            val bufferedReader = BufferedReader(inputStreamReader)

            val stringBuilder = StringBuilder()
            var line = bufferedReader.readLine()
            while (line != null) {
                stringBuilder.append(line)
                line = bufferedReader.readLine()
            }

            bufferedReader.close()
            inputStreamReader.close()
            inputStream.close()
            connection.disconnect()

            val jsonObject = gson.fromJson(stringBuilder.toString(), JsonObject::class.java)
            val id = jsonObject.get("id").asString
            val formattedId = "${id.substring(0, 8)}-${id.substring(8, 12)}-${id.substring(12, 16)}-${id.substring(16, 20)}-${id.substring(20, 32)}"
            val name = jsonObject.get("name").asString
            val propertiesArray = jsonObject.get("properties").asJsonArray
            val texturesObject = propertiesArray.get(0).asJsonObject
            val textureValue = texturesObject.get("value").asString
            val textureSignature = texturesObject.get("signature").asString

            return SessionData(UUID.fromString(formattedId), name, textureValue, textureSignature)
        } catch (e: Exception) {
            return null
        }
    }

}
