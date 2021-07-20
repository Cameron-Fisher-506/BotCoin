package za.co.ticker.model.service

import retrofit2.Response
import za.co.ticker.utils.Resource
import java.lang.Exception

abstract class BaseService {
    protected suspend fun <T> getResource(call: suspend () -> Response<T>): Resource<T> {
        try {
            val response = call.invoke()
            if (response.isSuccessful && response.body() != null) {
                return Resource.success(response.body())
            }
            return Resource.error("Error code: ${response.code()}")
        } catch (e: Exception) {
            return Resource.error(e.message ?: e.toString())
        }
    }
}