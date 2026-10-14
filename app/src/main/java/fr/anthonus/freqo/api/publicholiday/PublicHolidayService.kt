package fr.anthonus.freqo.api.publicholiday

import retrofit2.http.GET
import retrofit2.http.Path

interface PublicHolidayService {
    @GET("{zone}/{year}.json")
    suspend fun getHolidaysByYear(
        @Path("zone") zone: String = "metropole",
        @Path("year") year: Int
    ): Map<String, String>
}