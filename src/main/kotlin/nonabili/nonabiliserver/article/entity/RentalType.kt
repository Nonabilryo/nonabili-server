package nonabili.nonabiliserver.article.entity

enum class RentalType(val value: Int) {
    YEAR(0),
    MONTH(1),
    DAY(2),
    HOUR(3);

    companion object {
        private val map = values().associateBy(RentalType::value)
        fun fromInt(type: Int) = map[type]
    }
}