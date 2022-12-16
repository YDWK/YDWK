package io.github.ydwk.ydwk.impl.entities.builder

import io.github.ydwk.ydwk.YDWK
import io.github.ydwk.ydwk.entities.builder.EntityBuilder
import io.github.ydwk.ydwk.entities.builder.GuildBuilder
import io.github.ydwk.ydwk.entities.builder.guild.GuildEntitiesBuilder

class EntityBuilderImpl(val ydwk : YDWK) : EntityBuilder {
    override fun createGuild(name: String): GuildBuilder {
        return GuildBuilderImpl(ydwk, name)
    }

    override fun getGuild(id: String): GuildEntitiesBuilder {
        TODO("Not yet implemented")
    }
}