package za.co.botcoin.state

sealed class ServiceState {
    data object Success : ServiceState()
    data object Error : ServiceState()
    data object Loading : ServiceState()
}