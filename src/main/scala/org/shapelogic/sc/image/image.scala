package org.shapelogic.sc

package object image {

  /**
   * Simple description of layout of colors band in array
   */
  case class RGBOffsets(red: Int, green: Int, blue: Int, alpha: Int, hasAlpha: Boolean) {
    lazy val colorMap = Map(
      red -> "red",
      green -> "green",
      blue -> "blue",
      alpha -> "alpha")

    def byte2Unsigned(byte: Byte): Int = {
      if (byte < 0)
        256 + byte
      else
        byte.toInt
    }

    def colorFromByteSeq(intSeq: Seq[Byte]): String = {
      try {
        val colors = intSeq.zipWithIndex.map { case (value, i) => s"${colorMap(i)}: ${byte2Unsigned(value)}" }
        colors.mkString(", ")
      } catch {
        case ex: Throwable => {
          println(ex.getMessage)
          "Count not make RGB"
        }
      }
    }
  }

  val grayRGBOffsets = RGBOffsets(red = 0, green = 0, blue = 0, alpha = 1, hasAlpha = false)
  val grayAlphaRGBOffsets = RGBOffsets(red = 0, green = 0, blue = 0, alpha = 1, hasAlpha = true)
  val bgrRGBOffsets = RGBOffsets(red = 2, green = 1, blue = 0, alpha = 3, hasAlpha = false)
  val bgraRGBOffsets = RGBOffsets(red = 2, green = 1, blue = 0, alpha = 3, hasAlpha = true)
  val rgbRGBOffsets = RGBOffsets(red = 0, green = 1, blue = 2, alpha = 3, hasAlpha = false)
  val rgbaRGBOffsets = RGBOffsets(red = 0, green = 1, blue = 2, alpha = 3, hasAlpha = true)
  val abgrRGBOffsets = RGBOffsets(red = 3, green = 2, blue = 1, alpha = 0, hasAlpha = true)

  val redBlueSwap = Array(2, 1, 0, 3)

  trait HasBufferImage[T] {
    def result: BufferImage[T]
  }

  /**
   * Intention is to use this for menu registration
   */
  case class ImageTransformWithName[T](transform: BufferImage[T] => BufferImage[T], name: String) extends ImageTransformWithNameT[T]

  trait ImageTransformWithNameT[T] {
    def transform: BufferImage[T] => BufferImage[T]
    def name: String
  }

  //  type ImageTransformWithNameT = ImageTransformWithNameGenT[Byte]
  //
  //  /**
  //   * Intention is to use this for menu registration
  //   * For now only have the Byte version
  //   */
  //  type ImageTransformWithName = ImageTransformWithNameGenT[Byte]

  trait ImageTransformDialogT {
    def transform: (BufferImage[Byte], String) => BufferImage[Byte]
    def name: String
    def dialog: String
    def defaultValue: String
  }

  case class ImageTransformDialog(
    transform: (BufferImage[Byte], String) => BufferImage[Byte],
    name: String,
    dialog: String,
    defaultValue: String) extends ImageTransformDialogT

  /**
   * 
   */
  case class CrossingCountAndMatcing(crossings: Int, matching: Int)

}