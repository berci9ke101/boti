import com.jessecorbett.diskord.bot.bot
import com.jessecorbett.diskord.bot.classicCommands
import moscht.MosogepApiV1
import moscht.MosogepApiV2
import moscht.rendering.SimpleDliRenderer

/**
 * Reads in the bot token
 *
 * @throws RuntimeException when could not locate the file with the token
 */
private val BOT_TOKEN = try {
    ClassLoader.getSystemResource("bot-token.txt").readText().trim()
} catch (error: Exception) {
    throw RuntimeException(
        "Failed to load bot token. Message: ", error
    )
}

suspend fun main() {
    //creating the bot
    bot(BOT_TOKEN) {
//        // Modern interactions API for slash commands, user commands, etc
//        interactions {
//            slashCommand("echo", "Makes the bot say something", permissions = Permissions.of(Permission.SEND_MESSAGES)) {
//                val message by stringParameter("message", "The message", optional = true)
//                callback {
//                    respond {
//                        content = if (message != null) {
//                            message
//                        } else {
//                            "The message was null, because it is optional"
//                        }
//                    }
//                }
//            }
//
//            commandGroup("emoji", "Send an emoji to the server") {
//                subgroup("smile", "Smile emoji") {
//                    slashCommand("slight", "A slight smile emoji") {
//                        callback {
//                            respond {
//                                content = "🙂"
//                            }
//                        }
//                    }
//                }
//
//                slashCommand("shh", "The shh emoji") {
//                    val secret by stringParameter("secret", "Send the emoji secretly")
//                    callback {
//                        respond {
//                            content = "🤫"
//                            if (secret != null) {
//                                ephemeral
//                            }
//                        }
//                    }
//                }
//            }
//        }

        // The old-fashioned way, it uses messages, such as .ping, for commands
        classicCommands(".") {

            command("ping") {
                it.reply("pong")
            }

            command("wash") {
                val renderer = SimpleDliRenderer()
                renderer.renderData(MosogepApiV1(), MosogepApiV2())
                it.reply(renderer.getData())
            }
        }
    }
}