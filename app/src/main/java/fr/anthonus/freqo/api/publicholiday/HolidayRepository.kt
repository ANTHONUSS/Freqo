package fr.anthonus.freqo.api.publicholiday

import fr.anthonus.freqo.data.publicholiday.PublicHoliday
import java.time.LocalDate

class HolidayRepository {
    private val service = NetworkDataSource.publicHolidayService

    suspend fun fetchHolidays(year: Int, zone: String = "metropole"): Resource<List<PublicHoliday>> {
        return try {
            val rawMap: Map<String, String> = service.getHolidaysByYear(zone = zone, year = year)

            val holidaysList: List<PublicHoliday> = rawMap.map { (dateStr, holidayName) ->
                PublicHoliday(
                    date = LocalDate.parse(dateStr),
                    name = holidayName
                )
            }

            Resource.Success(holidaysList)
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: "Erreur réseau inconnue")
        }
    }
}