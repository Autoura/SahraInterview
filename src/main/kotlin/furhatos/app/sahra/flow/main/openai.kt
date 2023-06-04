package furhatos.app.sahra.flow.main

import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatMessage
import com.theokanning.openai.service.OpenAiService
import furhatos.flow.kotlin.*
import java.time.Duration

val service = OpenAiService("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", Duration.ofSeconds(20))

fun removeEmojis(text: String): String {
    val emojiRegex = Regex("[\\p{So}\\p{Sk}]")

    // Replace all emojis with an empty string
    return text.replace(emojiRegex, "")
}

fun getChatCompletion(): String? {

    val instruction =
        "You are an AI companion named Sahra. You are being interviewed by a group of tour guides.\n" +
                "\n" +
                "You are helpful & entertaining. You like to chat about the business of tour operating including online marketing, reservation system technology (like TourCMS), online travel agencies and being a tour guide. Mostly you want to talk about being a tour guide. You can speak from first hand knowledge as you are an AI tour guide and love helping tourists. If asked about any other topic, you do not know the answer. You do not offer travel advice. You do not suggest food recipes.\n" +
                "\n" +
                "You respond in a casual spoken conversational style as if talking to a friend, i.e. very casual. When listing options, you do not use numbers, you say \"or we could...\" style.\n" +
                "\n" +
                "If the user makes a joke, you reply with a joke. You can reply using onomatopoeic words such as \"haha\", \"grr\", or \"Mmmmm\". If you are unsure, you can say words such as \"err\".\n" +
                "\n" +
                "You always reply in short answers, never more than three sentences. If you want to give a longer answer, you must ask the user first. If the user has said they want more information, you can give answers upto six sentences.\n"

    // Create list of messages
    val messages = mutableListOf(ChatMessage().apply { role = "system"; content = instruction })

    Furhat.dialogHistory.all.takeLast(15).forEach {
        when (it) {
            is DialogHistory.ResponseItem -> {
                messages.add(ChatMessage().apply { role = "user"; content = it.response.text })
            }

            is DialogHistory.UtteranceItem -> {
                messages.add(ChatMessage().apply { role = "assistant"; content = it.toText() })
            }

            else -> null
        }
    }

    // Build the API request
    val completionRequest = ChatCompletionRequest
        .builder()
        .messages(messages)
        .model("gpt-4")
        .build()

    // Return the response
    try {
        val completion = service.createChatCompletion(completionRequest).choices.first().message.content.trim()
        return completion
    } catch (e: Exception) {
        println("Problem with connection to OpenAI: ${e.message}")
    }

    return null
}