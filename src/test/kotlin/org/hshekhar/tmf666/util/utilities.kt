package org.hshekhar.tmf666.util

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper

/**
 * @created 3/12/2021'T'10:48 AM
 * @author Himanshu Shekhar (609080540)
 **/

val DATETIME_FORMAT = ""

val ModelMapper = jacksonObjectMapper().also {
    it.setSerializationInclusion(JsonInclude.Include.NON_NULL)
}