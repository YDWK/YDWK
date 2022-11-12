package io.github.ydwk.ydwk.voice.sub

interface Track {

    /**
     * Gets the title of the track.
     *
     * @return The title of the track.
     */
    val title: String

    /**
     * Gets the author of the track.
     *
     * @return The author of the track.
     */
    val author: String

    /**
     * Gets the duration of the track.
     *
     * @return The duration of the track.
     */
    val duration: Long
}