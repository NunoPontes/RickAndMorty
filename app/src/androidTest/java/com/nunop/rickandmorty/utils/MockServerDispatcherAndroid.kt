package com.nunop.rickandmorty.utils

import androidx.test.platform.app.InstrumentationRegistry
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.RecordedRequest
import java.io.InputStream
import java.nio.charset.StandardCharsets
import java.util.*

class MockServerDispatcherAndroid {

    /**
     * Gets text inside InputStream
     *
     * @param inputStream to be converted to String
     * @return string
     */
    private fun getStringFromJson(inputStream: InputStream): String? {
        var text: String?
        Scanner(inputStream, StandardCharsets.UTF_8.name()).use { scanner ->
            text = scanner.useDelimiter("\\A").next()
        }

        return text
    }

    /**
     * Gets text inside file
     *
     * @param id json file
     * @return text inside file
     */
    private fun getStringFile(id: Int): String? {
        return getStringFromJson(
            InstrumentationRegistry.getInstrumentation()
                .context
                .resources
                .openRawResource(
                    id
                )
        )
    }

    /**
     * Return ok response from mock server
     */
    inner class RequestDispatcher : Dispatcher() {
        override fun dispatch(request: RecordedRequest): MockResponse {
            if (request.path=="/character?page=1") {
                return MockResponse().setResponseCode(200)
                    .setBody(getStringFile(com.nunop.rickandmorty.test.R.raw.getcharacterspage1success)!!)
            } else if (request.path=="/character?page=2") {
                return MockResponse().setResponseCode(200)
                    .setBody(getStringFile(com.nunop.rickandmorty.test.R.raw
                        .getcharacterspage2success)!!)
            } else if (request.path=="/location?page=1") {
                return MockResponse().setResponseCode(200)
                    .setBody(getStringFile(com.nunop.rickandmorty.test.R.raw
                        .getlocationspage1success)!!)
            } else if (request.path=="/episode?page=1") {
                return MockResponse().setResponseCode(200)
                    .setBody(getStringFile(com.nunop.rickandmorty.test.R.raw
                        .getepisodespage1success)!!)
            }
            return MockResponse().setResponseCode(404)
        }
    }

    /**
     * Return error response from mock server
     */
    inner class ErrorRequestDispatcher : Dispatcher() {
        override fun dispatch(request: RecordedRequest): MockResponse {
            return MockResponse().setResponseCode(404)
        }
    }
}