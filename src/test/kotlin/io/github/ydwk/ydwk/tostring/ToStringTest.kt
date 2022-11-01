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
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */ 
package io.github.ydwk.ydwk.tostring

import io.github.ydwk.ydwk.util.EntityToStringBuilder
import kotlin.test.assertEquals
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test

class ToStringTest {

    @Test
    @Order(1)
    fun testNameAsClassName() {
        assertEquals("SampleEntity", EntityToStringBuilder(SampleEntity()).toString())

        assertEquals(
            "SampleEntity:SampleEntity",
            EntityToStringBuilder(SampleEntity()).name("SampleEntity").toString())
    }

    @Test
    @Order(2)
    fun testNameAsString() {
        assertEquals("SampleEntity", EntityToStringBuilder("SampleEntity").toString())

        assertEquals(
            "SampleEntity:SampleEntity",
            EntityToStringBuilder("SampleEntity").name("SampleEntity").toString())
    }

    @Test
    @Order(3)
    fun testSnowflake() {

        assertEquals(
            "SampleSnowflakeEntity(id=1,createTime=1420070400000,workerId=0,increment=1)",
            EntityToStringBuilder(SampleSnowflakeEntity(1)).toString())

        assertEquals(
            "SampleSnowflakeEntity:SampleSnowflakeEntity(id=2,createTime=1420070400000,workerId=0,increment=2)",
            EntityToStringBuilder(SampleSnowflakeEntity(2))
                .name("SampleSnowflakeEntity")
                .toString())
    }

    @Test
    @Order(4)
    fun testNameAndFields() {
        assertEquals(
            "SampleEntity{name=Sample,id=1}",
            EntityToStringBuilder(SampleEntity()).add("id", 1).add("name", "Sample").toString())

        assertEquals(
            "SampleEntity:SampleEntity{name=Sample,id=2}",
            EntityToStringBuilder(SampleEntity())
                .name("SampleEntity")
                .add("id", 2)
                .add("name", "Sample")
                .toString())
    }

    @Test
    @Order(5)
    fun testFields() {
        assertEquals(
            "SampleEntity{name=Sample,id=1}",
            EntityToStringBuilder(SampleEntity()).add("id", 1).add("name", "Sample").toString())

        assertEquals(
            "SampleEntity{name=Sample,id=2}",
            EntityToStringBuilder(SampleEntity()).add("id", 2).add("name", "Sample").toString())
    }

    @Test
    @Order(6)
    fun testFieldsWithNull() {
        assertEquals(
            "SampleEntity{name=null,id=1}",
            EntityToStringBuilder(SampleEntity()).add("id", 1).add("name", null).toString())

        assertEquals(
            "SampleEntity{name=null,id=2}",
            EntityToStringBuilder(SampleEntity()).add("id", 2).add("name", null).toString())
    }

    @Test
    @Order(7)
    fun testFieldsWithSnowflake() {
        assertEquals(
            "SampleSnowflakeEntity{name=Sample}(id=1,createTime=1420070400000,workerId=0,increment=1)",
            EntityToStringBuilder(SampleSnowflakeEntity(1)).add("name", "Sample").toString())

        assertEquals(
            "SampleSnowflakeEntity{name=Sample,age=18}(id=1,createTime=1420070400000,workerId=0,increment=1)",
            EntityToStringBuilder(SampleSnowflakeEntity(1))
                .add("name", "Sample")
                .add("age", 18)
                .toString())
    }
}
