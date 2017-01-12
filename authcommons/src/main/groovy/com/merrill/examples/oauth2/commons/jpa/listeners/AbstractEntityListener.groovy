package com.merrill.examples.oauth2.commons.jpa.listeners

import com.merrill.examples.oauth2.commons.domain.AbstractEntity
import org.springframework.stereotype.Component

import javax.persistence.PrePersist
import javax.persistence.PreUpdate
import java.sql.Timestamp
import java.time.LocalDateTime

@Component
class AbstractEntityListener {
	@PreUpdate
	public void onBeforeUpdate(AbstractEntity abstractEntity) {
		def now = Timestamp.valueOf(LocalDateTime.now())
		abstractEntity.lastUpdated = now
	}


	@PrePersist
	public void onBeforeInsert(AbstractEntity abstractEntity) {
		def now = Timestamp.valueOf(LocalDateTime.now())
		abstractEntity.lastCreated = now
		abstractEntity.lastUpdated = now
	}
}
