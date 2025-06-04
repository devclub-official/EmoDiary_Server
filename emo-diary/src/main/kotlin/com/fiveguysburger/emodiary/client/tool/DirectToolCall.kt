package com.fiveguysburger.emodiary.mcp.client.tool

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.ai.mcp.SyncMcpToolCallbackProvider
import org.springframework.stereotype.Component

@Component
class DirectToolCall(
    private val toolCallbackProvider: SyncMcpToolCallbackProvider,
    private val objectMapper: ObjectMapper,
) {
    /**
     * 문서 삽입 (단일) - Spring AI 표준 타입 사용
     */
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

        return callTool("insert-document", params)
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
                "limit" to limit.toString(),
                "projection" to projection?.let { objectMapper.writeValueAsString(it) },
                "sort" to sort?.let { objectMapper.writeValueAsString(it) },
            )

        return callTool("find-documents", params)
    }

    /**
     * 문서 업데이트
     */
    fun updateDocument(
        collection: String,
        filter: Map<String, Any>,
        update: Map<String, Any>,
        options: Map<String, Any>? = null,
    ): String {
        val params =
            buildParams(
                "collection" to collection,
                "filter" to objectMapper.writeValueAsString(filter),
                "update" to objectMapper.writeValueAsString(update),
                "options" to options?.let { objectMapper.writeValueAsString(it) },
            )

        return callTool("update-document", params)
    }

    /**
     * 문서 삭제
     */
    fun deleteDocument(
        collection: String,
        filter: Map<String, Any>,
    ): String {
        val params =
            buildParams(
                "collection" to collection,
                "filter" to objectMapper.writeValueAsString(filter),
            )

        return callTool("delete-document", params)
    }

    /**
     * 최대한 간소화된 도구 호출
     */
    private fun callTool(
        toolName: String,
        params: Map<String, Any>,
    ): String {
        val toolCallback =
            toolCallbackProvider.getToolCallbacks()
                .find { it.toolDefinition.name() == toolName }
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
