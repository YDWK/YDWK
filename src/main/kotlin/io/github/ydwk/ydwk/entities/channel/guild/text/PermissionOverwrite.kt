package io.github.ydwk.ydwk.entities.channel.guild.text

import io.github.ydwk.ydwk.entities.util.GenericEntity
import io.github.ydwk.ydwk.util.SnowFlake

interface PermissionOverwrite : GenericEntity, SnowFlake {
    /**
     * Gets the type of this permission overwrite.
     *
     * @return the type of this permission overwrite.
     */
    val type: Int

    /**
     * Gets to allow of this permission overwrite.
     *
     * @return the allow of this permission overwrite.
     */
    val allow: String

    /**
     * Gets the deny of this permission overwrite.
     *
     * @return the deny of this permission overwrite.
     */
    val deny: String
}