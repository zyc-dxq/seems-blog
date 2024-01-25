package top.seems.blog.kt.runtime.interceptor

import org.babyfish.jimmer.kt.isLoaded
import org.babyfish.jimmer.sql.DraftInterceptor
import org.springframework.stereotype.Component
import top.seems.blog.kt.common.HandleField
import top.seems.blog.kt.common.HandleFieldDraft
import top.seems.blog.kt.runtime.untils.currentUser
import top.seems.blog.kt.runtime.untils.isHasUser
import java.time.LocalDateTime

@Component
class HandleFieldDraftInterceptor : DraftInterceptor<HandleField, HandleFieldDraft> {

    override fun beforeSave(draft: HandleFieldDraft, original: HandleField?) {
        if (!isLoaded(draft, HandleField::updateTime) && original != null) {
            draft.updateTime = LocalDateTime.now()
            if (isHasUser()) {
                draft.updateBy = currentUser().userName
            } else {
                draft.updateBy = ""
            }

        }
        if (original == null && !isLoaded(draft, HandleField::createTime)) {
            draft.createTime = LocalDateTime.now()
            if (isHasUser()) {
                draft.createBy = currentUser().userName
            } else {
                draft.createBy = ""
            }
        }
    }
}
