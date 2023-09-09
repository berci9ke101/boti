package hu.kszi2.boti

import com.jessecorbett.diskord.bot.interaction.*

abstract class BotSlashCommand(
) {
    abstract fun initSlashCommand(interactionBuilder: InteractionBuilder)
}