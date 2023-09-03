package hu.kszi2.boti.rendering

import hu.kszi2.boti.MosogepAsyncApi

interface MachineRenderer {
    suspend fun renderData(vararg apis: MosogepAsyncApi)
}
