package com.renameyourappname.mobile.utils

import android.text.Editable
import android.text.Selection
import android.text.TextWatcher
import android.widget.EditText


object EditTextUtil {
    /**
     * 验卷，每四位自动跟横线
     * @param mEditText
     */
    fun bankCardFormat(mEditText: EditText) {
        mEditText.addTextChangedListener(object : TextWatcher {
            internal var beforeTextLength = 0
            internal var onTextLength = 0
            internal var isChanged = false
            var delimiter = ' '

            internal var location = 0//记录光标的位置
            private var tempChar: CharArray? = null
            private val buffer = StringBuffer()
            internal var konggeNumberB = 0

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                beforeTextLength = s.length
                if (buffer.length > 0) {
                    buffer.delete(0, buffer.length)
                }
                konggeNumberB = 0
                for (i in 0 until s.length) {
                    if (s[i] == delimiter) {
                        konggeNumberB++
                    }
                }
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                onTextLength = s.length
                buffer.append(s.toString())
                if (onTextLength == beforeTextLength || onTextLength <= 3 || isChanged) {
                    isChanged = false
                    return
                }
                isChanged = true
            }

            override fun afterTextChanged(s: Editable) {
                if (isChanged) {
                    location = mEditText.selectionEnd
                    var index = 0
                    while (index < buffer.length) {
                        if (buffer[index] == delimiter) {
                            buffer.deleteCharAt(index)
                        } else {
                            index++
                        }
                    }

                    index = 0
                    var konggeNumberC = 0
                    while (index < buffer.length) {
                        //银行卡号的话需要改这里
                        if (index == 4 || index == 9 || index == 14 || index == 19) {
                            buffer.insert(index, delimiter)
                            konggeNumberC++
                        }
                        index++
                    }

                    if (konggeNumberC > konggeNumberB) {
                        location += konggeNumberC - konggeNumberB
                    }

                    tempChar = CharArray(buffer.length)
                    buffer.getChars(0, buffer.length, tempChar, 0)
                    val str = buffer.toString()
                    if (location > str.length) {
                        location = str.length
                    } else if (location < 0) {
                        location = 0
                    }

                    mEditText.setText(str)
                    val etable = mEditText.text
                    Selection.setSelection(etable, location)
                    isChanged = false
                }
            }
        })
    }

}