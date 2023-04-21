/*
 * Copyright 2022 YDWK inc.
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
package io.github.ydwk.ydwk.ws.voice

import com.fasterxml.jackson.databind.JsonNode
import io.github.ydwk.ydwk.voice.util.VoiceCloseCode
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetSocketAddress
import java.nio.ByteBuffer

// connection related

// UDP is a communication protocol that transmits independent packets over the network with no
// guarantee of arrival and no guarantee of the order of delivery.
internal fun VoiceWebSocket.handleUDP(address: InetSocketAddress, ssrc: Int): InetSocketAddress? {
    try {
        // check if udp is already connected and if so close it
        if (voiceConnection.udpsocket != null) {
            voiceConnection.udpsocket!!.close()
        }

        // create a new udp socket
        voiceConnection.udpsocket = DatagramSocket()

        // create a byte array of length 70 containing the ssrc
        val ssrcBytes = ByteBuffer.allocate(70)
        ssrcBytes.putShort(1.toShort())
        ssrcBytes.putShort(70.toShort())
        ssrcBytes.putInt(ssrc)

        // create a new datagram packet with the ssrc bytes and the address
        val packet = DatagramPacket(ssrcBytes.array(), ssrcBytes.array().size, address)
        voiceConnection.udpsocket!!.send(packet)

        // Discord returns a packet with the port and ip of the udp socket
        val receivedPacket = DatagramPacket(ByteArray(74), 74)
        voiceConnection.udpsocket!!.soTimeout = 1000
        voiceConnection.udpsocket!!.receive(receivedPacket)

        // get the received bytes and convert them to a string
        val receivedBytes: ByteArray = receivedPacket.data

        // get the ip from the received bytes
        var ip = String(receivedBytes, 8, receivedBytes.size - 10)
        ip = ip.trim()

        // get the port from the received bytes and end it with 0xFFFF to get the correct port
        val port = getPort(receivedBytes, receivedBytes.size - 2).toInt() or 0xFFFF
        voiceConnection.address = address
        return InetSocketAddress(ip, port)
    } catch (e: Exception) {
        logger.error("Error while sending encoded data", e)
        return null
    }
}

internal fun VoiceWebSocket.getPort(array: ByteArray, offset: Int): Short {
    return (array[offset].toInt() and 0xff shl 8 or array[offset + 1].toInt() and 0xff).toShort()
}

internal fun VoiceWebSocket.stopSendingEncodedData() {
    try {
        if (voiceConnection.udpsocket != null) {
            voiceConnection.udpsocket!!.close()
        }
    } catch (e: Exception) {
        logger.error("Error while stopping sending encoded data", e)
    }
}

// payload related

internal fun VoiceWebSocket.resume() {
    val resumePayload = ydwk.objectNode
    resumePayload.put("op", io.github.ydwk.ydwk.voice.util.VoiceOpcode.RESUME.code)
    val resumeData = ydwk.objectNode
    resumeData.put("server_id", voiceConnection.channel.guild.idAsLong)
    resumeData.put("session_id", voiceConnection.sessionId)
    resumeData.put("token", voiceConnection.token)
    resumePayload.set<JsonNode>("d", resumeData)
    webSocket?.sendText(resumePayload.toString())
}

internal fun VoiceWebSocket.identify() {
    val identifyPayload = ydwk.objectNode
    identifyPayload.put("op", io.github.ydwk.ydwk.voice.util.VoiceOpcode.IDENTIFY.code)
    val identifyData = ydwk.objectNode
    identifyData.put("server_id", voiceConnection.channel.guild.idAsLong)
    identifyData.put("user_id", voiceConnection.userId)
    identifyData.put("session_id", voiceConnection.sessionId)
    identifyData.put("token", voiceConnection.token)
    identifyPayload.set<JsonNode>("d", identifyData)
    webSocket?.sendText(identifyPayload.toString())
}

internal fun VoiceWebSocket.onSelectProtocol(ourInetSocketAddress: InetSocketAddress) {
    val selectProtocolPayload = ydwk.objectNode
    selectProtocolPayload.put("op", io.github.ydwk.ydwk.voice.util.VoiceOpcode.SELECT_PROTOCOL.code)

    val selectProtocolData = ydwk.objectNode
    selectProtocolData.put("protocol", "udp")

    val dataPayload = ydwk.objectNode
    dataPayload.put("address", ourInetSocketAddress.hostString)
    dataPayload.put("port", ourInetSocketAddress.port)
    dataPayload.put("mode", encryptionMode!!.getPreferredMode())

    selectProtocolPayload.set<JsonNode>("d", selectProtocolData)
    selectProtocolData.set<JsonNode>("data", dataPayload)

    webSocket?.sendText(selectProtocolPayload.toString())
}

internal fun VoiceWebSocket.sendSpeaking() {
    val speakingPayload = ydwk.objectNode
    speakingPayload.put("op", io.github.ydwk.ydwk.voice.util.VoiceOpcode.SPEAKING.code)
    val speakingData = ydwk.objectNode

    val speakFlags: Long = 0
    for (speakFlag in voiceConnection.speakingFlags) {
        speakFlags.or(speakFlag.getValue())
    }

    speakingData.put("speaking", speakFlags)
    speakingData.put("delay", 0)
    speakingData.put("ssrc", ssrc)
    speakingPayload.set<JsonNode>("d", speakingData)
    webSocket?.sendText(speakingPayload.toString())
}

internal fun VoiceWebSocket.sendCloseCode(code: VoiceCloseCode) {
    checkNotNull(webSocket) { "WebSocket is null" }
    webSocket!!.sendClose(code.code, code.reason)
}
