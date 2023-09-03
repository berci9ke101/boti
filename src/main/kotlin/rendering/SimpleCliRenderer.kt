package hu.kszi2.boti.rendering

import hu.kszi2.boti.*
import java.lang.RuntimeException

class SimpleCliRenderer : MachineRenderer {
    override suspend fun renderData(vararg apis: MosogepAsyncApi) {
        val ex = RuntimeException("Cannot reach any API")
        for (api in apis) {
            try {
                return api.loadMachines().forEach(::renderMachine)
            } catch (timeout: MosogepAsyncApi.UnreachableApiError) {
                ex.addSuppressed(timeout)
            }
        }
        throw ex
    }

    private fun renderMachine(machine: Machine) {
        println("${machine.level}. szinti ${renderType(machine.type)}: ${renderStatus(machine.status)}")
    }

    private fun renderStatus(status: MachineStatus): String {
        val (stat, since) = status.effectiveStatus()
        return when (stat) {
            MachineStatus.MachineStatusType.Available -> "Szabad"
            MachineStatus.MachineStatusType.InUse -> "Használatban"
            MachineStatus.MachineStatusType.Unknown -> "Ismeretlen(${since} órája)"
        }
    }

    private fun renderType(type: MachineType): String {
        return when (type) {
            is MachineType.WashingMachine -> "Mosógép"
            is MachineType.Dryer -> "Szárítógép"
            is MachineType.Unknown -> "Ismeretlen(${type.shortName})"
        }
    }
}