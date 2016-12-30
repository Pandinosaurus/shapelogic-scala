package org.shapelogic.sc

package object image {

  /**
   * Simple description of layout of colors band in array
   */
  case class RGBOffsets(red: Int, green: Int, blue: Int, alpha: Int, hasAlpha: Boolean)

  val grayRGBOffsets = RGBOffsets(red = 0, green = 1, blue = 2, alpha = 3, hasAlpha = false)
  val bgrRGBOffsets = RGBOffsets(red = 2, green = 1, blue = 0, alpha = 3, hasAlpha = false)
  val rgbRGBOffsets = RGBOffsets(red = 0, green = 1, blue = 2, alpha = 3, hasAlpha = false)
  val rgbaRGBOffsets = RGBOffsets(red = 0, green = 1, blue = 2, alpha = 3, hasAlpha = true)
  val abgrRGBOffsets = RGBOffsets(red = 3, green = 2, blue = 1, alpha = 0, hasAlpha = true)

  val redBlueSwap = Array(2, 1, 0, 3)
}