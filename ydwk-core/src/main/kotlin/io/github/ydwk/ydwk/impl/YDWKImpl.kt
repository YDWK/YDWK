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
package io.github.ydwk.ydwk.impl

import io.github.ydwk.yde.entities.Application
import io.github.ydwk.yde.entities.Bot
import io.github.ydwk.yde.entities.application.PartialApplication
import io.github.ydwk.yde.impl.YDEImpl
import io.github.ydwk.yde.util.EntityToStringBuilder
import io.github.ydwk.yde.util.ThreadFactory
import io.github.ydwk.ydwk.*
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import okhttp3.OkHttpClient
import org.slf4j.LoggerFactory

class YDWKImpl(
    override var client: OkHttpClient,
    public override var token: String? = null,
    override var guildIdList: MutableList<String> = mutableListOf(),
    applicationId: String? = null,
) :
    YDWK,
    YDEImpl(
        token,
        applicationId,
        client,
        guildIdList,
        YDWKInfo.GITHUB_URL.getUrl(),
        YDWKInfo.YDWK_VERSION.getUrl()) {

    val ydwkLogger = LoggerFactory.getLogger(YDWK::class.java)

    override val defaultScheduledExecutorService: ScheduledExecutorService =
        Executors.newScheduledThreadPool(1)

    override val threadFactory: ThreadFactory
        get() = ThreadFactory

    fun shutDownRestApi() {
        client.dispatcher.executorService.shutdown()
        client.connectionPool.evictAll()
        client.cache?.close()
    }

    override fun setGuildIds(vararg guildIds: String) {
        guildIds.forEach { this.guildIdList.add(it) }
    }

    override var bot: Bot? = null
        get() {
            while (field == null) {
                try {
                    Thread.sleep(1000)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
            } // wait for bot to be set
            return field
        }

    override var partialApplication: PartialApplication? = null
        get() {
            while (field == null) {
                try {
                    Thread.sleep(1000)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                } finally {
                    if (field == null) {
                        error("Partial Application is null")
                    }
                }
            } // wait for application to be set
            return field
        }

    override var application: Application? = null
        get() {
            while (field == null) {
                try {
                    Thread.sleep(1000)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                } finally {
                    if (field == null) {
                        error("Application is null")
                    }
                }
            } // wait for application to be set
            return field
        }

    override fun toString(): String {
        return EntityToStringBuilder(this, this)
            .add("token", token)
            .add("applicationId", applicationId)
            .toString()
    }
}
