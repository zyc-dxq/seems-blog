//package top.seems.jimmer.kt.runtime.converter
//
//import org.babyfish.jimmer.jackson.Converter
//import java.sql.Date
//
//class DateConverter : Converter<String, Date?> {
//    override fun output(value: String?): Date? {
//        if (value.isNullOrEmpty()) {
//            return null
//        }
//        return Date.valueOf(value);
//    }
//}
