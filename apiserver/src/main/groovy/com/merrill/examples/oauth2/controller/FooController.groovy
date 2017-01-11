package com.merrill.examples.oauth2.controller

import groovy.json.JsonOutput
import org.springframework.http.MediaType
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController


/**
 * Created by upaulm2 on 1/10/17.
 */
@RestController
@RequestMapping("/foo")
class FooController {

    @PreAuthorize("#oauth2.hasScope('read')")
    @RequestMapping(value = "/foos", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.GET)
    def getFoos() {
        JsonOutput.toJson(
                [
                        [fooName: 'F1_Name', fooId: 'F1'],
                        [fooName: 'F1_Name', fooId: 'F1']
                ]
        )
    }

    @PreAuthorize("#oauth2.hasScope('read')")
    @RequestMapping(value = "/foo/{id}", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.GET)
    def getFoo(@PathVariable String id) {
        JsonOutput.toJson(
                [
                        fooName: "Foo ${id} Name",
                        fooId: "FID ${id}"
                ]
        )
    }
}
