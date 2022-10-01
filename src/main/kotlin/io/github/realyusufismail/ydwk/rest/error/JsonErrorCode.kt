/*
 * Copyright 2022 Yusuf Arfan Ismail and other YDWK contributors.
 *
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 *
 * you may not use this file except in compliance with the License.
 *
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */ 
package io.github.realyusufismail.ydwk.rest.error

enum class JsonErrorCode(private val code: Int, private val meaning: String) {
    // take all the codes from here
    // https://discord.com/developers/docs/topics/opcodes-and-status-codes#json-json-error-codes and
    // put them here
    GENERAL_ERROR(0, "General error (such as a malformed request body, amongst other things)"),
    UNKNOWN_ACCOUNT(10001, "Unknown account"),
    UNKNOWN_APPLICATION(10002, "Unknown application"),
    UNKNOWN_CHANNEL(10003, "Unknown channel"),
    UNKNOWN_GUILD(10004, "Unknown guild"),
    UNKNOWN_INTEGRATION(10005, "Unknown integration"),
    UNKNOWN_INVITE(10006, "Unknown invite"),
    UNKNOWN_MEMBER(10007, "Unknown member"),
    UNKNOWN_MESSAGE(10008, "Unknown message"),
    UNKNOWN_OVERWRITE(10009, "Unknown overwrite"),
    UNKNOWN_PROVIDER(10010, "Unknown provider"),
    UNKNOWN_ROLE(10011, "Unknown role"),
    UNKNOWN_TOKEN(10012, "Unknown token"),
    UNKNOWN_USER(10013, "Unknown user"),
    UNKNOWN_EMOJI(10014, "Unknown emoji"),
    UNKNOWN_WEBHOOK(10015, "Unknown webhook"),
    UNKNOWN_WEBHOOK_SERVICE(10016, "Unknown webhook service"),
    UNKNOWN_SESSION(10020, "Unknown session"),
    UNKNOWN_BAN(10026, "Unknown ban"),
    UNKNOWN_SKU(10027, "Unknown SKU"),
    UNKNOWN_STORE_LISTING(10028, "Unknown Store Listing"),
    UNKNOWN_ENTITLEMENT(10029, "Unknown entitlement"),
    UNKNOWN_BUILD(10030, "Unknown build"),
    UNKNOWN_LOBBY(10031, "Unknown lobby"),
    UNKNOWN_BRANCH(10032, "Unknown branch"),
    UNKNOWN_STORE_DIRECTORY_LAYOUT(10036, "Unknown store directory layout"),
    UNKNOWN_REDISTRIBUTABLE(10038, "Unknown redistributable"),
    UNKNOWN_GIFT_CODE(10050, "Unknown gift code"),
    UNKNOWN_STREAM(10060, "Unknown stream"),
    UNKNOWN_PREMIUM_SERVER_SUBSCRIPTION(10062, "Unknown premium server subscription"),
    UNKNOWN_GUILD_TEMPLATE(10057, "Unknown guild template"),
    UNKNOWN_DISCOVERABLE_SERVER_CATEGORY(10059, "Unknown discoverable server category"),
    UNKNOWN_STICKER(10060, "Unknown sticker"),
    UNKNOWN_INTERACTION(10062, "Unknown interaction"),
    UNKNOWN_APPLICATION_COMMAND(10063, "Unknown application command"),
}
