package com.renameyourappname.mobile.utils

import android.R.attr.path
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Bitmap.CompressFormat
import android.graphics.Canvas
import android.graphics.Color
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.support.annotation.ColorInt
import android.text.TextUtils
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.WriterException
import com.google.zxing.common.CharacterSetECI
import com.google.zxing.qrcode.QRCodeWriter
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.util.*




/**
 * @ClassName: QRCodeUtil
 * @Description: 二维码工具类
 * @Author Wangnan
 * @Date 2017/2/10
 */

object QRCodeUtil {

    /**
     * 创建二维码位图 (自定义黑、白色块颜色)
     *
     * @param content 字符串内容
     * @param size 位图宽&高(单位:px)
     * @param color_black 黑色色块的自定义颜色值
     * @param color_white 白色色块的自定义颜色值
     * @return
     */

    fun createQRCodeBitmap(content: String, size: Int, @ColorInt color_black: Int, @ColorInt color_white: Int): Bitmap? {
        return createQRCodeBitmap(content, size, "UTF-8", "H", "4", color_black, color_white, null, null, 0f)
    }

    /**
     * 创建二维码位图 (带Logo小图片)
     *
     * @param content 字符串内容
     * @param size 位图宽&高(单位:px)
     * @param logoBitmap logo图片
     * @param logoPercent logo小图片在二维码图片中的占比大小,范围[0F,1F]。超出范围->默认使用0.2F
     * @return
     */
    fun createQRCodeBitmap(content: String, size: Int, logoBitmap: Bitmap, logoPercent: Float): Bitmap? {
        return createQRCodeBitmap(content, size, "UTF-8", "H", "4", Color.BLACK, Color.WHITE, null, logoBitmap, logoPercent)
    }

    /**
     * 创建二维码位图 (Bitmap颜色代替黑色) 注意!!!注意!!!注意!!! 选用的Bitmap图片一定不能有白色色块,否则会识别不出来!!!
     *
     * @param content 字符串内容
     * @param size 位图宽&高(单位:px)
     * @param targetBitmap 目标图片 (如果targetBitmap != null, 黑色色块将会被该图片像素色值替代)
     * @return
     */
    fun createQRCodeBitmap(content: String, size: Int, targetBitmap: Bitmap): Bitmap? {
        return createQRCodeBitmap(content, size, "UTF-8", "H", "4", Color.BLACK, Color.WHITE, targetBitmap, null, 0f)
    }

    /**
     * 创建二维码位图 (支持自定义配置和自定义样式)
     *
     * @param content 字符串内容
     * @param size 位图宽&高(单位:px)
     * @param character_set 字符集/字符转码格式 (支持格式:[CharacterSetECI])。传null时,zxing源码默认使用 "ISO-8859-1"
     * @param error_correction 容错级别 (支持级别:[ErrorCorrectionLevel])。传null时,zxing源码默认使用 "L"
     * @param margin 空白边距 (可修改,要求:整型且>=0), 传null时,zxing源码默认使用"4"。
     * @param color_black 黑色色块的自定义颜色值
     * @param color_white 白色色块的自定义颜色值
     * @param targetBitmap 目标图片 (如果targetBitmap != null, 黑色色块将会被该图片像素色值替代)
     * @param logoBitmap logo小图片
     * @param logoPercent logo小图片在二维码图片中的占比大小,范围[0F,1F],超出范围->默认使用0.2F。
     * @return
     */
    @JvmOverloads
    fun createQRCodeBitmap(content: String, size: Int,
                           character_set: String = "UTF-8", error_correction: String = "H", margin: String = "4",
                           @ColorInt color_black: Int = Color.BLACK, @ColorInt color_white: Int = Color.WHITE, targetBitmap: Bitmap? = null,
                           logoBitmap: Bitmap? = null, logoPercent: Float = 0f): Bitmap? {
        var targetBitmap = targetBitmap

        /** 1.参数合法性判断  */
        if (TextUtils.isEmpty(content)) { // 字符串内容判空
            return null
        }

        if (size <= 0) { // 宽&高都需要>0
            return null
        }

        try {
            /** 2.设置二维码相关配置,生成BitMatrix(位矩阵)对象  */
            val hints = Hashtable<EncodeHintType, String>()

            if (!TextUtils.isEmpty(character_set)) {
                hints.put(EncodeHintType.CHARACTER_SET, character_set) // 字符转码格式设置
            }

            if (!TextUtils.isEmpty(error_correction)) {
                hints.put(EncodeHintType.ERROR_CORRECTION, error_correction) // 容错级别设置
            }

            if (!TextUtils.isEmpty(margin)) {
                hints.put(EncodeHintType.MARGIN, margin) // 空白边距设置
            }
            val bitMatrix = QRCodeWriter().encode(content, BarcodeFormat.QR_CODE, size, size, hints)

            /** 3.根据BitMatrix(位矩阵)对象为数组元素赋颜色值  */
            if (targetBitmap != null) {
                targetBitmap = Bitmap.createScaledBitmap(targetBitmap, size, size, false)
            }
            val pixels = IntArray(size * size)
            for (y in 0 until size) {
                for (x in 0 until size) {
                    if (bitMatrix.get(x, y)) { // 黑色色块像素设置
                        if (targetBitmap != null) {
                            pixels[y * size + x] = targetBitmap.getPixel(x, y)
                        } else {
                            pixels[y * size + x] = color_black
                        }
                    } else { // 白色色块像素设置
                        pixels[y * size + x] = color_white
                    }
                }
            }

            /** 4.创建Bitmap对象,根据像素数组设置Bitmap每个像素点的颜色值,之后返回Bitmap对象  */
            val bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)
            bitmap.setPixels(pixels, 0, size, 0, 0, size, size)

            /** 5.为二维码添加logo小图标  */
            return if (logoBitmap != null) {
                addLogo(bitmap, logoBitmap, logoPercent)
            } else bitmap

        } catch (e: WriterException) {
            e.printStackTrace()
        }

        return null
    }

    /**
     * 向一张图片中间添加logo小图片(图片合成)
     *
     * @param srcBitmap 原图片
     * @param logoBitmap logo图片
     * @param logoPercent 百分比 (用于调整logo图片在原图片中的显示大小, 取值范围[0,1], 传值不合法时使用0.2F)
     * 原图片是二维码时,建议使用0.2F,百分比过大可能导致二维码扫描失败。
     * @return
     */
    private fun addLogo(srcBitmap: Bitmap?, logoBitmap: Bitmap?, logoPercent: Float): Bitmap? {
        var logoPercent = logoPercent

        /** 1. 参数合法性判断  */
        if (srcBitmap == null) {
            return null
        }

        if (logoBitmap == null) {
            return srcBitmap
        }

        if (logoPercent < 0f || logoPercent > 1f) {
            logoPercent = 0.2f
        }

        /** 2. 获取原图片和Logo图片各自的宽、高值  */
        val srcWidth = srcBitmap.width
        val srcHeight = srcBitmap.height
        val logoWidth = logoBitmap.width
        val logoHeight = logoBitmap.height

        /** 3. 计算画布缩放的宽高比  */
        val scaleWidth = srcWidth * logoPercent / logoWidth
        val scaleHeight = srcHeight * logoPercent / logoHeight

        /** 4. 使用Canvas绘制,合成图片  */
        val bitmap = Bitmap.createBitmap(srcWidth, srcHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        canvas.drawBitmap(srcBitmap, 0f, 0f, null)
        canvas.scale(scaleWidth, scaleHeight, srcWidth / 2f, srcHeight / 2f)
        canvas.drawBitmap(logoBitmap, srcWidth / 2f - logoWidth / 2f, srcHeight / 2f - logoHeight / 2f, null)

        return bitmap
    }

/*    *//**
     * 保存bitmap到SD卡
     * @param bitName 保存的名字
     * @param mBitmap 图片对像
     * return 生成压缩图片后的图片路径
     *//*
    fun saveBitmap(bitName: String, mBitmap: Bitmap): String {
        val f = File("/sdcard/$bitName.png")
        try {
            f.createNewFile()
        } catch (e: IOException) {
            Logger.e("在保存图片时出错：" + e.toString())
        }

        var fOut: FileOutputStream? = null
        try {
            fOut = FileOutputStream(f)
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }

        try {
            mBitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut)
        } catch (e: Exception) {
            return "create_bitmap_error"
        }

        try {
            fOut!!.flush()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        try {
            fOut!!.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return "/sdcard/$bitName.png"
    }*/

    fun saveImageToGallery(context: Context, bmp: Bitmap) {
        // 首先保存图片
        val appDir = File(Environment.getExternalStorageDirectory(), "Boohee")
        if (!appDir.exists()) {
            appDir.mkdir()
        }
        val fileName = System.currentTimeMillis().toString() + ".jpg"
        val file = File(appDir, fileName)
        try {
            val fos = FileOutputStream(file)
            bmp.compress(CompressFormat.JPEG, 100, fos)
            fos.flush()
            fos.close()
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        // 其次把文件插入到系统图库
        try {
            MediaStore.Images.Media.insertImage(context.getContentResolver(),
                    file.getAbsolutePath(), fileName, null)
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }

        // 最后通知图库更新
        context.sendBroadcast(Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://$path")))
    }
}



