package moscht

interface MosogepAsyncApi {
    suspend fun loadMachines(): List<Machine>
    class UnreachableApiError(val name: String) : Throwable()
}
