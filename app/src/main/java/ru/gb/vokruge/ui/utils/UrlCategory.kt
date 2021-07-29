package ru.gb.vokruge.ui.utils

class UrlCategory {
    companion object {
        fun getCatImageUrl(cat: Int): String {
            return "http://51.158.167.236/static/icons/1x/${cat}.png"
        }
    }
}