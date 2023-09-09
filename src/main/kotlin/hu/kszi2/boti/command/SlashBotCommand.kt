package hu.kszi2.boti.command

import com.jessecorbett.diskord.bot.interaction.*

fun InteractionBuilder.initSlashCommand(vararg botSlashCommands: BotSlashCommand) {
    botSlashCommands.forEach { it.initSlashCommand(this@initSlashCommand) }
}

abstract class BotSlashCommand(
) {
    abstract fun initSlashCommand(interactionBuilder: InteractionBuilder)
}