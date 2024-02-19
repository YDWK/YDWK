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
package io.github.ydwk.yde.entities.user

import java.awt.image.BufferedImage
import java.net.URL

interface Avatar {

    /**
     * The url belonging to the avatar.
     *
     * @return the url belonging to the avatar.
     */
    val url: URL

    /**
     * The icon as a byte array.
     *
     * @return the icon as a byte array.
     */
    val asByteArray: ByteArray

    /**
     * The icon as a base64 string.
     *
     * @return the icon as a base64 string.
     */
    val asBase64: String

    /**
     * The icon as a buffer image.
     *
     * @return the icon as a buffer image.
     */
    val asBufferedImage: BufferedImage
}
