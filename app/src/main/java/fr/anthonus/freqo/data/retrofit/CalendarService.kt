package fr.anthonus.freqo.data.retrofit

import retrofit2.http.GET
import retrofit2.http.Path

interface CalendarService {
    @GET("{zone}/{year}.json")
    suspend fun getHolidaysByYear(
        @Path("zone") zone: String = "metropole",
        @Path("year") year: Int
    ): Map<String, String>
}