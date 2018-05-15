package com.renameyourappname.mobile.utils

import java.text.DecimalFormat
import java.text.NumberFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern
import kotlin.experimental.and

/**
 * Created by Kobe on 2017/12/25.
 */
object StringUtils {
    private val emailer = Pattern.compile(
            "\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*")
    private val phone = Pattern.compile("^(1[0-9][0-9])\\d{8}$")

    /**
     * 判断给定字符串是否空白串 空白串是指由空格、制表符、回车符、换行符组成的字符串 若输入字符串为null或空字符串，返回true
     */
    fun isEmpty(input: CharSequence?): Boolean {
        if (input == null || "" == input) {
            return true
        }

        for (i in 0 until input.length) {
            val c = input[i]
            if (c != ' ' && c != '\t' && c != '\r' && c != '\n') {
                return false
            }
        }
        return true
    }

    /**
     * 判断给定字符串是否空白串 空白串是指由空格、制表符、回车符、换行符组成的字符串 若输入字符串为null或空字符串，返回true
     */
 /*   fun isEmpty(vararg strs: CharSequence): Boolean {
        for (str in strs) {
            if (isEmpty(str)) {
                return true
            }
        }
        return false
    }*/

    /**
     * 判断是不是一个合法的电子邮件地址
     */
    fun isEmail(email: CharSequence): Boolean {
        return if (isEmpty(email)) {
            false
        } else emailer.matcher(email)
                .matches()
    }

    /**
     * 判断是不是一个合法的手机号码
     */
    fun isPhone(phoneNum: CharSequence): Boolean {
        return if (isEmpty(phoneNum)) {
            false
        } else phone.matcher(phoneNum)
                .matches()
    }


    /**
     * 返回当前系统时间
     */
    fun getDataTime(format: String): String {
        val df = SimpleDateFormat(format)
        return df.format(Date())
    }

    /**
     * 字符串转整数
     *
     * @param str
     * @param defValue
     * @return
     */
    fun toInt(str: String, defValue: Int): Int {
        try {
            return Integer.parseInt(str)
        } catch (e: Exception) {
        }

        return defValue
    }

    fun toFloat(str: String, defValue: Float): Float {
        try {
            return java.lang.Float.parseFloat(str)
        } catch (e: Exception) {
        }

        return defValue
    }

    fun toFloat(obj: Any?): Float {
        return if (obj == null) {
            0f
        } else toFloat(obj.toString(), 0f)
    }


    /**
     * 对象转整
     *
     * @param obj
     * @return 转换异常返回 0
     */
    fun toInt(obj: Any?): Int {
        return if (obj == null) {
            0
        } else toInt(obj.toString(), 0)
    }

    /**
     * String转long
     *
     * @param obj
     * @return 转换异常返回 0
     */
    fun toLong(obj: String): Long {
        try {
            return java.lang.Long.parseLong(obj)
        } catch (e: Exception) {
        }

        return 0
    }

    /**
     * String转double
     *
     * @param obj
     * @return 转换异常返回 0
     */
    fun toDouble(obj: String): Double {
        try {
            return java.lang.Double.parseDouble(obj)
        } catch (e: Exception) {
        }

        return 0.0
    }

    /**
     * 字符串转布尔
     *
     * @param b
     * @return 转换异常返回 false
     */
    fun toBool(b: String): Boolean {
        try {
            return java.lang.Boolean.parseBoolean(b)
        } catch (e: Exception) {
        }

        return false
    }

    /**
     * 判断一个字符串是不是数字
     */
    fun isNumber(str: CharSequence): Boolean {
        val p = Pattern.compile("[0-9]*")
        val m = p.matcher(str)
        return if (m.matches()) {
            true
        } else false
    }

    /**
     * byte[]数组转换为16进制的字符串。
     *
     * @param data
     * 要转换的字节数组。
     * @return 转换后的结果。
     */
    fun byteArrayToHexString(data: ByteArray): String {
        val sb = StringBuilder(data.size * 2)
        for (b in data) {
            val v = b and 0xff.toByte()
            if (v < 16) {
                sb.append('0')
            }
            sb.append(Integer.toHexString(v.toInt()))
        }
        return sb.toString()
                .toUpperCase(Locale.getDefault())
    }

    /**
     * 16进制表示的字符串转换为字节数组。
     *
     * @param s
     * 16进制表示的字符串
     * @return byte[] 字节数组
     */
    fun hexStringToByteArray(s: String): ByteArray {
        val len = s.length
        val d = ByteArray(len / 2)
        var i = 0
        while (i < len) {
            // 两位一组，表示一个字节,把这样表示的16进制字符串，还原成一个进制字节
            d[i / 2] = ((Character.digit(s[i],
                    16) shl 4) + Character.digit(s[i + 1], 16)).toByte()
            i += 2
        }
        return d
    }

    private val dateFormater = object : ThreadLocal<SimpleDateFormat>() {
        override fun initialValue(): SimpleDateFormat {
            return SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        }
    }

    private val dateFormater2 = object : ThreadLocal<SimpleDateFormat>() {
        override fun initialValue(): SimpleDateFormat {
            return SimpleDateFormat("yyyy-MM-dd")
        }
    }

    /**
     * 以友好的方式显示时间
     *
     * @param sdate
     * @return
     */
    fun friendlyTime(sdate: String): String {
        var time: Date?

        if (isInEasternEightZones()) {
            time = toDate(sdate)
        } else {
            time = transformTime(toDate(sdate),
                    TimeZone.getTimeZone("GMT+08"),
                    TimeZone.getDefault())
        }

        if (time == null) {
            return "Unknown"
        }
        var ftime: String
        val cal = Calendar.getInstance()

        // 判断是否是同一天
        val curDate = dateFormater2.get()
                .format(cal.time)
        val paramDate = dateFormater2.get()
                .format(time)
        if (curDate == paramDate) {
            val hour = ((cal.timeInMillis - time.time) / 3600000).toInt()
            if (hour == 0) {
                ftime = Math.max((cal.timeInMillis - time.time) / 60000, 1).toString() + "分钟前"
            } else {
                ftime = hour.toString() + "小时前"
            }
            return ftime
        }

        val lt = time.time / 86400000
        val ct = cal.timeInMillis / 86400000
        val days = (ct - lt).toInt()
        if (days == 0) {
            val hour = ((cal.timeInMillis - time.time) / 3600000).toInt()
            if (hour == 0) {
                ftime = Math.max((cal.timeInMillis - time.time) / 60000, 1).toString() + "分钟前"
            } else {
                ftime = hour.toString() + "小时前"
            }
        } else if (days == 1) {
            ftime = "昨天"
        } else if (days == 2) {
            ftime = "前天 "
        } else if (days > 2 && days < 31) {
            ftime = days.toString() + "天前"
        } else if (days >= 31 && days <= 2 * 31) {
            ftime = "一个月前"
        } else if (days > 2 * 31 && days <= 3 * 31) {
            ftime = "2个月前"
        } else if (days > 3 * 31 && days <= 4 * 31) {
            ftime = "3个月前"
        } else {
            ftime = dateFormater2.get()
                    .format(time)
        }
        return ftime
    }

    /**
     * 将字符串转位日期类型
     *
     * @param sdate
     * @return
     */
    fun toDate(sdate: String): Date? {
        return toDate(sdate, dateFormater.get())
    }

    fun toDate(sdate: String, dateFormater: SimpleDateFormat): Date? {
        try {
            return dateFormater.parse(sdate)
        } catch (e: ParseException) {
            return null
        }

    }

    /**
     * 判断用户的设备时区是否为东八区（中国） 2014年7月31日
     *
     * @return
     */
    fun isInEasternEightZones(): Boolean {
        var defaultVaule: Boolean
        if (TimeZone.getDefault() === TimeZone.getTimeZone("GMT+08")) {
            defaultVaule = true
        } else {
            defaultVaule = false
        }
        return defaultVaule
    }

    /**
     * 根据不同时区，转换时间 2014年7月31日
     */
    fun transformTime(date: Date?, oldZone: TimeZone, newZone: TimeZone): Date? {
        var finalDate: Date? = null
        if (date != null) {
            val timeOffset = oldZone.getOffset(date.time) - newZone.getOffset(date.time)
            finalDate = Date(date.time - timeOffset)
        }
        return finalDate
    }

    /**
     * 字符串空值判断
     *
     * @param str 字符串
     * @return true 空 false 非空
     */
    fun isEmpty(str: String): Boolean {
        return str.trim { it <= ' ' }
                .length == 0 || str.toString()
                .trim { it <= ' ' }
                .equals("null", ignoreCase = true)
    }

    /**
     * 字符串非空判断
     *
     * @param str 字符串
     * @return true 非空 false 空
     */
    fun isNotEmpty(str: String): Boolean {
        return !isEmpty(str)
    }

    /**
     * 百分比格式化数字
     *
     * @param n
     * @return
     */
    fun fromtPercent(n: Double): String {
        //获取常规数值格式
        val numFormat = NumberFormat.getPercentInstance()
        numFormat.maximumIntegerDigits = 3 //设置数的 整数 部分所允许的最小位数(如果不足后面补0)
        numFormat.maximumFractionDigits = 2//设置数的 小数 部分所允许的最大位数(如果超过会四舍五入)
        return numFormat.format(n)
    }

    /**
     * 货币
     *
     * @param n
     * @return
     */
    fun fromtCurrency(n: Double): String {
        //获取常规数值格式
        val numFormat = NumberFormat.getCurrencyInstance()//NumberFormat.getCurrencyInstance(Locale.US);
        return numFormat.format(n)
    }

    /**
     * 手机号码屏蔽处理
     *
     * @param mobile 手机号
     * @return String 例 183****4036
     */
    fun starMobile(mobile: String): String {
        return if (mobile.length == 11) {
            mobile[0] + mobile[1].toString() + mobile[2] + "****" + mobile[7] + mobile[8] + mobile[9] + mobile[10]
        } else {
            mobile
        }
    }

    /**
     * 邮箱屏蔽处理
     *
     * @param email 邮箱
     * @return String
     */
    fun handleEmail(email: String?): String {
        var email = email
        if (email == null) {
            return ""
        } else {
            val aryEmail = email.split("@".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            if (aryEmail.size == 2) {
                val firstPart = aryEmail[0].substring(aryEmail[0].length / 2,
                        aryEmail[0].length)
                if ("" != firstPart) {
                    val repeatChar = CharArray(firstPart.length)

                    for (i in 0 until firstPart.length) {
                        repeatChar[i] = 42.toChar()
                    }

                    email = email.replaceFirst((firstPart + "@").toRegex(), String(repeatChar) + "@")
                }
            }

            return email
        }
    }

    /**
     * 获取指定长度的随机字符串
     *
     * @param strLength 指定长度
     * @return string
     */
    fun getRandomString(strLength: Int): String {
        val buffer = StringBuffer()
        val random = Random()

        for (i in 0 until strLength) {
            var charInt: Int
            val c: Char
            if (random.nextBoolean()) {
                charInt = 48 + random.nextInt(10)
                c = charInt.toChar()
                buffer.append(c)
            } else {
                if (random.nextBoolean()) {
                    charInt = 65 + random.nextInt(26)
                } else {
                    charInt = 97 + random.nextInt(26)
                }

                if (charInt == 79) {
                    charInt = 111
                }

                c = charInt.toChar()
                buffer.append(c)
            }
        }

        return buffer.toString()
    }

    /**
     * 获取指定长度的随机数字字符串
     *
     * @param n 指定长度
     * @return string
     */
    fun getRandNumStr(n: Int): String {
        val random = Random()
        var sRand = ""

        for (i in 0 until n) {
            val rand = random.nextInt(10).toString()
            sRand = sRand + rand
        }

        return sRand
    }

    /**
     * 获取系统随机
     *
     * @param count 指定长度
     * @return string
     */
    fun getSysTimeRandom(count: Int): String {
        val resultRandom = System.currentTimeMillis().toString() + "" + Random().nextInt(100)
        var resultRandomPro = ""
        val resultCount = resultRandom.length
        if (count < resultCount) {
            return resultRandom.substring(resultCount - 1 - count, resultCount - 1)
        } else {
            for (i in 0 until count - resultCount) {
                resultRandomPro = resultRandomPro + "0"
            }

            return resultRandomPro + resultRandom
        }
    }

    /**
     * 字符串相等判断
     *
     * @param str1 字符串1
     * @param str2 字符串2
     * @return true 相等 false 不相等
     */
    fun equals(str1: String?, str2: String?): Boolean {
        return if (str1 != null && str1 != "" && str2 != null && str2 != "")
            str1 == str2
        else
            false
    }

    /**
     * 字符串转int
     *
     * @param str 字符串
     * @return int
     */
    fun toInt(str: String?): Int {
        if (str != null && str != "") {
            var value1: Int
            try {
                value1 = Integer.parseInt(str)
            } catch (var3: Exception) {
                var3.printStackTrace()
                value1 = 0
            }

            return value1
        } else {
            return 0
        }
    }

    /**
     * 数组转字符串
     *
     * @param array 数组 例 {"123","456","789"}
     * @param split 分隔符 例 “|”
     * @return string 例 123|456|789
     */
    fun arrayToString(array: Array<Any>?, split: String): String {
        if (array == null) {
            return ""
        } else {
            val str = StringBuffer("")

            for (i in array.indices) {
                if (i != array.size - 1) {
                    str.append(array[i].toString())
                            .append(split)
                } else {
                    str.append(array[i].toString())
                }
            }

            return str.toString()
        }
    }

    /**
     * 字符串转文件大小
     *
     * @param fileSize
     * @return string
     */
    fun getFileSize(fileSize: String): String {
        var temp: String
        val df = DecimalFormat("0.00")
        val dbFileSize = java.lang.Double.parseDouble(fileSize)
        if (dbFileSize >= 1024.0) {
            if (dbFileSize >= 1048576.0) {
                if (dbFileSize >= 1.073741824E9) {
                    temp = df.format(dbFileSize / 1024.0 / 1024.0 / 1024.0) + " GB"
                } else {
                    temp = df.format(dbFileSize / 1024.0 / 1024.0) + " MB"
                }
            } else {
                temp = df.format(dbFileSize / 1024.0) + " KB"
            }
        } else {
            temp = df.format(dbFileSize / 1024.0) + " KB"
        }

        return temp
    }

    /**
     * 转utf-8 编码
     *
     * @param str 字符串
     * @return string
     */
    fun toUTF8(str: String): String {
        return to(str, "UTF-8")
    }

    /**
     * 字符串转指定编码
     *
     * @param str     字符串
     * @param charset 编码格式
     * @return string
     */
    fun to(str: String?, charset: String): String {
        return if (str != null && str != "") {
            try {
                java.lang.String(str.toByteArray(charset("ISO8859-1")), charset)
            } catch (var3: Exception) {
                var3.printStackTrace()
                ""
            } as String

        } else {
            ""
        }
    }

    /**
     * 数字转中文
     *
     * @param num 数字
     * @return string
     */
    fun getChineseNum(num: Int): String {
        val chineseNum = arrayOf("零", "壹", "贰", "叁", "肆", "伍", "陆", "柒", "捌", "玖")
        return chineseNum[num]
    }

    /**
     * 去除回车换行字符
     *
     * @param str 字符串
     * @return string
     */
    fun replaceEnter(str: String?): String? {
        return str?.replace("\r".toRegex(), "")?.replace("\n".toRegex(), "")
    }

    /**
     * 去除空格
     *
     * @param strtrim
     * @return
     */
    fun toTrim(strtrim: String?): String {
        return if (null != strtrim && strtrim != "")
            strtrim.trim { it <= ' ' }
        else
            ""
    }

    /**
     * 获取html中的内容（去除html标签）
     *
     * @param element html字符串
     * @return string
     */
    fun getTxtWithoutHTMLElement(element: String?): String? {
        if (null == element) {
            return element
        } else {
            val pattern = Pattern.compile("<[^<|^>]*>")
            val matcher = pattern.matcher(element)
            val txt = StringBuffer()

            var temp: String
            while (matcher.find()) {
                temp = matcher.group()
                if (temp.matches("<[\\s]*>".toRegex())) {
                    matcher.appendReplacement(txt, temp)
                } else {
                    matcher.appendReplacement(txt, "")
                }
            }

            matcher.appendTail(txt)
            temp = txt.toString()
                    .replace("[\r|\n]".toRegex(), "")
            temp = temp.replace("\\s+".toRegex(), " ")
            return temp
        }
    }

    fun createUUID(): String {
        val uuid = UUID.randomUUID()
        return uuid.toString()
    }

    /**
     * 截取添加追加字符
     *
     * @param sourceStr 源字符串
     * @param length    截取的位置
     * @param charactor 追加的字符
     * @return
     */
    fun cutffStr(sourceStr: String?, length: Int, charactor: String): String? {
        var resultStr = sourceStr
        if (sourceStr != null && "" != sourceStr) {
            if (sourceStr.length > length) {
                resultStr = sourceStr.substring(0, length)
                resultStr = resultStr + charactor
            }

            return resultStr
        } else {
            return ""
        }
    }

    /**
     * 截串，超过长度 省略
     *
     * @param goodsName
     * @param length
     * @return
     */
    fun getLength(goodsName: Any?, length: Int): String? {
        if (goodsName == null) {
            return null
        } else {
            var temp = goodsName.toString()
            if (temp.length <= length) {
                return temp
            } else {
                temp = temp.substring(0, length) + "..."
                return temp
            }
        }
    }

    /**
     * 校验器:利用正则表达式校验邮箱、手机号等
     * @author Mr.duan
     */
//  =========================================================================
    /**
     * 正则表达式:验证用户名(不包含中文和特殊字符)如果用户名使用手机号码或邮箱 则结合手机号验证和邮箱验证
     */
    val REGEX_USERNAME = "^[a-zA-Z]\\w{5,17}$"

    /**
     * 正则表达式:验证密码(不包含特殊字符)
     */
    val REGEX_PASSWORD = "^[a-zA-Z0-9]{6,16}$"

    /**
     * 正则表达式:验证手机号
     */
    val REGEX_MOBILE = "^(0|86|17951)?(13[0-9]|15[012356789]|17[678]|18[0-9]|14[57])[0-9]{8}$"

    /**
     * 正则表达式:验证邮箱
     */
    val REGEX_EMAIL = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$"

    /**
     * 正则表达式:验证汉字(1-4个汉字)  {1,8} 自定义区间
     */
    val REGEX_CHINESE = "^[\\u4e00-\\u9fa5]{1,8}$"

    /**
     * 正则表达式:验证身份证
     */
    val REGEX_ID_CARD = "(^\\d{15}$)|(^\\d{17}([0-9]|X)$)"

    /**
     * 正则表达式:验证URL
     */
    val REGEX_URL = "http(s)?://([\\w-]+\\.)+[\\w-]+(/[\\w- ./?%&=]*)?"

    /**
     * 正则表达式:验证IP地址
     */
    val REGEX_IP_ADDR = "(2[5][0-5]|2[0-4]\\d|1\\d{2}|\\d{1,2})\\.(25[0-5]|2[0-4]\\d|1\\d{2}|\\d{1,2})\\.(25[0-5]|2[0-4]\\d|1\\d{2}|\\d{1,2})\\.(25[0-5]|2[0-4]\\d|1\\d{2}|\\d{1,2})"
    //  =========================================================================
    /**
     * 校验用户名
     * @param username
     * @return 校验通过返回true，否则返回false
     */
    fun isUserName(username: String): Boolean {
        return Pattern.matches(REGEX_USERNAME, username)
    }

    /**
     * 校验密码
     * @param password
     * @return 校验通过返回true，否则返回false
     */
    fun isPassword(password: String): Boolean {
        return Pattern.matches(REGEX_PASSWORD, password)
    }

    /**
     * 校验手机号
     * @param mobile
     * @return 校验通过返回true，否则返回false
     */
    fun isMobile(mobile: String): Boolean {
        return Pattern.matches(REGEX_MOBILE, mobile)
    }

    /**
     * 校验邮箱
     * @param email
     * @return 校验通过返回true，否则返回false
     */
    fun isEmail(email: String): Boolean {
        return Pattern.matches(REGEX_EMAIL, email)
    }

    /**
     * 校验汉字
     * @param chinese
     * @return 校验通过返回true，否则返回false
     */
    fun isChinese(chinese: String): Boolean {
        return Pattern.matches(REGEX_CHINESE, chinese)
    }

    /**
     * 自定义校验汉字
     * @param chinese
     * @return 校验通过返回true，否则返回false
     */
    fun isChinese(chinese: String, regex: String): Boolean {
        return Pattern.matches(regex, chinese)
    }

    /**
     * 校验身份证
     * @param idCard
     * @return 校验通过返回true，否则返回false
     */
    fun isIDCard(idCard: String): Boolean {
        return Pattern.matches(REGEX_ID_CARD, idCard)
    }

    /**
     * 校验URL
     * @param url
     * @return 校验通过返回true，否则返回false
     */
    fun isUrl(url: String): Boolean {
        return Pattern.matches(REGEX_URL, url)
    }

    /**
     * 校验IP地址
     * @param ipAddress
     * @return
     */
    fun isIPAddress(ipAddress: String): Boolean {
        return Pattern.matches(REGEX_IP_ADDR, ipAddress)
    }
}