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
