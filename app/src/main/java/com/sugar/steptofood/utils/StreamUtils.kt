package com.sugar.steptofood.utils

import java.io.ByteArrayOutputStream
import java.io.InputStream

fun readBytes(inputStream: InputStream): ByteArray {
    val byteBuffer = ByteArrayOutputStream()
    val bufferSize = 1024
    val buffer = ByteArray(bufferSize)

    var len = inputStream.read(buffer)
    while (len != -1) {
        byteBuffer.write(buffer, 0, len)
        len = inputStream.read(buffer)
    }
    return byteBuffer.toByteArray()
}