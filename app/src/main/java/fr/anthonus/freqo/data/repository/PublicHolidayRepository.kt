package fr.anthonus.freqo.data.repository

import fr.anthonus.freqo.data.model.publicholiday.PublicHoliday
import fr.anthonus.freqo.data.datasource.publicholiday.NetworkDataSource
import fr.anthonus.freqo.data.datasource.publicholiday.Resource
import java.time.LocalDate

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