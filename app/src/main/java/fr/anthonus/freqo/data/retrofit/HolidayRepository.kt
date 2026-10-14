package fr.anthonus.freqo.data.retrofit

import fr.anthonus.freqo.data.PublicHoliday
import java.time.LocalDate

class HolidayRepository {
    private val service = NetworkDataSource.calendarService

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