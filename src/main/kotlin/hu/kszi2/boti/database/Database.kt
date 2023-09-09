package hu.kszi2.boti.database

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.StdOutSqlLogger
import org.jetbrains.exposed.sql.addLogger
import org.jetbrains.exposed.sql.kotlin.datetime.datetime
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.Clock
import java.time.LocalDateTime

object Reminders : IntIdTable() {
    val title = varchar("title", 50)
    val description = varchar("description", 255).nullable()
    val date = datetime("datetime").index().nullable()
    val location = varchar("location", 30).nullable()
}

object RepeatIntervals : IntIdTable() {
    val days = integer("days")
    val reminder = reference("reminder", Reminders)
}

object NotificationTimes : IntIdTable() {
    val minutes = integer("minutes")
    val reminder = reference("reminder", Reminders).nullable()
    val washingreminder = reference("washingreminder", WashingReminders).nullable()
}

object WashingReminders : IntIdTable() {
    val date = datetime("datetime").index()
    val machine = reference("machine", Machines)
}

object Machines : IntIdTable() {
    val type = varchar("type", 1).uniqueIndex("machine")
    val level = integer("level").uniqueIndex("machine")
    val description = varchar("description", 300).nullable()
}

class Reminder(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<Reminder>(Reminders)

    var title by Reminders.title
    var description by Reminders.description
    var date by Reminders.date
    var location by Reminders.location
}

class RepeatInterval(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<RepeatInterval>(RepeatIntervals)

    var days by RepeatIntervals.days
}

class NotificationTime(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<NotificationTime>(NotificationTimes)

    var minutes by NotificationTimes.minutes
}

class WashingReminder(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<WashingReminder>(WashingReminders)

    var date by WashingReminders.date
}

class Machine(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<Machine>(Machines)

    var type by Machines.type
    var level by Machines.level
    var description by Machines.description
}

fun initdb() {
    Database.connect("jdbc:sqlite:runtime/data.db", "org.sqlite.JDBC")
    transaction {
        addLogger(StdOutSqlLogger)

        SchemaUtils.create(
            Reminders,
            RepeatIntervals,
            NotificationTimes,
            WashingReminders,
            Machines
        )

        Reminder.new {
            title = "Test"
            description = "Testdesc"
            date = kotlinx.datetime.LocalDateTime(2023, 1, 1, 11, 20)
            location = "IB028"
        }


        println(Reminder.all().joinToString { it.title })
    }
}