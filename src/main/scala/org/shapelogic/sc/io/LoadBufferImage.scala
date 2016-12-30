package org.shapelogic.sc.io

import java.awt._
import java.awt.event._
import java.awt.image._
import java.io._
import javax.imageio._
import scala.util.Try
import java.awt.image.Raster
import java.awt.image.DataBufferByte
import org.shapelogic.sc.image.ReadImage
import org.shapelogic.sc.image.BufferImage
import org.shapelogic.sc.image._

/**
 * BufferImage is the workhorse image type
 * Factory for this currently only handles the easy AWT BufferedImage
 */
object LoadBufferImage {

  val coveredBufferedImageTypeSet: Set[Int] = Set(
    BufferedImage.TYPE_3BYTE_BGR,
    BufferedImage.TYPE_BYTE_GRAY,
    BufferedImage.TYPE_4BYTE_ABGR)

  def awtBufferedImage2BufferImage(awtBufferedImage: BufferedImage): Option[BufferImage[Byte]] = {
    val rgbType = awtBufferedImage.getType
    println(s"rgbType: $rgbType")
    val colorModel = awtBufferedImage.getColorModel
    println(s"colorModel: $colorModel")
    if (coveredBufferedImageTypeSet.contains(rgbType)) {
      try {
        val raster = awtBufferedImage.getData
        val byteBuffer: Array[Byte] = LoadImage.rasterToByteArray(raster)
        val res: BufferImage[Byte] =
          if (rgbType == BufferedImage.TYPE_3BYTE_BGR) {
            val byteArray = raster.getDataBuffer.asInstanceOf[DataBufferByte].getData
            new BufferImage(
              width = awtBufferedImage.getWidth,
              height = awtBufferedImage.getHeight,
              numBands = 3,
              bufferInput = byteArray,
              rgbOffsetsOpt = Some(rgbRGBOffsets))
          } else if (rgbType == BufferedImage.TYPE_4BYTE_ABGR) {
            val byteArray = raster.getDataBuffer.asInstanceOf[DataBufferByte].getData
            new BufferImage(
              width = awtBufferedImage.getWidth,
              height = awtBufferedImage.getHeight,
              numBands = 4,
              bufferInput = byteArray,
              rgbOffsetsOpt = Some(rgbaRGBOffsets))
          } else if (rgbType == BufferedImage.TYPE_BYTE_GRAY)
            new BufferImage(
              width = awtBufferedImage.getWidth,
              height = awtBufferedImage.getHeight,
              numBands = 3,
              bufferInput = byteBuffer,
              rgbOffsetsOpt = Some(grayRGBOffsets))
          else {
            println(s"rgbType: $rgbType")
            null
          }
        Some(res)
      } catch {
        case ex: Throwable => {
          println("awtBufferedImage2BufferImage error:" + ex.getMessage)
          None
        }
      }
    } else
      None
  }

  def main(args: Array[String]): Unit = {
    println(s"args: ${args.toSeq}")
    val filename = if (0 < args.size) args(0) else "../image3bgr.jpg"
    val imageOpt = LoadImage.loadAWTBufferedImage(filename).toOption
    imageOpt match {
      case Some(image) => {
        val wrappedOpt = awtBufferedImage2BufferImage(image)
        wrappedOpt match {
          case Some(wrapped) => {
            val pointRGB = wrapped.getPixel(10, 10).toSeq
            println(s"Image loaded. RGB: $pointRGB")
          }
          case None => println("Very strange")
        }
      }
      case None => println("Image could not be loaded")
    }
  }
}