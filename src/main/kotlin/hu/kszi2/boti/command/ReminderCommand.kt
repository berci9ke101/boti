package hu.kszi2.boti.command

import com.jessecorbett.diskord.api.channel.*
import com.jessecorbett.diskord.bot.interaction.InteractionBuilder
import java.time.Clock
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class ReminderCommand : BotSlashCommand() {

    private fun parseTime(datestring: String?): LocalDateTime {
        val datetime = try {
            datestring.let { LocalDateTime.parse(datestring, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")) }
        } catch (e: Exception) {
            throw e
        }
        return datetime
    }

    private fun parseIntervals(notificationtime: String?, repeatinterval: String?): String {
        val noti = if (notificationtime != null) {
            "Notification time : $notificationtime minute(s) before event"
        } else {
            null
        }

        val repi = if (repeatinterval != null) {
            "Repeat interval : $repeatinterval day(s)"
        } else {
            null
        }

        return "${noti ?: ""}\n${repi ?: ""}"
    }

    override fun initSlashCommand(interactionBuilder: InteractionBuilder) {
        interactionBuilder.slashCommand("remind", "Create a reminder.") {
            //arguments
            val title by stringParameter("title", "Title", optional = false)
            val date by stringParameter("datetime", "Datetime", optional = false)
            val location by stringParameter("location", "Location", optional = true)
            val description by stringParameter("description", "Description", optional = true)
            val notificationtime by stringParameter("notificationtime", "Notification time in minutes", optional = true)
            val repeatinterval by stringParameter("repeatinterval", "Repeat interval in days", optional = true)

            callback {
                //parse datetime
                val datetime = try {
                    parseTime(date)
                } catch (e: Exception) {
                    respond {
                        content = "**Invalid datetime syntax. Use YYYY-MM-DD HH:MM**"
                        ephemeral
                    }
                    //ABORT
                    return@callback
                }

                //successes parsing
                respond {
                    ephemeral
                    //confirmation
                    content = "**Is this correct?**\n"
                    embeds =
                        mutableListOf(
                            //preview card
                            Embed(
                                title = title + "\t" + (location ?: ""),
                                description = description + datetime.toString(),
                                color = 5763719,
                                fields = mutableListOf(EmbedField(parseIntervals(notificationtime, repeatinterval), "", true)),
                                timestamp = datetime.toString()
                            )
                        )
                }
            }
        }
    }

}