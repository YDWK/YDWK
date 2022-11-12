package io.github.ydwk.ydwk.voice

import io.github.ydwk.ydwk.YDWK
import io.github.ydwk.ydwk.entities.util.AssignableEntity
import io.github.ydwk.ydwk.voice.sub.Track

interface VoiceLocation : AssignableEntity<VoiceLocation> {

    /**
     * Gets the main instance of YDWK.
     *
     * @return The main instance of YDWK.
     */
    val ydwk: YDWK

    /**
     * Skip to the next track in the queue.
     *
     * @return The new track that is playing.
     */
    fun skip(): Track

    /**
     * Skip to the previous track in the queue.
     *
     * @return The new track that is playing.
     */
    fun previous(): Track

    /**
     * Skip to the specified track in the queue.
     *
     * @param index The index of the track to skip to.
     * @return The new track that is playing.
     */
    fun skipTo(index: Int): Track

    /**
     * Skip to the specified track in the queue.
     *
     * @param track The track to skip to.
     * @return The new track that is playing.
     */
    fun skipTo(track: Track): Track

    /**
     * Add a track to the queue.
     *
     * @param track The track to add to the queue.
     * @return The track that was added to the queue.
     */
    fun addTrack(track: Track): Track

    /**
     * Check fif the track has finished playing.
     *
     * @return True if the track has finished playing.
     */
    fun isFinished(): Boolean

    /**
     * Check if the track is currently playing.
     *
     * @return True if the track is currently playing.
     */
    fun isPlaying(): Boolean

    /**
     * Check if the track is currently paused.
     *
     * @return True if the track is currently paused.
     */
    fun isPaused(): Boolean

    /**
     * Check if the track is currently stopped.
     *
     * @return True if the track is currently stopped.
     */
    fun isStopped(): Boolean

    /**
     * Mutes the track.
     *
     * @return The track that was muted.
     */
    fun mute(): Track

    /**
     * Unmutes the track.
     *
     * @return The track that was unmuted.
     */
    fun unmute(): Track

    /**
     * Pauses the track.
     *
     * @return The track that was paused.
     */
    fun pause(): Track

    /**
     * Resumes the track.
     *
     * @return The track that was resumed.
     */
    fun resume(): Track

    /**
     * Stops the track.
     *
     * @return The track that was stopped.
     */
    fun stop(): Track

    /**
     * Sets the volume of the track.
     *
     * @param volume The volume to set the track to.
     * @return The track that was set.
     */
    fun setVolume(volume: Int): Track

    /**
     * Gets the volume of the track.
     *
     * @return The volume of the track.
     */
    fun getVolume(): Int

    /**
     * Gets the current track.
     *
     * @return The current track.
     */
    fun getCurrentTrack(): Track

    /**
     * Gets all the tracks in the queue.
     *
     * @return All the tracks in the queue.
     */
    fun getTracks(): List<Track>

    /**
     * Gets a copy of this voice location.
     *
     * @return A copy of this voice location.
     */
    fun copy(): VoiceLocation
}