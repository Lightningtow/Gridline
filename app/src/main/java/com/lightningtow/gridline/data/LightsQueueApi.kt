//package com.lightningtow.gridline.data
//
//import com.adamratzman.spotify.SpotifyException
//import com.adamratzman.spotify.http.CacheState
//import com.adamratzman.spotify.http.HttpHeader
//import com.adamratzman.spotify.http.HttpRequestMethod
//import com.adamratzman.spotify.http.HttpResponse
//import com.adamratzman.spotify.http.SpotifyEndpoint
//import com.adamratzman.spotify.http.SpotifyRequest
//import com.adamratzman.spotify.models.ErrorObject
//import com.adamratzman.spotify.models.ErrorResponse
//import com.adamratzman.spotify.models.Playable
//import kotlinx.coroutines.withTimeout
//import kotlinx.serialization.SerialName
//import kotlinx.serialization.Serializable
//
//
//@Serializable
//public data class CurrentUserQueue(
//
//    @SerialName("currently_playing") val currentlyPlaying: Playable? = null,
//    @SerialName("queue") val queue: List<Playable>
//)
//public class LightsQueueApi(api: GenericSpotifyApi) : SpotifyEndpoint(api) {
//    internal class EndpointBuilder(private val path: String, api: GenericSpotifyApi) {
//        val base = api.spotifyApiOptions.proxyBaseUrl ?: api.spotifyApiBase
//        private val builder = StringBuilder(base + path)
//
//        fun with(key: String, value: Any?): EndpointBuilder {
//            if (value != null && (value !is String || value.isNotEmpty())) {
//                if (builder.toString() == base + path) {
//                    builder.append("?")
//                } else {
//                    builder.append("&")
//                }
//                builder.append(key).append("=").append(value.toString())
//            }
//            return this
//        }
//
//        override fun toString() = builder.toString()
//    }
//    internal fun endpointBuilder(path: String) = EndpointBuilder(path, api)
//    internal open suspend fun get(url: String): String {
//        return execute<String>(url)
//    }
//    @Suppress("UNCHECKED_CAST")
//    internal open suspend fun <ReturnType : String?> execute(
//        url: String,
//        body: String? = null,
//        method: HttpRequestMethod = HttpRequestMethod.GET,
//        retry202: Boolean = true,
//        contentType: String? = null,
//        attemptedRefresh: Boolean = false,
//        retryOnNull: Boolean = true
//    ): ReturnType {
//        if (api.token.shouldRefresh()) {
//            if (!api.spotifyApiOptions.automaticRefresh) {
//                throw SpotifyException.ReAuthenticationNeededException(message = "The access token has expired.")
//            } else {
//                api.refreshToken()
//            }
//        }
//
//        val spotifyRequest = SpotifyRequest(url, method, body, api)
//        val cacheState = if (api.useCache) cache[spotifyRequest] else null
//
//        if (cacheState?.isStillValid() == true) {
//            return cacheState.data as ReturnType
//        } else if (cacheState?.let { it.eTag == null } == true) {
//            cache -= spotifyRequest
//        }
//
//        try {
//            return withTimeout(api.spotifyApiOptions.requestTimeoutMillis ?: (100 * 1000L)) {
//                try {
//                    val document = createConnection(url, body, method, contentType).execute(
//                        additionalHeaders = cacheState?.eTag?.let {
//                            listOf(HttpHeader("If-None-Match", it))
//                        },
//                        retryIfInternalServerErrorLeft = api.spotifyApiOptions.retryOnInternalServerErrorTimes
//                    )
//
//                    handleResponse(document, cacheState, spotifyRequest, retry202) ?: run {
//                        if (retryOnNull) {
//                            execute<ReturnType>(url, body, method, false, contentType, retryOnNull)
//                        } else {
//                            null
//                        }
//                    }
//                } catch (e: SpotifyException.BadRequestException) {
//                    if (e.statusCode == 401 && !attemptedRefresh) {
//                        api.refreshToken()
//
//                        execute<ReturnType>(
//                            url,
//                            body,
//                            method,
//                            retry202,
//                            contentType,
//                            true,
//                            retryOnNull
//                        )
//                    } else {
//                        throw e
//                    }
//                }
//            } as ReturnType
//        } catch (e: CancellationException) {
//            throw TimeoutException(
//                e.message
//                    ?: "The request $spotifyRequest timed out after (${api.spotifyApiOptions.requestTimeoutMillis ?: (100_000)}ms.",
//                e
//            )
//        }
//    }
//
//    private fun handleResponse(
//        document: HttpResponse,
//        cacheState: CacheState?,
//        spotifyRequest: SpotifyRequest,
//        retry202: Boolean
//    ): String? {
//        val statusCode = document.responseCode
//
//        if (statusCode == HttpStatusCode.NotModified.value) {
//            requireNotNull(cacheState?.eTag) { "304 status only allowed on Etag-able endpoints" }
//            return cacheState?.data
//        } else if (statusCode == HttpStatusCode.NoContent.value) {
//            return null
//        }
//
//        val responseBody = document.body
//
//        document.headers.find { it.key.equals("Cache-Control", true) }?.also { cacheControlHeader ->
//            if (api.useCache) {
//                cache[spotifyRequest] = (
//                        cacheState ?: CacheState(
//                            responseBody,
//                            document.headers
//                                .find { it.key.equals("ETag", true) }?.value
//                        )
//                        ).update(cacheControlHeader.value)
//            }
//        }
//
//        if (document.responseCode !in 200..399 /* Check if status is not 2xx or 3xx */) {
//            val response = try {
//                document.body.toObject(ErrorResponse.serializer(), api, api.spotifyApiOptions.json)
//            } catch (e: Exception) {
//                ErrorResponse(ErrorObject(400, "malformed request sent"), e)
//            }
//            throw SpotifyException.BadRequestException(response.error)
//        } else if (document.responseCode == 202 && retry202) return null
//        return responseBody
//    }
//    public suspend fun getUserQueue(): CurrentUserQueue {
//        return get(
//            endpointBuilder("/me/player/queue").toString()
//        ).toObject(CurrentUserQueue.serializer(), api = API_State.api, json = json)
//    }
//}