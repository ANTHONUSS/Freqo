package fr.anthonus.freqo.repository

import fr.anthonus.freqo.data.publicholiday.PublicHoliday
import fr.anthonus.freqo.datasource.publicholiday.NetworkDataSource
import fr.anthonus.freqo.datasource.publicholiday.Resource
import java.time.LocalDate
import kotlin.coroutines.cancellation.CancellationException

object PublicHolidayRepository {
    private val publicHolidayService = NetworkDataSource.publicHolidayService

    suspend fun getPublicHolidays( zone: String, year: Int): Resource<List<PublicHoliday>> {
        return try {
            val rawMap: Map<String, String> = publicHolidayService.getHolidaysByYear(zone = zone, year = year)

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