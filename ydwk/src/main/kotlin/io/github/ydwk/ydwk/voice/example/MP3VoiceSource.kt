package io.github.ydwk.ydwk.voice.example


import io.github.ydwk.ydwk.voice.VoiceSource
import org.apache.tika.Tika
import java.io.File
import java.io.IOException
import java.nio.ByteBuffer
import javax.sound.sampled.AudioFormat
import javax.sound.sampled.AudioInputStream
import javax.sound.sampled.AudioSystem
import javax.sound.sampled.UnsupportedAudioFileException

/**
 * Handles mp3 players.
 */
class MP3VoiceSource(private val audioFilePath: File) : VoiceSource {

    private val audioInputStream: AudioInputStream
    private val audioFormat: AudioFormat
    private val bufferSize: Int

    init {
        if (!isValidAudioFile(audioFilePath)) {
            throw IllegalArgumentException("Unsupported audio format")
        }

        try {
            audioInputStream = AudioSystem.getAudioInputStream(audioFilePath)
            // Extract audio format from the input stream
            audioFormat = audioInputStream.format
            // Set the buffer size based on audio format
            bufferSize = audioFormat.sampleRate.toInt() * audioFormat.frameSize * BUFFER_DURATION / 1000
        } catch (ex: Exception) {
            throw IOException("Failed to initialize MP3VoiceSource for file: $audioFilePath", ex)
        }
    }


    override fun getNextAudioChunk(): ByteArray {
        getOriginalAudio().let { buffer ->
            return buffer.array().copyOf(buffer.remaining())
        }
    }

    override fun getOriginalAudio() : ByteBuffer {
        val buffer = ByteArray(bufferSize)
        try {
            val bytesRead = audioInputStream.read(buffer)
            return if (bytesRead != -1) ByteBuffer.wrap(buffer.copyOf(bytesRead)) else ByteBuffer.wrap(byteArrayOf())
        } catch (ex: IOException) {
            throw IOException("Error reading audio data from MP3 file: $audioFilePath", ex)
        }
    }

    override val isFinished: Boolean
        get() = audioInputStream.available() <= 0

    private fun isValidAudioFile(file: File): Boolean {
        try {
            val tika = Tika()
            val mimeType = tika.detect(file)
            return mimeType.startsWith("audio/")
        } catch (e: Exception) {
            throw UnsupportedAudioFileException("The provided file is not a supported audio files.")
        }
    }

    companion object {
        // Duration of audio buffer in milliseconds
        private const val BUFFER_DURATION = 20
    }
}