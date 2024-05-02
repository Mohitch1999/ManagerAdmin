package com.example.managerapp.compress

import android.media.MediaCodec
import android.media.MediaCodecInfo
import android.media.MediaFormat
import java.io.FileInputStream
import java.io.FileOutputStream

class VideoCompressor {

    fun compressVideo(inputFilePath: String, outputFilePath: String, outputWidth: Int, outputHeight: Int, bitRate: Int, frameRate: Int) {
        val inputBuffer = ByteArray(BUFFER_SIZE)
        val outputBuffer = ByteArray(BUFFER_SIZE)
        val mediaCodec = MediaCodec.createEncoderByType(MIME_TYPE)
        val format = MediaFormat.createVideoFormat(MIME_TYPE, outputWidth, outputHeight)
        format.setInteger(MediaFormat.KEY_BIT_RATE, bitRate)
        format.setInteger(MediaFormat.KEY_FRAME_RATE, frameRate)
        format.setInteger(MediaFormat.KEY_COLOR_FORMAT, MediaCodecInfo.CodecCapabilities.COLOR_FormatSurface)
        format.setInteger(MediaFormat.KEY_I_FRAME_INTERVAL, I_FRAME_INTERVAL)
        mediaCodec.configure(format, null, null, MediaCodec.CONFIGURE_FLAG_ENCODE)
        mediaCodec.start()

        val inputSurface = mediaCodec.createInputSurface()

        val inputFile = FileInputStream(inputFilePath)
        val outputFile = FileOutputStream(outputFilePath)
        val bufferInfo = MediaCodec.BufferInfo()
        var inputEndOfStream = false
        var outputEndOfStream = false

        try {
            while (!outputEndOfStream) {
                if (!inputEndOfStream) {
                    val inputBufferIndex = mediaCodec.dequeueInputBuffer(-1)
                    if (inputBufferIndex >= 0) {
                        val inputBuffer = mediaCodec.getInputBuffer(inputBufferIndex)
                        inputBuffer?.clear()
                        val bytesRead = inputFile.read(inputBuffer?.array())
                        if (bytesRead == -1) {
                            mediaCodec.queueInputBuffer(inputBufferIndex, 0, 0, 0, MediaCodec.BUFFER_FLAG_END_OF_STREAM)
                            inputEndOfStream = true
                        } else {
                            mediaCodec.queueInputBuffer(inputBufferIndex, 0, bytesRead, 0, 0)
                        }
                    }
                }

                val outputBufferIndex = mediaCodec.dequeueOutputBuffer(bufferInfo, TIMEOUT_USEC.toLong())
                if (outputBufferIndex >= 0) {
                    val outputBuffer = mediaCodec.getOutputBuffer(outputBufferIndex)
                    val chunkSize = bufferInfo.size + BUFFER_EXTRA_SIZE
                    if (outputBuffer != null) {
                        outputBuffer.get(outputBuffer.array(), 0, chunkSize)
                        outputBuffer.position(0)
                        outputBuffer.limit(chunkSize)
                        outputFile.write(outputBuffer.array(), 0, chunkSize)
                    }
                    mediaCodec.releaseOutputBuffer(outputBufferIndex, false)
                    if (bufferInfo.flags and MediaCodec.BUFFER_FLAG_END_OF_STREAM != 0) {
                        outputEndOfStream = true
                    }
                }
            }
        } finally {
            mediaCodec.stop()
            mediaCodec.release()
            inputFile.close()
            outputFile.close()
        }
    }
    companion object {
        private const val MIME_TYPE = "video/avc"
        private const val BUFFER_SIZE = 1024 * 1024
        private const val BUFFER_EXTRA_SIZE = 1024
        private const val TIMEOUT_USEC = 10000
        private const val I_FRAME_INTERVAL = 5
    }
}