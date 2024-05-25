package moe.peanutmelonseedbigalmond.push.pushserverfcm.utils

import org.commonmark.parser.Parser
import org.commonmark.renderer.html.HtmlRenderer
import org.jsoup.Jsoup

object MarkdownUtil {
    private val parser = Parser.builder().build()
    fun getMarkdownExcerpt(markdown: String, maxLength: Int): String {
        val node = parser.parse(markdown)
        val renderer = HtmlRenderer.Builder().build()
        val html = renderer.render(node)
        return Jsoup.parse(html).wholeText().trim().take(maxLength)
    }
}