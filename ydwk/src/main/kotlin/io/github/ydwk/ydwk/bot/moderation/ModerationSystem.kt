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
package io.github.ydwk.ydwk.bot.moderation

import io.github.ydwk.ydwk.evm.backend.event.CoroutineEventListener
import io.github.ydwk.ydwk.evm.backend.event.GenericEvent
import io.github.ydwk.ydwk.evm.event.events.interaction.slash.SlashCommandEvent

/**
 * Event listener that powers the moderation system registered by
 * [io.github.ydwk.ydwk.bot.useModerationSystem].
 *
 * You do not normally instantiate this directly — call the extension function instead.
 */
class ModerationSystem(private val config: ModerationConfig) : CoroutineEventListener {

  override suspend fun onEvent(event: GenericEvent) {
    if (event !is SlashCommandEvent) return
    val name = event.slash.name
    when {
      name == config.warnCommandName -> handleWarn(event)
      name == config.kickCommandName -> handleKick(event)
      name == config.banCommandName -> handleBan(event)
      name == config.unbanCommandName -> handleUnban(event)
      name == config.warnsCommandName -> handleWarns(event)
      name == config.clearWarnsCommandName -> handleClearWarns(event)
    }
  }

  // ── /warn ──────────────────────────────────────────────────────────────────

  private suspend fun handleWarn(event: SlashCommandEvent) {
    val db = config.database ?: run {
      event.slash.reply("No database is configured for the moderation system.").setEphemeral(true).send()
      return
    }
    val guildId = event.slash.guildId?.asString ?: run {
      event.slash.reply("This command can only be used inside a server.").setEphemeral(true).send()
      return
    }
    val targetUser = event.slash.getOption("user")?.asUser ?: run {
      event.slash.reply("Please mention a valid user.").setEphemeral(true).send()
      return
    }
    val reason = event.slash.getOption("reason")?.asString ?: "No reason provided"
    val moderatorId = event.slash.user.id

    db.addWarn(guildId, targetUser.id, reason, moderatorId)
    val total = db.getWarnCount(guildId, targetUser.id)

    val autoBanThreshold = config.autoBanAt
    val autoBanNote =
      if (autoBanThreshold > 0 && total >= autoBanThreshold) " — auto-ban triggered." else ""

    event.slash.reply(
        "${targetUser.username} warned. Reason: $reason | Warnings: $total$autoBanNote"
      )
      .send()

    if (autoBanThreshold > 0 && total >= autoBanThreshold) {
      val guild = event.ydwk.getGuildById(guildId) ?: return
      guild.banUser(targetUser, reason = "Auto-banned after $total warnings")
      db.clearWarns(guildId, targetUser.id)
    }
  }

  // ── /kick ──────────────────────────────────────────────────────────────────

  private suspend fun handleKick(event: SlashCommandEvent) {
    val guildId = event.slash.guildId?.asString ?: run {
      event.slash.reply("This command can only be used inside a server.").setEphemeral(true).send()
      return
    }
    val targetMember = event.slash.getOption("user")?.asMember ?: run {
      event.slash.reply("Please mention a valid server member.").setEphemeral(true).send()
      return
    }
    val reason = event.slash.getOption("reason")?.asString
    val guild = event.ydwk.getGuildById(guildId) ?: return

    guild.kickMember(targetMember, reason)
    event.slash
      .reply("${targetMember.user.username} has been kicked. Reason: ${reason ?: "No reason provided"}")
      .send()
  }

  // ── /ban ───────────────────────────────────────────────────────────────────

  private suspend fun handleBan(event: SlashCommandEvent) {
    val guildId = event.slash.guildId?.asString ?: run {
      event.slash.reply("This command can only be used inside a server.").setEphemeral(true).send()
      return
    }
    val targetUser = event.slash.getOption("user")?.asUser ?: run {
      event.slash.reply("Please mention a valid user.").setEphemeral(true).send()
      return
    }
    val reason = event.slash.getOption("reason")?.asString
    val guild = event.ydwk.getGuildById(guildId) ?: return

    guild.banUser(targetUser, reason = reason)
    event.slash
      .reply("${targetUser.username} has been banned. Reason: ${reason ?: "No reason provided"}")
      .send()
  }

  // ── /unban ─────────────────────────────────────────────────────────────────

  private suspend fun handleUnban(event: SlashCommandEvent) {
    val guildId = event.slash.guildId?.asString ?: run {
      event.slash.reply("This command can only be used inside a server.").setEphemeral(true).send()
      return
    }
    val targetUser = event.slash.getOption("user")?.asUser ?: run {
      event.slash.reply("Please mention a valid user.").setEphemeral(true).send()
      return
    }
    val reason = event.slash.getOption("reason")?.asString
    val guild = event.ydwk.getGuildById(guildId) ?: return

    guild.unbanUser(targetUser, reason)
    event.slash.reply("${targetUser.username} has been unbanned.").send()
  }

  // ── /warns ─────────────────────────────────────────────────────────────────

  private suspend fun handleWarns(event: SlashCommandEvent) {
    val db = config.database ?: run {
      event.slash.reply("No database is configured for the moderation system.").setEphemeral(true).send()
      return
    }
    val guildId = event.slash.guildId?.asString ?: run {
      event.slash.reply("This command can only be used inside a server.").setEphemeral(true).send()
      return
    }
    val targetUser = event.slash.getOption("user")?.asUser ?: run {
      event.slash.reply("Please mention a valid user.").setEphemeral(true).send()
      return
    }

    val warns = db.getWarns(guildId, targetUser.id)
    if (warns.isEmpty()) {
      event.slash.reply("${targetUser.username} has no warnings.").setEphemeral(true).send()
      return
    }

    val list =
      warns.mapIndexed { i, w -> "${i + 1}. ${w.reason} (by <@${w.moderatorId}>)" }.joinToString("\n")
    event.slash
      .reply("**${targetUser.username}** has **${warns.size}** warning(s):\n$list")
      .setEphemeral(true)
      .send()
  }

  // ── /clearwarns ────────────────────────────────────────────────────────────

  private suspend fun handleClearWarns(event: SlashCommandEvent) {
    val db = config.database ?: run {
      event.slash.reply("No database is configured for the moderation system.").setEphemeral(true).send()
      return
    }
    val guildId = event.slash.guildId?.asString ?: run {
      event.slash.reply("This command can only be used inside a server.").setEphemeral(true).send()
      return
    }
    val targetUser = event.slash.getOption("user")?.asUser ?: run {
      event.slash.reply("Please mention a valid user.").setEphemeral(true).send()
      return
    }

    db.clearWarns(guildId, targetUser.id)
    event.slash.reply("Cleared all warnings for ${targetUser.username}.").send()
  }
}
