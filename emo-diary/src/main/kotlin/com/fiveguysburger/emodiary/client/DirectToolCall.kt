package com.fiveguysburger.emodiary.mcp.client.tool

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.ai.mcp.SyncMcpToolCallbackProvider
import org.springframework.ai.tool.annotation.Tool
import org.springframework.stereotype.Component

@Component
class DirectToolCall(
    private val toolCallbackProvider: SyncMcpToolCallbackProvider,
    private val objectMapper: ObjectMapper,
) {
    /**
     * 문서 삽입 (단일) - Spring AI 표준 타입 사용
     */
    @Tool
    fun insertDocument(
        collection: String,
        document: Map<String, Any>,
        options: Map<String, Any>? = null,
    ): String {
        val params =
            buildParams(
                "collection" to collection,
                "document" to objectMapper.writeValueAsString(document),
                "options" to options?.let { objectMapper.writeValueAsString(it) },
            )

        return callTool("spring_ai_mcp_client_mongodb_lens_insert_document", params)
    }

    /**
     * 문서 조회 - Spring AI 표준 타입 사용
     */
    fun findDocuments(
        collection: String,
        filter: Map<String, Any> = emptyMap(),
        limit: Int = 10,
        projection: Map<String, Any>? = null,
        sort: Map<String, Any>? = null,
    ): String {
        val params =
            buildParams(
                "collection" to collection,
                "filter" to objectMapper.writeValueAsString(filter),
                "limit" to limit,
                "projection" to projection?.let { objectMapper.writeValueAsString(it) },
                "sort" to sort?.let { objectMapper.writeValueAsString(it) },
            )

        return callTool("spring_ai_mcp_client_mongodb_lens_find_documents", params)
    }

    /**
     * 최대한 간소화된 도구 호출
     */
    private fun callTool(
        toolName: String,
        params: Map<String, Any>,
    ): String {
        val toolCallback =
            toolCallbackProvider.getToolCallbacks().find { it.toolDefinition.name() == toolName }
                ?: throw IllegalArgumentException("Tool '$toolName' not found")

        val jsonParams = objectMapper.writeValueAsString(params)

        return toolCallback.call(jsonParams)
    }

    /**
     * 매개변수 맵 빌더 (null 값 제외)
     */
    private fun buildParams(vararg pairs: Pair<String, Any?>): Map<String, Any> {
        return pairs.mapNotNull { (key, value) ->
            value?.let { key to it }
        }.toMap()
    }
}
