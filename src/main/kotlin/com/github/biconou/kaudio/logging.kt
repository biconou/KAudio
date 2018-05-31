package com.github.biconou.kaudio

import org.slf4j.Logger
import org.slf4j.LoggerFactory


// http://www.baeldung.com/kotlin-logging

interface Logging

fun <T : Logging> T.logger(): Logger = LoggerFactory.getLogger(javaClass)
