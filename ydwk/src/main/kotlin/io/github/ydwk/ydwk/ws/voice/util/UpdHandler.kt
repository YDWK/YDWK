/*
 * Copyright 2024 YDWK inc.
 *
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 *
 * you may not use this file except in compliance with the License.
 *
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */ 
package io.github.ydwk.ydwk.ws.voice.util

import com.sun.jna.ptr.PointerByReference
import io.github.ydwk.ydwk.voice.impl.VoiceConnectionImpl
import io.github.ydwk.ydwk.voice.packet.opus.OpusEncoder
import io.github.ydwk.ydwk.ws.voice.payload.VoiceReadyPayload
import tomp2p.opuswrapper.Opus
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetSocketAddress
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.IntBuffer

class UpdHandler(
    private val voiceConnection: VoiceConnectionImpl,
    private val voiceReadyPayload: VoiceReadyPayload,
    private val inetSocketAddress: InetSocketAddress,
    private val socket: DatagramSocket = DatagramSocket()
) {
    // TODO: In separate pr incorporate the speaking part
    var secretKey: ByteArray? = null
    var opusEncoder: PointerByReference? = null

    fun findIp(): InetSocketAddress {
        var responseData = ByteArray(74)
        // Prepare a buffer with request data
        val requestData = ByteBuffer.wrap(responseData).order(ByteOrder.BIG_ENDIAN)
        requestData.putShort(1) // type - request
        requestData.putShort(70) // length
        requestData.putInt(voiceReadyPayload.ssrc) // SSRC

        // Construct and send a DatagramPacket with the request data
        val requestPacket = DatagramPacket(responseData, responseData.size, inetSocketAddress)
        socket.send(requestPacket)

        // Prepare a buffer to hold the response
        responseData = ByteArray(74)

        // Construct a DatagramPacket to receive the response
        val responsePacket = DatagramPacket(responseData, responseData.size)
        socket.receive(responsePacket)

        // Parse the response to extract the external ip and port
        val externalIP = String(responseData, 8, responseData.size - 10)

        // Create a new buffer to extract the port number, considering only last 2 bytes and
        // converting it to Int
        val externalPort =
            ByteBuffer.wrap(
                    byteArrayOf(
                        responseData[responseData.size - 1], responseData[responseData.size - 2]))
                .getShort()
                .toInt() and 0xffff

        // Return the InetSocketAddress
        return InetSocketAddress(externalIP, externalPort)
    }

    fun encodeAudio(audio : ByteBuffer) : ByteBuffer {
        if (opusEncoder == null) {
            // TODO : Add to chekc if the opus library is loaded

            val error = IntBuffer.allocate(1)
            opusEncoder = Opus.INSTANCE.opus_encoder_create(48000, 2, Opus.OPUS_APPLICATION_VOIP, error)
            if (error.get() != Opus.OPUS_OK) {
                throw IllegalStateException("Could not create Opus encoder: ${Opus.INSTANCE.opus_strerror(error.get())}")
            }
        }

        return OpusEncoder().encode(audio) ?: throw IllegalStateException("Could not encode audio")
    }

}
