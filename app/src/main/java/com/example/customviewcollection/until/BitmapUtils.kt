package com.example.customviewcollection.until

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Matrix

class BitmapUtils {

    companion object {

        fun getBitmap(context: Context,vectorDrawableId : Int) : Bitmap {
            val vectorDrawable = context.getDrawable(vectorDrawableId) ?: throw NullPointerException("can't found the resource")
            val bitmap = Bitmap.createBitmap(vectorDrawable.intrinsicWidth,vectorDrawable.intrinsicHeight, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(bitmap)
            vectorDrawable.setBounds(0,0,canvas.width,canvas.height)
            vectorDrawable.draw(canvas)
            return bitmap
        }

        fun getNewBitmap(bitmap: Bitmap,newWidth : Int,newHeight : Int) : Bitmap {
            val width = bitmap.width
            val height = bitmap.height
            val scaleWidth = newWidth / width.toFloat()
            val scaleHeight = newHeight / height.toFloat()
            val matrix = Matrix()
            matrix.postScale(scaleWidth, scaleHeight)
            return Bitmap.createBitmap(bitmap,0,0,width, height,matrix,true)
        }
    }
}