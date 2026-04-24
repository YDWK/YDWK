/*
 * Copyright 2024-2026 YDWK inc.
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
package io.github.ydwk.ydwk.voice

import io.github.ydwk.ydwk.voice.opus.VoiceOpusHandler
import java.io.File
import java.io.FileInputStream
import java.util.Arrays

/** Provides audio frames for a Discord voice connection. */
interface VoiceSource {

  /**
   * Returns one 20-ms audio frame.
   *
   * If [isOpusEncoded] is `false`, the caller expects raw signed 16-bit little-endian stereo PCM at
   * 48 kHz ([VoiceOpusHandler.PCM_FRAME_BYTES] bytes). If [isOpusEncoded] is `true`, the bytes are
   * already an Opus packet.
   *
   * When the source is exhausted, return silence (zero-filled PCM frame or empty array).
   */
  fun getAudioData(): ByteArray

  /** Returns `true` once no more audio frames are available. */
  val isFinished: Boolean

  /**
   * Performs any necessary initialisation (e.g. opens file handles, seeks to the beginning). Must
   * be called before the first [getAudioData] call.
   */
  fun build(): VoiceSource

  /**
   * `true` if [getAudioData] returns already-encoded Opus packets; `false` (default) if it returns
   * raw PCM that must be encoded by [VoiceOpusHandler].
   */
  val isOpusEncoded: Boolean
    get() = false

  /**
   * A [VoiceSource] that streams raw PCM from a file.
   *
   * The file must contain signed 16-bit little-endian stereo PCM audio at 48 000 Hz (the exact
   * format Discord's voice server expects after Opus decoding). Each [getAudioData] call returns
   * exactly [VoiceOpusHandler.PCM_FRAME_BYTES] bytes (3 840 bytes, covering 20 ms). The last frame
   * is zero-padded with silence if the file does not end on a frame boundary.
   */
  class ConvertVoiceSource(val file: File) : VoiceSource {

    private val frameBytes = VoiceOpusHandler.PCM_FRAME_BYTES
    private lateinit var inputStream: FileInputStream
    private var finished = false

    override val isOpusEncoded: Boolean = false

    override fun build(): VoiceSource {
      inputStream = FileInputStream(file)
      finished = false
      return this
    }

    override fun getAudioData(): ByteArray {
      if (finished) return silence()
      val frame = ByteArray(frameBytes)
      var offset = 0
      while (offset < frameBytes) {
        val read = inputStream.read(frame, offset, frameBytes - offset)
        if (read == -1) {
          // Pad remainder with silence and mark as finished on next call
          Arrays.fill(frame, offset, frameBytes, 0)
          finished = true
          break
        }
        offset += read
      }
      return frame
    }

    override val isFinished: Boolean
      get() = finished

    private fun silence(): ByteArray = ByteArray(frameBytes)
  }
}
