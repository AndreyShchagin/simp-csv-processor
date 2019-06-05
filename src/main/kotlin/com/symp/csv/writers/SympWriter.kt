package com.symp.csv.writers

interface SympWriter<T, V> {
    fun writeReport(uniqueUsersMap: Map<String, MutableList<T>>, point5: V)

}
