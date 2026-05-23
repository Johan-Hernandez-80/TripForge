package com.example.tripforge.data

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL

// ── API response models ───────────────────────────────────────────────────────

private data class CountriesResponse(
    @SerializedName("error") val error: Boolean,
    @SerializedName("data")  val data: List<CountryEntry>
)

private data class CountryEntry(
    @SerializedName("country") val country: String,
    @SerializedName("cities")  val cities: List<String>
)

private data class CitiesResponse(
    @SerializedName("error") val error: Boolean,
    @SerializedName("data")  val data: List<String>
)

// ── LocationData ──────────────────────────────────────────────────────────────

object LocationData {

    private const val BASE_URL = "https://countriesnow.space/api/v0.1"
    private const val TIMEOUT_MS = 8_000
    private val gson = Gson()

    // ── Fallback hardcoded data ───────────────────────────────────────────────

    private val fallbackCountriesList: List<String> = listOf(
        "Argentina", "Australia", "Austria", "Belgium", "Bolivia", "Brazil",
        "Canada", "Chile", "China", "Colombia", "Costa Rica", "Croatia",
        "Czech Republic", "Denmark", "Ecuador", "Egypt", "Finland", "France",
        "Germany", "Greece", "Guatemala", "Hungary", "Iceland", "India",
        "Indonesia", "Ireland", "Italy", "Japan", "Jordan",
        "Kenya", "Malaysia", "Mexico", "Morocco", "Netherlands", "New Zealand",
        "Norway", "Panama", "Peru", "Philippines", "Poland", "Portugal",
        "Romania", "Russia", "Singapore", "South Africa", "South Korea", "Spain",
        "Sweden", "Switzerland", "Thailand", "Turkey", "Ukraine", "United Kingdom",
        "United States", "Uruguay", "Vietnam"
    ).sorted()

    private val fallbackCitiesMap: Map<String, List<String>> = mapOf(
        "Argentina"      to listOf("Buenos Aires", "Córdoba", "Rosario", "Mendoza", "Bariloche", "Salta", "Mar del Plata"),
        "Australia"      to listOf("Sydney", "Melbourne", "Brisbane", "Perth", "Adelaide", "Canberra", "Hobart", "Darwin"),
        "Austria"        to listOf("Vienna", "Graz", "Linz", "Salzburg", "Innsbruck"),
        "Belgium"        to listOf("Brussels", "Antwerp", "Ghent", "Bruges", "Liège"),
        "Bolivia"        to listOf("La Paz", "Santa Cruz de la Sierra", "Cochabamba", "Sucre", "Oruro"),
        "Brazil"         to listOf("São Paulo", "Rio de Janeiro", "Brasília", "Salvador", "Fortaleza", "Manaus", "Curitiba", "Florianópolis"),
        "Canada"         to listOf("Toronto", "Vancouver", "Montreal", "Calgary", "Ottawa", "Edmonton", "Quebec City", "Winnipeg"),
        "Chile"          to listOf("Santiago", "Valparaíso", "Concepción", "La Serena", "Antofagasta", "Puerto Montt", "Punta Arenas"),
        "China"          to listOf("Beijing", "Shanghai", "Guangzhou", "Shenzhen", "Chengdu", "Xi'an", "Hangzhou", "Chongqing"),
        "Colombia"       to listOf("Bogotá", "Medellín", "Cali", "Cartagena", "Barranquilla", "Santa Marta", "Pereira"),
        "Costa Rica"     to listOf("San José", "Liberia", "Jacó", "La Fortuna", "Puerto Viejo"),
        "Croatia"        to listOf("Zagreb", "Split", "Dubrovnik", "Rijeka", "Zadar"),
        "Czech Republic" to listOf("Prague", "Brno", "Ostrava", "Plzeň", "Liberec"),
        "Denmark"        to listOf("Copenhagen", "Aarhus", "Odense", "Aalborg", "Esbjerg"),
        "Ecuador"        to listOf("Quito", "Guayaquil", "Cuenca", "Manta", "Baños"),
        "Egypt"          to listOf("Cairo", "Alexandria", "Luxor", "Aswan", "Hurghada", "Sharm el-Sheikh"),
        "Finland"        to listOf("Helsinki", "Tampere", "Turku", "Oulu", "Rovaniemi"),
        "France"         to listOf("Paris", "Lyon", "Marseille", "Nice", "Bordeaux", "Toulouse", "Strasbourg", "Nantes"),
        "Germany"        to listOf("Berlin", "Munich", "Hamburg", "Frankfurt", "Cologne", "Stuttgart", "Dresden", "Düsseldorf"),
        "Greece"         to listOf("Athens", "Thessaloniki", "Heraklion", "Santorini", "Mykonos", "Rhodes", "Corfu"),
        "Guatemala"      to listOf("Guatemala City", "Antigua", "Quetzaltenango", "Cobán", "Flores"),
        "Hungary"        to listOf("Budapest", "Debrecen", "Miskolc", "Pécs", "Győr"),
        "Iceland"        to listOf("Reykjavik", "Akureyri", "Hafnarfjörður", "Keflavik"),
        "India"          to listOf("Mumbai", "Delhi", "Bengaluru", "Hyderabad", "Chennai", "Kolkata", "Jaipur", "Goa", "Agra"),
        "Indonesia"      to listOf("Jakarta", "Bali", "Surabaya", "Yogyakarta", "Bandung", "Lombok", "Medan"),
        "Ireland"        to listOf("Dublin", "Cork", "Galway", "Limerick", "Killarney"),
        "Italy"          to listOf("Rome", "Milan", "Florence", "Venice", "Naples", "Turin", "Bologna", "Amalfi", "Cinque Terre"),
        "Japan"          to listOf("Tokyo", "Osaka", "Kyoto", "Hiroshima", "Sapporo", "Fukuoka", "Nara", "Yokohama"),
        "Jordan"         to listOf("Amman", "Petra", "Aqaba", "Jerash", "Wadi Rum"),
        "Kenya"          to listOf("Nairobi", "Mombasa", "Kisumu", "Nakuru", "Amboseli"),
        "Malaysia"       to listOf("Kuala Lumpur", "Penang", "Johor Bahru", "Kota Kinabalu", "Malacca"),
        "Mexico"         to listOf("Mexico City", "Cancún", "Guadalajara", "Monterrey", "Oaxaca", "Puerto Vallarta", "Tulum", "Los Cabos"),
        "Morocco"        to listOf("Marrakech", "Casablanca", "Fez", "Rabat", "Chefchaouen", "Agadir"),
        "Netherlands"    to listOf("Amsterdam", "Rotterdam", "The Hague", "Utrecht", "Eindhoven"),
        "New Zealand"    to listOf("Auckland", "Wellington", "Christchurch", "Queenstown", "Dunedin"),
        "Norway"         to listOf("Oslo", "Bergen", "Trondheim", "Stavanger", "Tromsø"),
        "Panama"         to listOf("Panama City", "Bocas del Toro", "Boquete", "Colón"),
        "Peru"           to listOf("Lima", "Cusco", "Arequipa", "Machu Picchu", "Puno", "Iquitos"),
        "Philippines"    to listOf("Manila", "Cebu", "Davao", "Palawan", "Boracay", "Bohol"),
        "Poland"         to listOf("Warsaw", "Kraków", "Gdańsk", "Wrocław", "Poznań"),
        "Portugal"       to listOf("Lisbon", "Porto", "Faro", "Braga", "Sintra", "Funchal"),
        "Romania"        to listOf("Bucharest", "Cluj-Napoca", "Timișoara", "Brasov", "Sibiu"),
        "Russia"         to listOf("Moscow", "Saint Petersburg", "Novosibirsk", "Kazan", "Vladivostok"),
        "Singapore"      to listOf("Singapore"),
        "South Africa"   to listOf("Cape Town", "Johannesburg", "Durban", "Pretoria", "Stellenbosch"),
        "South Korea"    to listOf("Seoul", "Busan", "Incheon", "Jeju", "Gyeongju"),
        "Spain"          to listOf("Madrid", "Barcelona", "Seville", "Valencia", "Granada", "Bilbao", "San Sebastián", "Mallorca"),
        "Sweden"         to listOf("Stockholm", "Gothenburg", "Malmö", "Uppsala", "Kiruna"),
        "Switzerland"    to listOf("Zurich", "Geneva", "Bern", "Basel", "Lucerne", "Interlaken"),
        "Thailand"       to listOf("Bangkok", "Chiang Mai", "Phuket", "Koh Samui", "Pattaya", "Ayutthaya"),
        "Turkey"         to listOf("Istanbul", "Ankara", "Antalya", "Cappadocia", "Izmir", "Bodrum"),
        "Ukraine"        to listOf("Kyiv", "Lviv", "Odessa", "Kharkiv"),
        "United Kingdom" to listOf("London", "Edinburgh", "Manchester", "Liverpool", "Bristol", "Oxford", "Cambridge", "Bath"),
        "United States"  to listOf("New York", "Los Angeles", "Chicago", "Miami", "Las Vegas", "San Francisco", "New Orleans", "Honolulu", "Seattle", "Boston"),
        "Uruguay"        to listOf("Montevideo", "Punta del Este", "Colonia del Sacramento", "Salto"),
        "Vietnam"        to listOf("Ho Chi Minh City", "Hanoi", "Da Nang", "Hoi An", "Hue", "Halong Bay", "Nha Trang")
    )

    // ── Public API ────────────────────────────────────────────────────────────

    /**
     * Returns the sorted list of countries.
     * Tries CountriesNow first; falls back to [fallbackCountriesList] on any error.
     */
    suspend fun fetchCountries(): List<String> = withContext(Dispatchers.IO) {
        try {
            val conn = openGet("$BASE_URL/countries")
            val body = conn.inputStream.bufferedReader().readText()
            conn.disconnect()

            val response = gson.fromJson(body, CountriesResponse::class.java)
            if (!response.error && response.data.isNotEmpty()) {
                response.data
                    .map { it.country }
                    .filter { it.isNotBlank() }
                    .sorted()
            } else {
                fallbackCountriesList
            }
        } catch (e: Exception) {
            fallbackCountriesList
        }
    }

    /**
     * Returns the cities for [country].
     * Tries CountriesNow first; falls back to [fallbackCitiesMap] on any error.
     */
    suspend fun fetchCitiesForCountry(country: String): List<String> = withContext(Dispatchers.IO) {
        try {
            val conn = openPost("$BASE_URL/countries/cities")
            val payload = """{"country":"$country"}"""
            OutputStreamWriter(conn.outputStream).use { it.write(payload) }

            val body = conn.inputStream.bufferedReader().readText()
            conn.disconnect()

            val response = gson.fromJson(body, CitiesResponse::class.java)
            if (!response.error && response.data.isNotEmpty()) {
                response.data.filter { it.isNotBlank() }.sorted()
            } else {
                fallbackCitiesMap[country] ?: emptyList()
            }
        } catch (e: Exception) {
            fallbackCitiesMap[country] ?: emptyList()
        }
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private fun openGet(url: String): HttpURLConnection =
        (URL(url).openConnection() as HttpURLConnection).apply {
            requestMethod = "GET"
            connectTimeout = TIMEOUT_MS
            readTimeout = TIMEOUT_MS
            setRequestProperty("Content-Type", "application/json")
        }

    private fun openPost(url: String): HttpURLConnection =
        (URL(url).openConnection() as HttpURLConnection).apply {
            requestMethod = "POST"
            connectTimeout = TIMEOUT_MS
            readTimeout = TIMEOUT_MS
            setRequestProperty("Content-Type", "application/json")
            doOutput = true
        }
}