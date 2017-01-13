package com.merrill.examples.oauth2.commons.jms.impl

import com.merrill.examples.oauth2.commons.config.mongo.EnvironmentAwareMongoDbBuilder
import com.merrill.examples.oauth2.commons.domain.tokenstore.AccessToken
import com.merrill.examples.oauth2.commons.domain.tokenstore.RefreshToken
import com.merrill.examples.oauth2.commons.jms.AbstractTokenMessageReceiver
import com.merrill.examples.oauth2.commons.jms.dto.TokenMessagePayload
import com.merrill.examples.oauth2.commons.jms.dto.TokenMessageType
import com.merrill.examples.oauth2.commons.utils.DataBinder
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.mongodb.MongoDbFactory
import org.springframework.data.mongodb.core.MongoOperations
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.stereotype.Component

/**
 * Created by upaulm2 on 1/13/17.
 */
@Component
class MongoTokenMessageReceiver extends AbstractTokenMessageReceiver {

    @Autowired
    EnvironmentAwareMongoDbBuilder environmentAwareMongoDbBuilder

    @Override
    def processPayload(TokenMessagePayload tokenMessagePayload) {
        def creds = tokenMessagePayload.tokenStoreCreds
        if (creds && creds.tokenStoreUrl) {
            MongoOperations mongoTemplate = resolveMongoTemplate(creds)
            switch (tokenMessagePayload.type) {
                case TokenMessageType.SAVE_ACCESS_TOKEN:
                    def docAnn = (Document) AccessToken.annotations.find { it.annotationType() == Document }
                    def accessToken = new AccessToken()
                    DataBinder.populateObjectFromProperties(accessToken, tokenMessagePayload.payload)
                    mongoTemplate?.save(accessToken, docAnn.collection())
                    break
                case TokenMessageType.SAVE_REFRESH_TOKEN:
                    def docAnn = (Document) RefreshToken.annotations.find { it.annotationType() == Document }
                    def refreshToken = new RefreshToken()
                    DataBinder.populateObjectFromProperties(refreshToken, tokenMessagePayload.payload)
                    mongoTemplate?.save(refreshToken, docAnn.collection())
                    break
                case TokenMessageType.DEL_ACCESS_TOKEN:
                    def docAnn = (Document) AccessToken.annotations.find { it.annotationType() == Document }
                    def idQuery = new Query(Criteria.where("_id").is(tokenMessagePayload.payload))
                    mongoTemplate?.remove(idQuery, docAnn.collection())
                case TokenMessageType.DEL_REFRESH_TOKEN:
                    def docAnn = (Document) RefreshToken.annotations.find { it.annotationType() == Document }
                    def idQuery = new Query(Criteria.where("_id").is(tokenMessagePayload.payload))
                    mongoTemplate?.remove(idQuery, docAnn.collection())
                case TokenMessageType.DEL_ACCESS_BY_REFRESH_TOKEN:
                    def docAnn = (Document) AccessToken.annotations.find { it.annotationType() == Document }
                    def idQuery = new Query(Criteria.where("refreshToken").is(tokenMessagePayload.payload))
                    mongoTemplate?.remove(idQuery, docAnn.collection())
            }
        }
    }

    private MongoOperations resolveMongoTemplate(creds) {

        def beanKey = "mongoTemplate_${creds.tokenStoreLocationKey}"
        getOrAddBeans(beanKey, {
            def configs = [
                    'mongo-uri': creds.tokenStoreUrl
            ]
            MongoDbFactory factory = environmentAwareMongoDbBuilder.prepareMongoDataSource(configs)
            new MongoTemplate(factory)
        })

    }

}
