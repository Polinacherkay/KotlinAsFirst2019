@file:Suppress("UNUSED_PARAMETER")

package lesson12.task1

import java.lang.IllegalArgumentException

/**
 * Класс "расписание поездов".
 *
 * Общая сложность задания -- средняя.
 * Объект класса хранит расписание поездов для определённой станции отправления.
 * Для каждого поезда хранится конечная станция и список промежуточных.
 * Поддерживаемые методы:
 * добавить новый поезд, удалить поезд,
 * добавить / удалить промежуточную станцию существующему поезду,
 * поиск поездов по времени.
 *
 * В конструктор передаётся название станции отправления для данного расписания.
 */
class TrainTimeTable(val baseStationName: String) {
    private val trains = mutableListOf<Train>()
    /**
     * Добавить новый поезд.
     *
     * Если поезд с таким именем уже есть, следует вернуть false и ничего не изменять в таблице
     *
     * @param train название поезда
     * @param depart время отправления с baseStationName
     * @param destination конечная станция
     * @return true, если поезд успешно добавлен, false, если такой поезд уже есть
     */
    fun addTrain(train: String, depart: Time, destination: Stop): Boolean {
        for (tt in trains) {
            if (tt.name == train) return false
        }
        trains.add(Train(train, Stop(baseStationName, depart), destination))
        return true
    }

    /**
     * Удалить существующий поезд.
     *
     * Если поезда с таким именем нет, следует вернуть false и ничего не изменять в таблице
     *
     * @param train название поезда
     * @return true, если поезд успешно удалён, false, если такой поезд не существует
     */
    fun removeTrain(train: String): Boolean {
        for (tt in trains) {
            if (tt.name == train) {
                trains.remove(tt)
                return true
            }
        }
        return false
    }

    /**
     * Добавить/изменить начальную, промежуточную или конечную остановку поезду.
     *
     * Если у поезда ещё нет остановки с названием stop, добавить её и вернуть true.
     * Если stop.name совпадает с baseStationName, изменить время отправления с этой станции и вернуть false.
     * Если stop совпадает с destination данного поезда, изменить время прибытия на неё и вернуть false.
     * Если stop совпадает с одной из промежуточных остановок, изменить время прибытия на неё и вернуть false.
     *
     * Функция должна сохранять инвариант: время прибытия на любую из промежуточных станций
     * должно находиться в интервале между временем отправления с baseStation и временем прибытия в destination,
     * иначе следует бросить исключение IllegalArgumentException.
     * Также, время прибытия на любую из промежуточных станций не должно совпадать с временем прибытия на другую
     * станцию или с временем отправления с baseStation, иначе бросить то же исключение.
     *
     * @param train название поезда
     * @param stop начальная, промежуточная или конечная станция
     * @return true, если поезду была добавлена новая остановка, false, если было изменено время остановки на старой
     */
    fun addStop(train: String, stop: Stop): Boolean  {
        for ((j, tt) in trains.withIndex()) {
            if (tt.name == train) {
                val stops = tt.stops
                val lastIndex = stops.lastIndex
                if (stops[0].time > stop.time && stops[0].name != stop.name ||
                    stops.last().time < stop.time && stops.last().name != stop.name)
                    throw IllegalArgumentException()
                for ((i, st) in stops.withIndex()) {
                    if (st.name == stop.name) {
                        if (i > 0 && stops[i - 1].time > stop.time || i < lastIndex && stops[i + 1].time < stop.time)
                            throw IllegalArgumentException()
                        trains[j].stops[i] = stop
                        return false
                    }
                }
                for ((i, st) in stops.withIndex()) {
                    if (stop.time < st.time) {
                        trains[j].stops.add(i, stop)
                        return true
                    }
                }
            }
        }
        return false
    }

    /**
     * Удалить одну из промежуточных остановок.
     *
     * Если stopName совпадает с именем одной из промежуточных остановок, удалить её и вернуть true.
     * Если у поезда нет такой остановки, или stopName совпадает с начальной или конечной остановкой, вернуть false.
     *
     * @param train название поезда
     * @param stopName название промежуточной остановки
     * @return true, если удаление успешно
     */
    fun removeStop(train: String, stopName: String): Boolean {
        for (tt in trains) {
            if (train == tt.name) {
                val l = tt.stops.lastIndex
                if (stopName == tt.stops[0].name || stopName == tt.stops[l].name) return false
                for (i in 1 until l) {
                    if (stopName == tt.stops[i].name) {
                        tt.stops.removeAt(i)
                        return true
                    }
                }
            }
        }
        return false
    }

    /**
     * Вернуть список всех поездов, упорядоченный по времени отправления с baseStationName
     */
    fun trains(): List<Train> {
      return trains.sortedBy { it.stops.first().time.hour * 60 + it.stops.first().time.minute }
    }

    /**
     * Вернуть список всех поездов, отправляющихся не ранее currentTime
     * и имеющих остановку (начальную, промежуточную или конечную) на станции destinationName.
     * Список должен быть упорядочен по времени прибытия на станцию destinationName
     */
    fun trains(currentTime: Time, destinationName: String): List<Train> {
        val list = mutableListOf<Train>()
        for (tt in trains) {
            if (tt.stops[0].time > currentTime) {
                for (i in tt.stops.indices) {
                    if (tt.stops[i].name != destinationName) continue
                    if (tt.stops[i].name == destinationName) {
                        list.add(tt)
                    }
                }
            }
        }
        val finalList = mutableListOf<Train>()
        val size = list.size
        while (size != finalList.size) {
            var minimalTime = 721
            var index = 0
            for ((i,tt) in list.withIndex()) {
                for (st in tt.stops) {
                    if (st.name == destinationName && (st.time.hour * 60 + st.time.minute) < minimalTime) {
                        minimalTime = st.time.hour * 60 + st.time.minute
                        index = i
                    }
                }
            }
            finalList.add(list[index])
            list.removeAt(index)
            index = 0
            minimalTime = 721
        }
        return finalList
    }

    /**
     * Сравнение на равенство.
     * Расписания считаются одинаковыми, если содержат одинаковый набор поездов,
     * и поезда с тем же именем останавливаются на одинаковых станциях в одинаковое время.
     */
    override fun equals(other: Any?): Boolean {
        if (other is TrainTimeTable) {
            val set1 = trains.toSet()
            val set2 = other.trains.toSet()
            if (set1 == set2) return true
        }
        return false
    }
}

/**
 * Время (часы, минуты)
 */
data class Time(val hour: Int, val minute: Int) : Comparable<Time> {
    /**
     * Сравнение времён на больше/меньше (согласно контракту compareTo)
     */
    override fun compareTo(other: Time): Int = this.hour * 60 + this.minute - other.hour * 60 - other.minute
}

/**
 * Остановка (название, время прибытия)
 */
data class Stop(val name: String, val time: Time)

/**
 * Поезд (имя, список остановок, упорядоченный по времени).
 * Первой идёт начальная остановка, последней конечная.
 */
data class Train(val name: String, val stops: MutableList<Stop>) {
    constructor(name: String, vararg stops: Stop) : this(name, stops.asList().toMutableList())
}