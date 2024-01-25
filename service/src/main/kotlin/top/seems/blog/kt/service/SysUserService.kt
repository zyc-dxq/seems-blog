package top.seems.blog.kt.service

//import org.babyfish.jimmer.client.ThrowsAll
import org.babyfish.jimmer.client.FetchBy
import org.babyfish.jimmer.spring.model.SortUtils
import org.babyfish.jimmer.sql.kt.fetcher.newFetcher
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.*
import top.seems.blog.kt.dto.SaveSysUserInput
import top.seems.blog.kt.dto.SysUserSpecification
import top.seems.blog.kt.dto.UpdateSysUserInput
import top.seems.blog.kt.model.SysUser
import top.seems.blog.kt.model.by
import top.seems.blog.kt.repository.SysUserRepository

@RestController
@RequestMapping("/sys-user")
@Transactional
class SysUserService(
    private val repository: SysUserRepository
) {
    /**
     * 查询用户列表
     * @param specification 查询条件
     * @param sortCode 排序参数
     */
    @GetMapping("/list")
    fun findAuthors(
        specification: SysUserSpecification,
        @RequestParam(required = false, defaultValue = "createTime desc") sortCode: String
    ): List<@FetchBy("DEFAULT_FETCHER") SysUser> =
        repository.find(specification, SortUtils.toSort(sortCode), DEFAULT_FETCHER)

    /**
     * 查询用户
     * @param id 用户id
     */
    @GetMapping("/{id}")
    fun findComplexAuthor(
        @PathVariable id: Long
    ): @FetchBy("COMPLEX_FETCHER") SysUser? = repository.findNullable(id, COMPLEX_FETCHER)

    /**
     * 保存用户
     */
    @PutMapping("/save")
    fun saveAuthor(
        @RequestBody input: SaveSysUserInput
    ): SysUser = repository.save(input)

    /**
     * 修改用户
     */
    @PutMapping("/update")
    fun updateAuthor(
        @RequestBody input: UpdateSysUserInput
    ): SysUser = repository.save(input)

    /**
     * 删除用户
     */
    @DeleteMapping("/{id}")
    fun deleteAuthor(
        @PathVariable id: Long
    ) {
        repository.deleteById(id)
    }

    companion object {

        @JvmStatic
        private val DEFAULT_FETCHER = newFetcher(SysUser::class).by {
            allScalarFields()
        }

        @JvmStatic
        private val COMPLEX_FETCHER = newFetcher(SysUser::class).by {
            allScalarFields()
        }
    }
}
