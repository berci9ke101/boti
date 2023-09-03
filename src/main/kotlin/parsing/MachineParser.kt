package hu.kszi2.boti.parsing

import hu.kszi2.boti.Machine

interface MachineAsyncParser {
    suspend fun parse(payload: String): List<Machine>
}

interface MachineParser {
    fun parse(payload: String): List<Machine>
}
