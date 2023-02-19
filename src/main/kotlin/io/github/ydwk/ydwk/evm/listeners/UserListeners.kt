package io.github.ydwk.ydwk.evm.listeners

import io.github.ydwk.ydwk.evm.backend.event.GenericEvent
import io.github.ydwk.ydwk.evm.backend.event.IEventListener
import io.github.ydwk.ydwk.evm.event.events.user.*

interface UserListeners : IEventListener {
    /**
     * Listens to UserNameUpdateEvent
     *
     * @param event The UserNameUpdateEvent
     */
    fun onUserNameUpdate(event: UserNameUpdateEvent) {}

    /**
     * Listens to UserDiscriminatorUpdateEvent
     *
     * @param event The UserDiscriminatorUpdateEvent
     */
    fun onUserDiscriminatorUpdate(event: UserDiscriminatorUpdateEvent) {}

    /**
     * Listens to UserAvatarUpdateEvent
     *
     * @param event The UserAvatarUpdateEvent
     */
    fun onUserAvatarUpdate(event: UserAvatarUpdateEvent) {}

    /**
     * Listens to UserSystemUpdateEvent
     *
     * @param event The UserSystemUpdateEvent
     */
    fun onUserSystemUpdate(event: UserSystemUpdateEvent) {}

    /**
     * Listens to UserMfaEnabledUpdateEvent
     *
     * @param event The UserMfaEnabledUpdateEvent
     */
    fun onUserMfaEnabledUpdate(event: UserMfaEnabledUpdateEvent) {}

    /**
     * Listens to UserBannerUpdateEvent
     *
     * @param event The UserBannerUpdateEvent
     */
    fun onUserBannerUpdate(event: UserBannerUpdateEvent) {}

    /**
     * Listens to UserAccentColorUpdateEvent
     *
     * @param event The UserAccentColorUpdateEvent
     */
    fun onUserAccentColorUpdate(event: UserAccentColorUpdateEvent) {}

    /**
     * Listens to UserLocaleUpdateEvent
     *
     * @param event The UserLocaleUpdateEvent
     */
    fun onUserLocaleUpdate(event: UserLocaleUpdateEvent) {}

    /**
     * Listens to UserVerifiedUpdateEvent
     *
     * @param event The UserVerifiedUpdateEvent
     */
    fun onUserVerifiedUpdate(event: UserVerifiedUpdateEvent) {}

    /**
     * Listens to UserFlagsUpdateEvent
     *
     * @param event The UserFlagsUpdateEvent
     */
    fun onUserFlagsUpdate(event: UserFlagsUpdateEvent) {}

    override fun onEvent(event: GenericEvent) {
        when (event) {
            is UserNameUpdateEvent -> onUserNameUpdate(event)
            is UserDiscriminatorUpdateEvent -> onUserDiscriminatorUpdate(event)
            is UserAvatarUpdateEvent -> onUserAvatarUpdate(event)
            is UserSystemUpdateEvent -> onUserSystemUpdate(event)
            is UserMfaEnabledUpdateEvent -> onUserMfaEnabledUpdate(event)
            is UserBannerUpdateEvent -> onUserBannerUpdate(event)
            is UserAccentColorUpdateEvent -> onUserAccentColorUpdate(event)
            is UserLocaleUpdateEvent -> onUserLocaleUpdate(event)
            is UserVerifiedUpdateEvent -> onUserVerifiedUpdate(event)
            is UserFlagsUpdateEvent -> onUserFlagsUpdate(event)
        }
    }
}