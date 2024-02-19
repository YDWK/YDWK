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
package io.github.ydwk.yde.util

import java.util.*

interface NameAbleEntity : Formattable {
    /** @return the name of the entity */
    var name: String

    /**
     * Formats the name of the entity
     *
     * @param format the format of the name
     * @return the formatted name
     */
    fun format(format: String): String {
        return String.format(format, this)
    }

    /**
     * Formats the object using the provided {@link Formatter formatter}.
     *
     * @param formatter
     *
     * ```
     *         The {@link Formatter formatter}.  Implementing classes may call
     *         {@link Formatter#out() formatter.out()} or {@link
     *         Formatter#locale() formatter.locale()} to obtain the {@link
     *         Appendable} or {@link Locale} used by this
     *         {@code formatter} respectively.
     *
     * @param flags
     *         The flags modify the output format.  The value is interpreted as
     *         a bitmask.  Any combination of the following flags may be set:
     *         {@link FormattableFlags#LEFT_JUSTIFY}, {@link
     *         FormattableFlags#UPPERCASE}, and {@link
     *         FormattableFlags#ALTERNATE}.  If no flags are set, the default
     *         formatting of the implementing class will apply.
     *
     * @param width
     *         The minimum number of characters to be written to the output.
     *         If the length of the converted value is less than the
     *         {@code width} then the output will be padded by
     *         <code>'&nbsp;&nbsp;'</code> until the total number of characters
     *         equals width.  The padding is at the beginning by default.  If
     *         the {@link FormattableFlags#LEFT_JUSTIFY} flag is set then the
     *         padding will be at the end.  If {@code width} is {@code -1}
     *         then there is no minimum.
     *
     * @param precision
     *         The maximum number of characters to be written to the output.
     *         The precision is applied before the width, thus the output will
     *         be truncated to {@code precision} characters even if the
     *         {@code width} is greater than the {@code precision}.  If
     *         {@code precision} is {@code -1} then there is no explicit
     *         limit on the number of characters.
     *
     * @throws IllegalFormatException
     *          If any of the parameters are invalid.  For specification of all
     *          possible formatting errors, see the <a
     *          href="../util/Formatter.html#detail">Details</a> section of the
     *          formatter class specification.
     * ```
     */
    override fun formatTo(formatter: Formatter?, flags: Int, width: Int, precision: Int) {
        formatter?.format("%s", name)
    }
}
