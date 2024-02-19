/*
 * Copyright 2023 YDWK inc.
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
package io.github.ydwk.yde.entities.message.embed

import io.github.ydwk.yde.entities.util.GenericEntity
import java.net.URL

interface Author : GenericEntity {
    /**
     * The name of this author.
     *
     * @return The name of this author.
     */
    val name: String

    /**
     * The url of this author.
     *
     * @return The url of this author.
     */
    val url: URL?

    /**
     * The icon url of this author.
     *
     * @return The icon url of this author.
     */
    val iconUrl: String?

    /**
     * The proxy icon url of this author.
     *
     * @return The proxy icon url of this author.
     */
    val proxyIconUrl: String?
}
