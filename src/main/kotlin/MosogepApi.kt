package hu.kszi2.boti

interface MosogepAsyncApi {
    suspend fun loadMachines(): List<Machine>
    class UnreachableApiError(val name: String) : Throwable()
}
