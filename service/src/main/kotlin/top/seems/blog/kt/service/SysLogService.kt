package top.seems.blog.kt.service

import org.babyfish.jimmer.client.FetchBy
import org.babyfish.jimmer.spring.model.SortUtils
import org.babyfish.jimmer.sql.kt.fetcher.newFetcher
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.*
import top.seems.blog.kt.dto.SaveSysLogInput
import top.seems.blog.kt.dto.SysLogSpecification
import top.seems.blog.kt.dto.UpdateSysLogInput
import top.seems.blog.kt.model.SysLog
import top.seems.blog.kt.model.by
import top.seems.blog.kt.repository.SysLogRepository
import top.seems.blog.kt.runtime.annotation.AutoLog

/**
 * log日志接口服务
 */
@RestController
@RequestMapping("/sys-log")
@Transactional
class SysLogService(
    private val repository: SysLogRepository
) {
    /**
     * 查询log日志列表
     * @param specification 查询条件
     * @param sortCode 排序参数
     * @return 日志列表
     */
    @GetMapping("/list")
    @AutoLog("查询list列表")
    fun findAuthors(
        specification: SysLogSpecification,
        @RequestParam(required = false, defaultValue = "createTime desc") sortCode: String
    ): List<@FetchBy("DEFAULT_FETCHER") SysLog> =
        repository.find(specification, SortUtils.toSort(sortCode), DEFAULT_FETCHER)

    /**
     * 查询log日志
     * @param id 主键id
     */
    @GetMapping("/{id}")
    fun findComplexAuthor(
        @PathVariable id: Long
    ): @FetchBy("COMPLEX_FETCHER") SysLog? = repository.findNullable(id, COMPLEX_FETCHER)

    /**
     * 保存log日志
     */
    @PutMapping("/save")
    fun saveAuthor(
        @RequestBody input: SaveSysLogInput
    ): SysLog = repository.save(input)

    /**
     * 修改log日志
     */
    @PutMapping("/update")
    fun updateAuthor(
        @RequestBody input: UpdateSysLogInput
    ): SysLog = repository.save(input)

    /**
     * 删除log日志
     */
    @DeleteMapping("/{id}")
    fun deleteAuthor(
        @PathVariable id: Long
    ) {
        repository.deleteById(id)
    }

    companion object {

        @JvmStatic
        private val DEFAULT_FETCHER = newFetcher(SysLog::class).by {
            allScalarFields()
        }

        @JvmStatic
        private val COMPLEX_FETCHER = newFetcher(SysLog::class).by {
            allScalarFields()
        }
    }
}
