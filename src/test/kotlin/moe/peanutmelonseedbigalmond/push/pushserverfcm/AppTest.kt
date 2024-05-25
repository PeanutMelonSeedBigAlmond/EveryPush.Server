package moe.peanutmelonseedbigalmond.push.pushserverfcm

import moe.peanutmelonseedbigalmond.push.pushserverfcm.utils.MarkdownUtil


object AppTest {
    @JvmStatic
    fun main(args: Array<String>) {
        val content =
            """[![](https://reverse.loliconapp.top/pixiv?target=https%3A%2F%2Fi.pximg.net%2Fimg-master%2Fimg%2F2024%2F07%2F21%2F10%2F47%2F04%2F120720291_p0_master1200.jpg&sign=efd23a6c1fbbcbf835c06bfec6dc1f93)](https://reverse.loliconapp.top/pixiv?target=https%3A%2F%2Fi.pximg.net%2Fimg-original%2Fimg%2F2024%2F07%2F21%2F10%2F47%2F04%2F120720291_p0.jpg&sign=6660102d50cf488871b6dd48cb4c95ef)
うりぼうざっか店さまの画集『Underwears8 -UTOPIA-』の限定版セットが受注開始してます❣<br><a href="https://melonbooks.co.jp/detail/detail.php?product_id=2468084" target="_blank">https://melonbooks.co.jp/detail/detail.php?product_id=2468084</a><br>缶バッチやアクリルフィギュアかわいい！よろしくお願いします🐈🖤
by うさ城まに
2024年7月21日 09:47:00"""
        println(MarkdownUtil.getMarkdownExcerpt(content, 1000))
    }
}