package com.duynn.zahoo.data.repository.source.local.api.db

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

/**
 *Created by duynn100198 on 10/04/21.
 */
class MigrationManager {
    companion object {
        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
            }
        }
    }
}
