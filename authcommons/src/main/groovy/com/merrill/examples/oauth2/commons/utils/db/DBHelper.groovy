package com.merrill.examples.oauth2.commons.utils.db

import java.sql.Connection

/**
 * Created by upaulm2 on 9/20/16.
 */
public interface DBHelper {
	def cleanup(Connection connection)
}