package com.merrill.examples.oauth2.commons.utils.db.impl

import com.merrill.examples.oauth2.commons.utils.db.DBHelper
import org.springframework.stereotype.Component

import java.sql.Connection
import java.sql.ResultSet
import java.sql.Statement

/**
 * Created by upaulm2 on 9/20/16.
 */
@Component("mysqlHelper")
class MySqlDBHelper implements DBHelper {
	@Override
	def cleanup(Connection connection) {
		def dbName = connection.metaData.database
		println "@@@@@ Cleaning up ${dbName} DB for DEV ***************************"
		Statement st
		try {
			st = connection.createStatement()
			st.addBatch("SET FOREIGN_KEY_CHECKS=0")

			// clean all tables
			ResultSet rs = connection.metaData.getTables(null, null, null)
			while (rs.next()) {
				def tablename = rs.getString(3)
				println "drop table ${tablename}"
				st.addBatch("drop table ${tablename}")
			}
			rs.close()
			st.addBatch("SET FOREIGN_KEY_CHECKS=1")
			st.executeBatch()
		} catch (e) {
			e.printStackTrace()
		} finally {
			if (st) {
				st.close()
			}
		}
	}
}
