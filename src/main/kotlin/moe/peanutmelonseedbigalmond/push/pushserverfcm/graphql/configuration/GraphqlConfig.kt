package moe.peanutmelonseedbigalmond.push.pushserverfcm.graphql.configuration

import graphql.kickstart.tools.SchemaParserDictionary
import moe.peanutmelonseedbigalmond.push.pushserverfcm.graphql.bean.TopicItem
import moe.peanutmelonseedbigalmond.push.pushserverfcm.graphql.bean.TopicItemWithCursor
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary

@Configuration
class GraphqlConfig {
    @Bean
    @Primary
    fun schemaParserDictionary(): SchemaParserDictionary? {
        val dictionary = SchemaParserDictionary()
        dictionary.add("TopicItem", TopicItem::class)
        dictionary.add("TopicItemWithCursor", TopicItemWithCursor::class)
        return dictionary
    }
}