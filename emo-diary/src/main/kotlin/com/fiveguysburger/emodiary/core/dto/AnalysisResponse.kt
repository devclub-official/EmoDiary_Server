package com.fiveguysburger.emodiary.core.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class AnalysisResponse(
    @JsonProperty("diary_analysis_text")
    val analysis : String?
)