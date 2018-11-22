package com.sugar.steptofood.utils.extension

import java.io.ByteArrayOutputStream
import java.io.InputStream

fun InputStream.readBytes(): ByteArray {
    val byteBuffer = ByteArrayOutputStream()
    val bufferSize = 1024
    val buffer = ByteArray(bufferSize)

    var len = this.read(buffer)
    while (len != -1) {
        byteBuffer.write(buffer, 0, len)
        len = this.read(buffer)
    }
    return byteBuffer.toByteArray()
}