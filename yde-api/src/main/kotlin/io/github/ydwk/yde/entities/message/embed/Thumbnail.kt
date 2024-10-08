/*
 * Copyright 2024 YDWK inc.
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

interface Thumbnail : GenericEntity {
    /**
     * The url of this thumbnail.
     *
     * @return The url of this thumbnail.
     */
    val url: URL

    /**
     * The proxy url of this thumbnail.
     *
     * @return The proxy url of this thumbnail.
     */
    val proxyUrl: String?

    /**
     * The height of this thumbnail.
     *
     * @return The height of this thumbnail.
     */
    val height: Int?

    /**
     * The width of this thumbnail.
     *
     * @return The width of this thumbnail.
     */
    val width: Int?
}
