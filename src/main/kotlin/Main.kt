import com.jessecorbett.diskord.api.channel.*
import com.jessecorbett.diskord.bot.bot
import com.jessecorbett.diskord.bot.classicCommands
import com.jessecorbett.diskord.bot.interaction.interactions
import moscht.*
import moscht.rendering.SimpleDliRenderer
import java.io.File
import java.time.Clock
import java.time.LocalDateTime

/**
 * Reads in the bot token
 *
 * @throws RuntimeException when could not locate the file with the token
 */
private val BOT_TOKEN = try {
    File("bot-token.txt").bufferedReader().use {
        it.readText()
    }
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
            slashCommand("moscht", "Request StatuSCH.") {
                val args by stringParameter("argument", "Arguments.", optional = true)
                callback {
                    respond {
                        val renderer = SimpleDliRenderer()
                        val filter = when (args) {
                            "w" -> { m: Machine -> m.type == MachineType.WashingMachine }

                            "wa" -> { m: Machine ->
                                m.type == MachineType.WashingMachine && m.status == MachineStatus(MachineStatus.MachineStatusType.Available)
                            }

                            "d" -> { m: Machine -> m.type == MachineType.Dryer }

                            "da" -> { m: Machine ->
                                m.type == MachineType.Dryer && m.status == MachineStatus(MachineStatus.MachineStatusType.Available)
                            }

                            else -> { _: Machine -> true }
                        }
                        with(renderer) {
                            renderData(MosogepApiV1(), MosogepApiV2()) { filter(it) }
                            content = ""
                            embeds = mutableListOf(
                                Embed(
                                    title = "StatuSCH :sweat_drops:",
                                    description = getData(),
                                    timestamp = LocalDateTime.now(Clock.systemUTC()).toString(),
                                    color = 808080,
                                    url = "https://mosogep.sch.bme.hu",
                                )
                            )
                        }
                    }
                }
            }

        }

        // The old-fashioned way, it uses messages, such as .ping, for commands
        classicCommands(".") {
            command("ping") {
                it.reply("pong")
            }
        }
    }
}