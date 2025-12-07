package com.example.navi.cryptoManager

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import com.example.navi.domain.navi.Token
import java.io.InputStream
import java.io.OutputStream
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec

class TokenCryptoManager {

    private val keyStore = KeyStore.getInstance("AndroidKeyStore").apply {
        load(null)
    }

    private val encryptCipher get() = Cipher.getInstance(TRANSFORMATION).apply {
        init(Cipher.ENCRYPT_MODE, getKey())
    }

    private fun getDecryptCipherForIv(iv: ByteArray): Cipher {
        return Cipher.getInstance(TRANSFORMATION).apply {
            init(Cipher.DECRYPT_MODE, getKey(), IvParameterSpec(iv))
        }
    }

    private fun getKey(): SecretKey {
        val existingKey = keyStore.getEntry("secret", null) as? KeyStore.SecretKeyEntry
        return existingKey?.secretKey ?: createKey()
    }

    private fun createKey(): SecretKey {
        return KeyGenerator.getInstance(ALGORITHM).apply {
            init(
                KeyGenParameterSpec.Builder(
                    "secret",
                    KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
                )
                    .setBlockModes(BLOCK_MODE)
                    .setEncryptionPaddings(PADDING)
                    .setUserAuthenticationRequired(false)
                    .setRandomizedEncryptionRequired(true)
                    .build()
            )
        }.generateKey()
    }

    fun encrypt(accessTokenBytes: ByteArray, refreshTokenBytes: ByteArray, outputStream: OutputStream): ByteArray {
        var cipher = encryptCipher
        val encryptedAccessTokenBytes = cipher.doFinal(accessTokenBytes)
        outputStream.also {
            it.write(cipher.iv.size)
            it.write(cipher.iv)
            it.write(encryptedAccessTokenBytes.size)
            it.write(encryptedAccessTokenBytes)
        }
        cipher = encryptCipher
        val encryptedRefreshTokenBytes = cipher.doFinal(refreshTokenBytes)
        outputStream.use {
            it.write(cipher.iv.size)
            it.write(cipher.iv)
            it.write(encryptedRefreshTokenBytes.size)
            it.write(encryptedRefreshTokenBytes)
        }
        return encryptedAccessTokenBytes + encryptedRefreshTokenBytes
    }

    fun decrypt(inputStream: InputStream): Token {
        return inputStream.use {
            val accessTokenIvSize = inputStream.read()
            val accessTokenIv = ByteArray(accessTokenIvSize)
            it.read(accessTokenIv)

            val encryptedAccessTokenBytesSize = it.read()
            val encryptedAccessTokenBytes = ByteArray(encryptedAccessTokenBytesSize)
            it.read(encryptedAccessTokenBytes)

            val refreshTokenIvSize = inputStream.read()
            val refreshTokenIv = ByteArray(refreshTokenIvSize)
            it.read(refreshTokenIv)

            val encryptedRefreshTokenBytesSize = it.read()
            val encryptedRefreshTokenBytes = ByteArray(encryptedRefreshTokenBytesSize)
            it.read(encryptedRefreshTokenBytes)

            Token(
                accessToken = getDecryptCipherForIv(accessTokenIv).doFinal(encryptedAccessTokenBytes).decodeToString(),
                refreshToken = getDecryptCipherForIv(refreshTokenIv).doFinal(encryptedRefreshTokenBytes).decodeToString()
            )
        }
    }

    companion object {
        private const val ALGORITHM = KeyProperties.KEY_ALGORITHM_AES
        private const val BLOCK_MODE = KeyProperties.BLOCK_MODE_CBC
        private const val PADDING = KeyProperties.ENCRYPTION_PADDING_PKCS7
        private const val TRANSFORMATION = "$ALGORITHM/$BLOCK_MODE/$PADDING"
    }

}