package hu.kszi2.boti

import com.jessecorbett.diskord.api.common.MessageComponent
import com.jessecorbett.diskord.api.interaction.InteractionTypeSerializer
import com.jessecorbett.diskord.bot.bot
import com.jessecorbett.diskord.bot.classicCommands
import com.jessecorbett.diskord.bot.events
import com.jessecorbett.diskord.bot.interaction.interactions
import hu.kszi2.boti.command.MoschtCommand
import hu.kszi2.boti.command.ReminderCommand
import hu.kszi2.boti.command.initSlashCommand
import hu.kszi2.boti.database.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import java.io.File

/**
 * Path to database
 */
val DBPATH = "runtime/data.db"

/**
 * Reads in the bot token
 *
 * @throws RuntimeException when could not locate the file with the token
 */
private val BOT_TOKEN = try {
    File("runtime/bot-token.txt").bufferedReader().use { it.readText() }.trim()
} catch (error: Exception) {
    throw RuntimeException(
        "Failed to load bot token. Message: ", error
    )
}

suspend fun main() {
    //init db
    dbInitialize()

    //test transaction
    dbTransaction {
        println(Reminder.all().map { it.repeatinterval.joinToString { it.toString() } })

        println("\n${Reminder.all().joinToString { it.toString() }}\n")
    }

    //creating the bot
    bot(BOT_TOKEN) {
        events {
            onInteractionCreate {
                if(it is MessageComponent){
                    it.data.customId..
                }
            }
        }
        // Modern interactions API for slash commands, user commands, etc
        interactions {
            initSlashCommand(MoschtCommand(), ReminderCommand())
        }

        // The old-fashioned way, it uses messages, such as .ping, for commands
        classicCommands(".") {
            command("ping") {
                it.reply("pong")
            }
        }
    }
}