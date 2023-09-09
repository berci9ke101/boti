package hu.kszi2.boti

import com.jessecorbett.diskord.bot.bot
import com.jessecorbett.diskord.bot.classicCommands
import com.jessecorbett.diskord.bot.interaction.interactions
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