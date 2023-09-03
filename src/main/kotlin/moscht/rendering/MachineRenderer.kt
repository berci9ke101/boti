package hu.kszi2.boti.rendering

import moscht.Machine
import moscht.MosogepAsyncApi

interface MachineRenderer {
    suspend fun renderData(vararg apis: MosogepAsyncApi, filter: (m: Machine) -> Boolean)
}
