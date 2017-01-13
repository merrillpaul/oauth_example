package com.merrill.examples.oauth2.commons.jms.dto

/**
 * Created by upaulm2 on 1/13/17.
 */
enum TokenMessageType {
    SAVE_ACCESS_TOKEN, SAVE_REFRESH_TOKEN, DEL_ACCESS_TOKEN, DEL_REFRESH_TOKEN,
    DEL_ACCESS_BY_REFRESH_TOKEN
}