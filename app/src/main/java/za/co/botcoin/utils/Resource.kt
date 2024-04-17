package za.co.botcoin.utils

import za.co.botcoin.state.ServiceState

class Resource<T>(val serviceState: ServiceState, val data: T?, val message: String?) {
    companion object {
        fun <T> success(data: T?): Resource<T> {
            return Resource(ServiceState.Success, data, null)
        }

        fun <T> error(message: String?, data: T? = null): Resource<T> {
            return Resource(ServiceState.Error, data, message)
        }

        fun <T> loading(data: T? = null): Resource<T> {
            return Resource(ServiceState.Loading, data, null)
        }
    }
}