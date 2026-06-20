import { QinDb } from "java:com.qin.runtime.core.db"

export const db = QinDb.fromSystemProperties()

export const users = QinDb.table("qin_demo_users")
users.bigserial("id").primaryKey()
users.text("name").notNull()
users.text("email").notNull().unique()
users.timestamptz("created_at").notNull().defaultNow()

db.ensure(users)
