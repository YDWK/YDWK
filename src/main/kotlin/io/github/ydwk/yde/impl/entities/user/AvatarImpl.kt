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
package io.github.ydwk.yde.impl.entities.user

import io.github.ydwk.yde.YDE
import io.github.ydwk.yde.entities.user.Avatar
import io.github.ydwk.yde.util.EntityToStringBuilder
import java.awt.image.BufferedImage
import java.net.URL
import java.util.*
import javax.imageio.ImageIO

class AvatarImpl(val yde: YDE, override val url: URL) : Avatar {

    override val asByteArray: ByteArray
        get() = url.readBytes()

    override val asBase64: String
        get() = Base64.getEncoder().encodeToString(asByteArray)

    override val asBufferedImage: BufferedImage
        get() = ImageIO.read(url)

    override fun toString(): String {
        return EntityToStringBuilder(yde, this).add("url", url).toString()
    }
}
