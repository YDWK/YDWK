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
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */ 
package io.github.ydwk.ydwk.ws.voice.util

import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetSocketAddress
import java.nio.ByteBuffer

fun findIp(inetSocketAddress: InetSocketAddress): InetSocketAddress {
    val socket = DatagramSocket()
    val address = InetSocketAddress(inetSocketAddress.hostName, inetSocketAddress.port)
    socket.send(DatagramPacket(byteArrayOf(0x01, 0x04), 2, address))
    val buffer = ByteArray(70)
    val packet = DatagramPacket(buffer, buffer.size)
    socket.receive(packet)
    val data = packet.data
    val ipBytes = ByteArray(4)
    System.arraycopy(data, 4, ipBytes, 0, 4)
    val portBytes = ByteArray(2)
    // port is in the 2nd and 3rd bytes
    System.arraycopy(data, 2, portBytes, 0, 2)
    // get the ip and port
    val bufferPort = ByteBuffer.wrap(portBytes).short.toInt()
    // get the ip address
    val newIp = ipBytes.joinToString(".") { it.toInt().toString() }
    return InetSocketAddress(newIp, bufferPort)
}
