package i.am.shiro.amai

import io.realm.DynamicRealm
import io.realm.RealmMigration

internal class Migration : RealmMigration {
    override fun migrate(realm: DynamicRealm, oldVersion: Long, newVersion: Long) {
        if (oldVersion == 0L) {
            realm.schema["SearchModel"]!!.removeField("isLoading")
        }
    }
}