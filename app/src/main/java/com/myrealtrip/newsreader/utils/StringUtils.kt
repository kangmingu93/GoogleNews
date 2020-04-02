package com.myrealtrip.newsreader.utils

import android.util.Log
import java.util.*

class StringUtils {

    companion object {
        private val TAG= StringUtils::class.java.simpleName
        val instance = StringUtils()
    }

    // 문자열에서 빈도수 높은 키워드 3개 추출(동일 빈도수일 경우 오름차순)
    fun getKeywords(s: String): Array<String> {
        Log.i(TAG, "원문 : $s")
        var str = s.replaceSpecialCharacters() // 특수문자 제거
        Log.i(TAG, "특수문자 제거 : $str")
        str = str.replaceSpaces() // 무의미한 공백 제거
        Log.i(TAG, "공백 제거 : $str")

        val st = StringTokenizer(str, " ") // 공백 기준 문자열 자르기
        val map = mutableMapOf<String, Int>()
        while (st.hasMoreTokens()) {
            val token = st.nextToken()
            if (token.isNotEmpty()) {
                map[token] = if (map.containsKey(token)) map[token]!!+1 else 1 // 같은 키 값이 있는경우 +1 아닌경우 새로운 값을 입력
            }
        }

        Log.i(TAG, "Map : $map")
        val sorted = map.toList().sortedBy { it.first }.sortedByDescending { it.second }.toMap()
        Log.i(TAG, "Sorted: $sorted")

        val list = arrayListOf<String>()
        if (!sorted.isNullOrEmpty()) {
            val it = sorted.iterator()
            while (it.hasNext() && list.size < 3) {
                list.add(it.next().key)
            }
        }

        val array = arrayOfNulls<String>(list.size)
        return list.toArray(array)
    }

    // 특수문자를 단일 공백으로 치환
    private fun String.replaceSpecialCharacters() : String = this.replace("[^\uAC00-\uD7A3xfe0-9a-zA-Z]".toRegex(), " ")

    // 연속된 공백 및 줄바꿈, 탭을 단일 공백으로 치환
    private fun String.replaceSpaces() : String = this.replace("\\s+".toRegex(), " ")

    fun replaceSpace(s: String) : String {
        return s.replace("\\s+".toRegex(), " ")
    }

}