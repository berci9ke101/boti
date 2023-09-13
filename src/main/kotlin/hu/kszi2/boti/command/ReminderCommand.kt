package hu.kszi2.boti.command

import com.jessecorbett.diskord.api.channel.*
import com.jessecorbett.diskord.api.common.*
import com.jessecorbett.diskord.bot.interaction.InteractionBuilder
import hu.kszi2.boti.date.*

class ReminderCommand : BotSlashCommand() {
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
                    parse(date)
                } catch (e: Exception) {
                    respond {
                        content = "**Invalid datetime syntax. (Use yyyy-MM-dd HH:mm)**"
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

                    //accept/decline buttons
                    components = mutableListOf(
                        ActionRow(
                            mutableListOf(
                                Button(
                                    customId = "acceptreminder",
                                    label = "Yes",
                                    style = ButtonStyle.Success
                                ),
                                Button(
                                    customId = "declinereminder",
                                    label = "No",
                                    style = ButtonStyle.Danger
                                )
                            )
                        )
                    )

                    //preview card
                    embeds =
                        mutableListOf(
                            Embed(
                                title = title + "\t" + (location ?: ""),
                                description = description,
                                color = 5763719,
                                fields = mutableListOf(
                                    EmbedField(
                                        parseIntervals(notificationtime, repeatinterval),
                                        "",
                                        true
                                    )
                                ),
                                footer = EmbedFooter(
                                    "${
                                        datetime?.dayOfMonth.toString().padStart(2, '0')
                                    }/${
                                        datetime?.monthValue.toString().padStart(2, '0')
                                    }/${datetime?.year} ${
                                        datetime?.hour.toString().padStart(2, '0')
                                    }:${datetime?.minute.toString().padStart(2, '0')}"
                                )
                            )
                        )
                }
            }
        }
    }

}