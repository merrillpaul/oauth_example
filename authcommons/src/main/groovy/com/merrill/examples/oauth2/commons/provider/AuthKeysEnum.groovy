package com.merrill.examples.oauth2.commons.provider


enum AuthKeysEnum implements Serializable {
    id,
    firstName,
    lastName,
    timezoneId,
    honorific,
    metaData
}

enum AuthUserMetaDataEnum implements Serializable {
    BUSINESS_ENTITY,
    COUNTRY_CODE
}