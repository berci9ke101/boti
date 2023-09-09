package hu.kszi2.boti

import com.jessecorbett.diskord.bot.bot
import com.jessecorbett.diskord.bot.classicCommands
import com.jessecorbett.diskord.bot.interaction.interactions
import hu.kszi2.boti.command.MoschtCommand
import hu.kszi2.boti.command.initSlashCommand
import hu.kszi2.boti.database.Reminder
import hu.kszi2.boti.database.initdb
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.transaction
import java.io.File

/**
 * Reads in the bot token
 *
 * @throws RuntimeException when could not locate the file with the token
 */
private val BOT_TOKEN = try {
    File("runtime/bot-token.txt").bufferedReader().use {
        it.readText()
    }.trim()
} catch (error: Exception) {
    throw RuntimeException(
        "Failed to load bot token. Message: ", error
    )
}

suspend fun main() {
    initdb()
    Database.connect("jdbc:sqlite:runtime/data.db", "org.sqlite.JDBC")
    transaction { println(Reminder.all().joinToString { it.title }) }

    //creating the bot
    bot(BOT_TOKEN) {
        // Modern interactions API for slash commands, user commands, etc
        interactions {
            initSlashCommand(MoschtCommand())
        }

        // The old-fashioned way, it uses messages, such as .ping, for commands
        classicCommands(".") {
            command("ping") {
                it.reply("pong")
            }
        }
    }
}