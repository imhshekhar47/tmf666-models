package org.hshekhar.tmf666.models

import com.fasterxml.jackson.module.kotlin.readValue
import org.hshekhar.tmf666.util.ModelMapper
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.fail

/**
 * @created 3/12/2021'T'10:44 AM
 * @author Himanshu Shekhar (609080540)
 **/

internal class AccountTest {

    @Test
    fun `should deserialize`() {
        val json = """
            {
                "name": "sample"
            }
        """.trimIndent()

        val account = ModelMapper.readValue<Account>(json.toByteArray())

        assertEquals("sample", account.name)
    }

    @Test
    fun `should serialize`() {
        val account = Account(
            id = "10001",
            name = "sample",
            accountBalance = listOf(
                AccountBalance(
                    balanceType = "default",
                    amount = Money(unit = "INR", value = 0.0F),
                    validFor = TimePeriod()
                )
            )
        )
        try {
            ModelMapper.writeValueAsString(account)
        } catch (e: Exception) {
            fail("Failed to serialize")
        }
    }
}