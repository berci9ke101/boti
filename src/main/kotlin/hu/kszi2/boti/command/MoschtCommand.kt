package hu.kszi2.boti.command

import com.jessecorbett.diskord.api.channel.Embed
import com.jessecorbett.diskord.bot.interaction.InteractionBuilder
import hu.kszi2.moscht.*
import hu.kszi2.moscht.rendering.SimpleDliRenderer
import java.time.Clock
import java.time.LocalDateTime

class MoschtCommand : BotSlashCommand() {
    override fun initSlashCommand(interactionBuilder: InteractionBuilder) {
        interactionBuilder.slashCommand("moscht", "Request StatuSCH.") {
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

}