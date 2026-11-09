package fr.anthonus.freqo.data.repository

import fr.anthonus.freqo.data.datasource.publicholiday.PublicHolidayDataSource
import fr.anthonus.freqo.data.model.PublicHoliday
import java.time.LocalDate

class PublicHolidayRepository {
    private val publicHolidayService = PublicHolidayDataSource.publicHolidayService

    suspend fun getPublicHolidays( zone: String, year: Int): Result<List<PublicHoliday>> {
        return try {
            val rawMap: Map<String, String> = publicHolidayService.getHolidaysByYear(zone = zone, year = year)

            val holidaysList: List<PublicHoliday> = rawMap.map { (dateStr, holidayName) ->
                PublicHoliday(
                    date = LocalDate.parse(dateStr),
                    name = holidayName
                )
            }

            Result.success(holidaysList)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}