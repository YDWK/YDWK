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
package io.github.ydwk.yde.rest.error

enum class HttpResponseCode(private val code: Int, private val message: String) {
    OK(200, "The request completed successfully."),
    CREATED(201, "The request completed successfully and a new resource was created."),
    NO_CONTENT(204, "The request completed successfully but returned no content."),
    NOT_MODIFIED(304, "The resource was not modified."),
    BAD_REQUEST(400, "The request was improperly formatted, or the server couldn't understand it."),
    UNAUTHORIZED(401, "The Authorization header was missing or invalid."),
    FORBIDDEN(403, "The Authorization token you passed did not have permission to the resource."),
    NOT_FOUND(404, "The resource at the location specified doesn't exist."),
    METHOD_NOT_ALLOWED(405, "The HTTP method used is not valid for the location specified."),
    NOT_ACCEPTABLE(406, "You requested a format that isn't json."),
    TOO_MANY_REQUESTS(429, "You are being rate limited."),
    GATEWAY_UNAVAILABLE(
        502, "The Discord servers are temporarily down or overloaded. Try again later."),
    UNKNOWN(-1, "Unknown error code.");

    companion object {
        /**
         * The [HttpResponseCode] from the code
         *
         * @param code The code to get the [HttpResponseCode] from.
         * @return The [HttpResponseCode] from the given [code].
         */
        fun fromInt(code: Int): HttpResponseCode {
            return values().firstOrNull { it.code == code } ?: UNKNOWN
        }
    }

    /**
     * The code of the [HttpResponseCode].
     *
     * @return The code of the [HttpResponseCode].
     */
    fun getCode(): Int {
        return code
    }

    /**
     * The message of the [HttpResponseCode].
     *
     * @return The message of the [HttpResponseCode].
     */
    fun getMessage(): String {
        return message
    }
}
