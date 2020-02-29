@file:Suppress("UNUSED_PARAMETER")

package lesson12.task1

/**
 * Класс "Телефонная книга".
 *
 * Общая сложность задания -- средняя.
 * Объект класса хранит список людей и номеров их телефонов,
 * при чём у каждого человека может быть более одного номера телефона.
 * Человек задаётся строкой вида "Фамилия Имя".
 * Телефон задаётся строкой из цифр, +, *, #, -.
 * Поддерживаемые методы: добавление / удаление человека,
 * добавление / удаление телефона для заданного человека,
 * поиск номера(ов) телефона по заданному имени человека,
 * поиск человека по заданному номеру телефона.
 *
 * Класс должен иметь конструктор по умолчанию (без параметров).
 */
class PhoneBook() {
    val book = mutableListOf<Pair<String, MutableList<String>>>()
    /**
     * Добавить человека.
     * Возвращает true, если человек был успешно добавлен,
     * и false, если человек с таким именем уже был в телефонной книге
     * (во втором случае телефонная книга не должна меняться).
     */
    fun addHuman(name: String): Boolean {
            for (pair in book) {
                if (pair.first == name) return false
            }
            book.add(name to mutableListOf())
            return true
    }

    /**
     * Убрать человека.
     * Возвращает true, если человек был успешно удалён,
     * и false, если человек с таким именем отсутствовал в телефонной книге
     * (во втором случае телефонная книга не должна меняться).
     */
    fun removeHuman(name: String): Boolean {
            for (i in book.indices) {
                if (book[i].first == name) {
                    book.removeAt(i)
                    return true
                }
            }
            return false
    }

    /**
     * Добавить номер телефона.
     * Возвращает true, если номер был успешно добавлен,
     * и false, если человек с таким именем отсутствовал в телефонной книге,
     * либо у него уже был такой номер телефона,
     * либо такой номер телефона зарегистрирован за другим человеком.
     */
    fun addPhone(name: String, phone: String): Boolean {
        for (pair in book) {
            if (pair.second.contains(phone)) return false
            if (pair.first == name) {
                pair.second.add(phone)
                return true
            }
        }
        return false
    }

    /**
     * Убрать номер телефона.
     * Возвращает true, если номер был успешно удалён,
     * и false, если человек с таким именем отсутствовал в телефонной книге
     * либо у него не было такого номера телефона.
     */
    fun removePhone(name: String, phone: String): Boolean {
        for (pair in book) {
            if (pair.first == name && pair.second.contains(phone)) {
                pair.second.remove(phone)
                return true
            }
        }
        return false
    }

    /**
     * Вернуть все номера телефона заданного человека.
     * Если этого человека нет в книге, вернуть пустой список
     */
    fun phones(name: String): Set<String> {
        for (pair in book) {
            if (pair.first == name) return pair.second.toSet()
        }
        val n = setOf<String>()
        return n
    }

    /**
     * Вернуть имя человека по заданному номеру телефона.
     * Если такого номера нет в книге, вернуть null.
     */
    fun humanByPhone(phone: String): String? {
        for (pair in book) {
            if (pair.second.contains(phone)) return pair.first
        }
        return null
    }

    /**
     * Две телефонные книги равны, если в них хранится одинаковый набор людей,
     * и каждому человеку соответствует одинаковый набор телефонов.
     * Порядок людей / порядок телефонов в книге не должен иметь значения.
     */
    val book2 = mutableListOf<Pair<String, MutableList<String>>>()
    override fun equals(other: Any?): Boolean {
            for (pair in book) {
                for (i in book2.indices) {
                    if ((pair.first != book2[i].first && !pair.second.containsAll(book[i].second)) || other !is PhoneBook) return false
                }
            }
        return true
    }

    override fun hashCode(): Int {
        var result = 5
        if(book == book2) {
            result = book.hashCode()
            result = 31 * result + book2.hashCode()
            return result
        }
        return result
    }

}