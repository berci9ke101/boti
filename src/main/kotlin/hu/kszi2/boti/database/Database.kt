package hu.kszi2.boti.database

import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.kotlin.datetime.datetime
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.transactions.transactionManager
import java.io.File
import hu.kszi2.boti.DBPATH

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
    val type = varchar("type", 1)
    val level = integer("level")
    val description = text("description").nullable()

    init {
        uniqueIndex(type, level)
    }
}

class Reminder(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<Reminder>(Reminders)

    var title by Reminders.title
    var description by Reminders.description
    var date by Reminders.date
    var location by Reminders.location
    val repeatinterval by RepeatInterval referrersOn RepeatIntervals.reminder
    val notificationtime by NotificationTime optionalReferrersOn NotificationTimes.reminder

    override fun toString(): String {
        return "$title \t $description \t $date \t $location"
    }
}

class RepeatInterval(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<RepeatInterval>(RepeatIntervals)

    var days by RepeatIntervals.days
    var reminder by Reminder referencedOn RepeatIntervals.reminder

    override fun toString(): String {
        return "$days"
    }
}

class NotificationTime(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<NotificationTime>(NotificationTimes)

    var minutes by NotificationTimes.minutes
    var reminder by Reminder optionalReferencedOn NotificationTimes.reminder
    var washingReminder by WashingReminder optionalReferencedOn NotificationTimes.washingreminder
}

class WashingReminder(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<WashingReminder>(WashingReminders)

    var date by WashingReminders.date
    var machine by Machine referencedOn WashingReminders.machine
}

class Machine(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<Machine>(Machines)

    var type by Machines.type
    var level by Machines.level
    var description by Machines.description
    var washingreminder by WashingReminder referencedOn WashingReminders.machine
}

fun <T> dbTransaction(db: Database? = null, statement: Transaction.() -> T): T {
    Database.connect("jdbc:sqlite:$DBPATH", "org.sqlite.JDBC")
    return transaction(
        db.transactionManager.defaultIsolationLevel,
        db.transactionManager.defaultReadOnly,
        db,
        statement
    )
}

fun dbInitialize() {
    if (File(DBPATH).exists())
        return

    Database.connect("jdbc:sqlite:$DBPATH", "org.sqlite.JDBC")
    transaction {
        addLogger(Slf4jSqlDebugLogger)

        SchemaUtils.create(
            Reminders,
            RepeatIntervals,
            NotificationTimes,
            WashingReminders,
            Machines
        )

        val testrem = Reminder.new {
            title = "Test"
            description = "Testdesc"
            date = Clock.System.now().toLocalDateTime(TimeZone.of("Europe/Budapest"))
            location = "IB028"
        }

        RepeatInterval.new {
            days = 1
            reminder = testrem
        }
    }
}