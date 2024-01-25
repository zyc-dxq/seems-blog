package top.seems.blog.kt.common

import org.babyfish.jimmer.spring.model.SortUtils
import org.springframework.data.domain.PageRequest

class PageModel(
    private val pageIndex: Long = 0,
    private val pageSize: Long = 10,
    private val sortCode: String = "create_time desc"
) {
    fun pageable(): PageRequest {
        return PageRequest.of(pageIndex.toInt(), pageSize.toInt(), SortUtils.toSort(sortCode))
    }
}
