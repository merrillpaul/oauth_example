package com.merrill.examples.oauth2.commons.mongo

import com.mongodb.WriteResult
import org.springframework.data.mongodb.MongoDbFactory
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.Update

/**
 * Created by upaulm2 on 1/12/17.
 * The customer operations can be async if needed or use a JMS Topic
 */
class CustomerLocationAwareMongoTemplate extends MongoTemplate {

    CustomerTemplateResolver customerTemplateResolver

    public CustomerLocationAwareMongoTemplate(MongoDbFactory mongoDbFactory, CustomerTemplateResolver customerTemplateResolver) {
        super(mongoDbFactory)
        this.customerTemplateResolver = customerTemplateResolver
    }


    protected MongoTemplate resolveTemplate() {
        null
    }

    @Override
    void insert(Object objectToSave) {
        super.insert(objectToSave)
        resolveTemplate()?.insert(objectToSave)
    }

    @Override
    void insert(Object objectToSave, String collectionName) {
        super.insert(objectToSave, collectionName)
        resolveTemplate()?.insert(objectToSave, collectionName)
    }

    @Override
    void insert(Collection<? extends Object> batchToSave, Class<?> entityClass) {
        super.insert(batchToSave, entityClass)
        resolveTemplate()?.insert(batchToSave, entityClass)
    }

    @Override
    void insert(Collection<? extends Object> batchToSave, String collectionName) {
        super.insert(batchToSave, collectionName)
        resolveTemplate()?.insert(batchToSave, collectionName)
    }

    @Override
    void insertAll(Collection<? extends Object> objectsToSave) {
        super.insertAll(objectsToSave)
        resolveTemplate()?.insertAll(objectsToSave)
    }

    @Override
    void save(Object objectToSave) {
        super.save(objectToSave)
        resolveTemplate()?.save(objectToSave)
    }

    @Override
    void save(Object objectToSave, String collectionName) {
        super.save(objectToSave, collectionName)
        resolveTemplate()?.save(objectToSave, collectionName)
    }

    @Override
    WriteResult upsert(Query query, Update update, Class<?> entityClass) {
        def obj =  super.upsert(query, update, entityClass)
        resolveTemplate()?.upsert(query, update, entityClass)
        obj
    }

    @Override
    WriteResult upsert(Query query, Update update, String collectionName) {
        def obj = super.upsert(query, update, collectionName)
        resolveTemplate()?.upsert(query, update, collectionName)
        obj
    }

    @Override
    WriteResult upsert(Query query, Update update, Class<?> entityClass, String collectionName) {
        def obj = super.upsert(query, update, entityClass, collectionName)
        resolveTemplate()?.upsert(query, update, entityClass, collectionName)
        obj
    }

    @Override
    WriteResult remove(Object object) {
        def obj = super.remove(object)
        resolveTemplate()?.remove(object)
        obj
    }

    @Override
    WriteResult remove(Object object, String collection) {
        def obj = super.remove(object, collection)
        resolveTemplate()?.remove(object, collection)
        obj
    }

    @Override
    WriteResult remove(Query query, String collectionName) {
        def obj = super.remove(query, collectionName)
        resolveTemplate()?.remove(query, collectionName)
        obj
    }

    @Override
    WriteResult remove(Query query, Class<?> entityClass) {
        def obj = super.remove(query, entityClass)
        resolveTemplate()?.remove(query, entityClass)
        obj
    }

    @Override
    WriteResult remove(Query query, Class<?> entityClass, String collectionName) {
        def obj = super.remove(query, entityClass, collectionName)
        resolveTemplate()?.remove(query, entityClass, collectionName)
        obj
    }
}
