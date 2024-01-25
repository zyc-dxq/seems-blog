package top.seems.blog.kt.enums

import org.babyfish.jimmer.sql.EnumItem

enum class SysLogType {

    @EnumItem(name = "登录")
    LOGIN,
    @EnumItem(name = "添加")
    ADD,
    @EnumItem(name = "删除")
    DELETE,
    @EnumItem(name = "更新")
    UPDATE,
    @EnumItem(name = "查询")
    QUERY,
    @EnumItem(name = "定时任务")
    JOB;
}
