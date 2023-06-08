package furhatos.app.sahra.util

import com.google.gson.JsonParser
import khttp.get

// Autoura API documentation : https://www.autoura.com/docs/api

fun AutouraApiGet(apiUrl: String): Any {

    val token = "7757a1969aaba8c3f8e717ffa462fe" // Autoura test key (public so OK to be in Github)
    val headers = mapOf("Authorization" to "Bearer $token")

    val response = get("https://api.autoura.com/api/$apiUrl", headers = headers)

    val jsonObject = JsonParser.parseString(response.text).asJsonObject

    // Sometimes the Autoura API responds with an array (e.g. search results), sometimes with an object (e.g. get stop). To enable the calling function to handle this correctly, returning the data to the calling function as string
    return jsonObject.get("response").toString();
}
