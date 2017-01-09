package org.shapelogic.sc.operation

import org.shapelogic.sc.image.BufferImage
import spire.math._
import spire.implicits._
import scala.reflect.ClassTag

/**
 * Should take an image and a value
 * Return gray scale image with 2 values 0 and 255
 */
class ThresholdOperation[@specialized T: ClassTag: Numeric: Ordering](
    inputImage: BufferImage[T],
    threshold: Double) {

  lazy val verboseLogging: Boolean = true

  lazy val implicitsForPromotion = {
    val res = new NumberPromotion.HighWithLowPriorityImplicits[T]()
    val typeOfInput = implicitly[ClassTag[T]]
    println(s"typeOfInput: $typeOfInput")
    val className = res.getClass().getName
    println(s"================= className: $className")
    res
  }

  import implicitsForPromotion._

  lazy val promoter: NumberPromotion[T] = implicitly[NumberPromotion[T]]

  def resToInt(input: promoter.Out): Int = {
    input match {
      case intVal: Int => intVal
      case byteVal: Byte => byteVal.toInt // & NumberPromotion.byteMask
      case _ => 0
    }
  }

  lazy val outputImage = new BufferImage[Byte](
    width = inputImage.width,
    height = inputImage.height,
    numBands = 1,
    bufferInput = null,
    rgbOffsetsOpt = None)

  lazy val inBuffer = inputImage.data
  lazy val outBuffer = outputImage.data
  lazy val inputNumBands = inputImage.numBands
  lazy val indexColorPixel: IndexColorPixel[T] = IndexColorPixel.apply(inputImage)
  lazy val pixelOperation: PixelOperation[T] = new PixelOperation(inputImage)

  var low = 0
  var high = 0

  val lowValue: Byte = 0
  val highValue: Byte = -1 // 255

  /**
   * This easily get very inefficient
   */
  def handleIndex(index: Int, indexOut: Int): Unit = {
    try {
      val oneChannel = promoter.promote(indexColorPixel.getRed(index))
      if (threshold < resToInt(oneChannel)) { //Problem with sign 
        high += 1
        outBuffer(indexOut) = highValue
      } else {
        low += 1
        outBuffer(indexOut) = lowValue
      }
    } catch {
      case ex: Throwable => {
        println(ex.getMessage)
      }
    }
  }

  /**
   * Run over input and output
   * Should I do by line?
   */
  def calc(): BufferImage[Byte] = {
    val pointCount = inputImage.width * inputImage.height
    pixelOperation.reset()
    var indexOut: Int = -1
    var index: Int = pixelOperation.index
    while (pixelOperation.hasNext) {
      index = pixelOperation.next()
      indexOut += 1
      handleIndex(index, indexOut)
    }
    if (verboseLogging)
      println(s"low count: $low, high: $high, index: $index, indexOut: $indexOut")
    outputImage
  }

  lazy val result: BufferImage[Byte] = calc()
}