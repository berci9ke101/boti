package hu.kszi2.boti.interaction

import com.jessecorbett.diskord.api.channel.ChannelClient
import com.jessecorbett.diskord.api.interaction.MessageComponent
import com.jessecorbett.diskord.bot.BotBase
import com.jessecorbett.diskord.bot.EventDispatcherWithContext
import com.jessecorbett.diskord.util.sendMessage
import kotlinx.coroutines.channels.Channel

class ReminderInteraction : BotInteraction() {
    override fun initBotInteraction(base: BotBase, eventDispatcherWithContext: EventDispatcherWithContext) {
        eventDispatcherWithContext.onInteractionCreate {
            if (it is MessageComponent) {
                //if it is an accept button
                if (it.data.customId.startsWith("acceptreminder")) {
                    //REPLY HERE
                }

                //if it is a decline button
                if (it.data.customId == "declinereminder") {
                    //REPLY HERE
                }
            }
        }
    }
}