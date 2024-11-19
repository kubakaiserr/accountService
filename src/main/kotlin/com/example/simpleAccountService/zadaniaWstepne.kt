package com.example.simpleAccountService

import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.UUID


class MainKtTest {

    class ColorGroups {
        val groupOne = setOf(Color.WHITE, Color.YELLOW, Color.ORANGE, Color.PINK)
        val groupTwo = setOf(Color.BLACK, Color.RED)
        val groupThree = setOf(Color.BLUE, Color.GREEN)
    }

    @Test
    fun `Excersise 1`() {
        // Given
        val colorGroups = ColorGroups()
        val handlerOne = HandlerOne()
        val handlerTwo = HandlerTwo()
        val handlerThree = HandlerThree()

        // create a solution that provides relevant handler for a passed color and applies "consume" operation over it.
        // E.g. something like handle(Color.BLACK) results in handlerOne.consume(Color.BLACK).


        assertEquals("OK",handle(Color.BLACK))
        assertEquals("ERROR",handlerOne.consume(Color.BLUE))

    }


    fun handle(color: Color): String {

        val colorGroups = ColorGroups()

        val handlerOne = HandlerOne()
        val handlerTwo = HandlerTwo()
        val handlerThree = HandlerThree()

        if (colorGroups.groupOne.contains(color)) {
            return handlerOne.consume(color)
        }
        else if (colorGroups.groupTwo.contains(color)) {
            return handlerTwo.consume(color)
        }
        else if (colorGroups.groupThree.contains(color)) {
            return handlerThree.consume(color)
        } else {
            return "INVALID COLOR"
        }
    }

    enum class Color {
        WHITE,
        BLACK,
        YELLOW,
        ORANGE,
        PINK,
        RED,
        BLUE,
        GREEN
    }

    class HandlerOne {
        val colorGroups = ColorGroups()
        fun consume(color: Color): String {
            // Should accept only colors from group one.
            if (colorGroups.groupOne.contains(color)) {
                return "OK"
            } else {
                return "ERROR"
            }
        }
    }

    class HandlerTwo {
        val colorGroups = ColorGroups()
        fun consume(color: Color): String {
            // Should accept only colors from group one.
            if (colorGroups.groupTwo.contains(color)) {
                return "OK"
            } else {
                return "ERROR"
            }
        }
    }

    class HandlerThree {
        val colorGroups = ColorGroups()
        fun consume(color: Color): String {
            // Should accept only colors from group one.
            if (colorGroups.groupThree.contains(color)) {
                return "OK"
            } else {
                return "ERROR"
            }
        }
    }


    // ---
    @Test
    fun `Excersise 2`() {
        // Given
        val listsOfSamples = listOf(
            listOf("A", "B", "C", "D"),
            listOf("A", "C"),
            listOf("C", "D", "E"),
            listOf("B", "D", "F"),
            listOf("F")
        )
        // and
        val relations =
            mapOf(
                1 to setOf("A", "C"),
                2 to setOf("A", "D"),
                3 to setOf("E", "F"),
                4 to setOf("B", "C", "D", "E")
            )

        // implement a method that creates a connection between entries from `listsOfSamples` and keys from
        // `relations` where at least one of the values present in `relations` value set is present in list from
        // `listsOfSamples`.
        // For example, a relation between key 1 and lists: ["A", "B", "C", "D"], ["A", "C"], ["C", "D", "E"] exists
        // because either "A" or "C" is found in each of them. Ensure that relations are ordered by a number of found
        // matches.

        val matches = mutableMapOf<Int, Int>()


        for (relation in relations) {
            var matchCount = 0
            for (listOfSamples in listsOfSamples) {
                if (listOfSamples.any { it in relation.value}) {
                    matchCount++
                }
            }
            matches[relation.key] = matchCount
        }
        val sortedMatches = matches.entries.sortedByDescending { it.value }

        for (entry in sortedMatches) {
            println("Relation ${entry.key} has ${entry.value} matches")
        }
    }

    // ---
    @Test
    fun `Excersise 3`() {
        // Given a sample representation of
        val existingEntries = listOf(
            scheduledEntry(
                UUID.fromString("ff371170-d75b-4353-a509-79f0e8231752"),
                ZonedDateTime.of(2024, 9, 30, 12, 15, 30, 0, ZoneId.of("UTC")),
                Type.OPERATION_A,
                Status.READY_FOR_PROCESSING,
                "sample content"
            )
        )

        // create a solution that completes in external service all READY_FOR_PROCESSING entries for a given operation
        // that are not preceded by FAILED entry and adds entry with relevant data to `existingEntries`.
    }

    class externalService {
        fun complete(id: String): Response {
            // Sample response
            return Response("1", ResponseStatus.SUCCESS, "{}")
        }
    }

    data class Response (
        val id: String,
        val operationStatus: ResponseStatus,
        val content: String
    )

    enum class ResponseStatus {
        SUCCESS,
        FAILURE
    }

    data class scheduledEntry (
        val correlationId: UUID,
        val createdAt: ZonedDateTime,
        val type: Type,
        val status: Status,
        val content: String
    )

    enum class Type {
        OPERATION_A,
        OPERATION_B,
        OPERATION_C
    }

    enum class Status {
        READY_FOR_PROCESSING,
        IN_PROGRESS,
        FAILED,
        UNKNOWN
    }

    // ---
    @Test
    fun `Excersise 4`() {
        // Write a simple REST application with endpoint that allows to update bank account balance. To simplify code,
        // for purposes of this exercise let's simulate persistence layer by some in-memory instance (e.g. Map).

    }
}
