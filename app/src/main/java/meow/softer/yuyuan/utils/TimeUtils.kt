package meow.softer.yuyuan.utils

import java.time.LocalDate
import java.time.ZoneId
import java.time.ZonedDateTime

fun ZonedDateTime.isToday(today: LocalDate = LocalDate.now()): Boolean =
    this.withZoneSameInstant(ZoneId.systemDefault()).toLocalDate() == today