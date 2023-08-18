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
package io.github.ydwk.ydwk.ws.logging

import com.neovisionaries.ws.client.*
import org.slf4j.Logger

class WebsocketLogging(val logger: Logger) : WebSocketListener {

    override fun onStateChanged(websocket: WebSocket?, newState: WebSocketState?) {
        logger.isTraceEnabled.let {
            if (it) {
                logger.trace("Websocket state changed to {}", newState)
            }
        }
    }

    override fun onConnected(
        websocket: WebSocket?,
        headers: MutableMap<String, MutableList<String>>?,
    ) {
        logger.trace("Websocket connected with headers {}", headers?.forEach { (key, value) ->
            logger.trace("Key: {}, Value: {}", key, value)
        } ?: "null")
    }

    override fun onConnectError(websocket: WebSocket?, cause: WebSocketException?) {
        logger.trace("Websocket connect error with cause {}", cause?.cause ?: "Missing cause")
    }

    override fun onDisconnected(
        websocket: WebSocket?,
        serverCloseFrame: WebSocketFrame?,
        clientCloseFrame: WebSocketFrame?,
        closedByServer: Boolean,
    ) {
        when {
            serverCloseFrame != null -> logger.trace("Websocket disconnected by server")
            clientCloseFrame != null -> logger.trace("Websocket disconnected by client")
            else -> logger.trace("Websocket disconnected")
        }
    }

    override fun onFrame(websocket: WebSocket?, frame: WebSocketFrame?) {
        logger.trace("Websocket frame received")
    }

    override fun onContinuationFrame(websocket: WebSocket?, frame: WebSocketFrame?) {
        logger.trace("Websocket continuation frame received")
    }

    override fun onTextFrame(websocket: WebSocket?, frame: WebSocketFrame?) {
        logger.trace("Websocket text frame received")
    }

    override fun onBinaryFrame(websocket: WebSocket?, frame: WebSocketFrame?) {
        logger.trace("Websocket binary frame received")
    }

    override fun onCloseFrame(websocket: WebSocket?, frame: WebSocketFrame?) {
        logger.trace("Websocket close frame received")
    }

    override fun onPingFrame(websocket: WebSocket?, frame: WebSocketFrame?) {
        logger.trace("Websocket ping frame received")
    }

    override fun onPongFrame(websocket: WebSocket?, frame: WebSocketFrame?) {
        logger.trace("Websocket pong frame received")
    }

    override fun onTextMessage(websocket: WebSocket?, text: String?) {
        logger.trace("Websocket text message received $text")
    }

    override fun onTextMessage(websocket: WebSocket?, data: ByteArray?) {
        logger.trace("Websocket text message received {}", data)
    }

    override fun onBinaryMessage(websocket: WebSocket?, binary: ByteArray?) {
        logger.trace("Websocket binary message received {}", binary)
    }

    override fun onSendingFrame(websocket: WebSocket?, frame: WebSocketFrame?) {
        logger.trace("Websocket sending frame")
    }

    override fun onFrameSent(websocket: WebSocket?, frame: WebSocketFrame?) {
        logger.trace("Websocket frame sent")
    }

    override fun onFrameUnsent(websocket: WebSocket?, frame: WebSocketFrame?) {
        logger.trace("Websocket frame unsent")
    }

    override fun onThreadCreated(websocket: WebSocket?, threadType: ThreadType?, thread: Thread?) {
        logger.trace("Websocket thread created {} {}", threadType, thread)
    }

    override fun onThreadStarted(websocket: WebSocket?, threadType: ThreadType?, thread: Thread?) {
        logger.trace("Websocket thread started {} {}", threadType, thread)
    }

    override fun onThreadStopping(websocket: WebSocket?, threadType: ThreadType?, thread: Thread?) {
        logger.trace("Websocket thread stopping {} {}", threadType, thread)
    }

    override fun onError(websocket: WebSocket?, cause: WebSocketException?) {
        logger.trace("Websocket error with cause {}", cause)
    }

    override fun onFrameError(
        websocket: WebSocket?,
        cause: WebSocketException?,
        frame: WebSocketFrame?,
    ) {
        logger.trace("Websocket frame error with cause {} and frame {}", cause, frame)
    }

    override fun onMessageError(
        websocket: WebSocket?,
        cause: WebSocketException?,
        frames: MutableList<WebSocketFrame>?,
    ) {
        logger.trace("Websocket message error with cause {} and frames {}", cause, frames)
    }

    override fun onMessageDecompressionError(
        websocket: WebSocket?,
        cause: WebSocketException?,
        compressed: ByteArray?,
    ) {
        logger.trace("Websocket message decompression error with cause {} and compressed {}", cause, compressed)
    }

    override fun onTextMessageError(
        websocket: WebSocket?,
        cause: WebSocketException?,
        data: ByteArray?,
    ) {
        logger.trace("Websocket text message error with cause {} and data {}", cause, data)
    }

    override fun onSendError(
        websocket: WebSocket?,
        cause: WebSocketException?,
        frame: WebSocketFrame?,
    ) {
        logger.trace("Websocket send error with cause {} and frame {}", cause, frame)
    }

    override fun onUnexpectedError(websocket: WebSocket?, cause: WebSocketException?) {
        logger.trace("Websocket unexpected error with cause {}", cause?.cause ?: "Missing cause")
    }

    override fun handleCallbackError(websocket: WebSocket?, cause: Throwable?) {
        logger.trace("Websocket callback error with cause {}", cause?.cause ?: "Missing cause")
    }

    override fun onSendingHandshake(
        websocket: WebSocket?,
        requestLine: String?,
        headers: MutableList<Array<String>>?,
    ) {
        logger.trace("Websocket sending handshake with request line {} and headers {}", requestLine, headers)
    }
}
