import com.fiveguysburger.emodiary.core.entity.Users
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime

@Entity
@Table(name = "emotional_analysis_results")
data class EmotionalAnalysisResults(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    var id: Int? = null,
    @Column(name = "user_id", nullable = false)
    var userId: Int,
    @Column(name = "start_date", nullable = false)
    var startDate: LocalDate,
    @Column(name = "end_date", nullable = false)
    var endDate: LocalDate,
    @Column(name = "sentiment_score")
    var sentimentScore: BigDecimal? = null,
    @Column(name = "key_emotions", length = 255)
    var keyEmotions: String? = null,
    @Column(name = "summary", columnDefinition = "TEXT")
    var summary: String? = null,
    @Column(name = "analysis_timestamp", nullable = false)
    var analysisTimestamp: LocalDateTime = LocalDateTime.now(),
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id", insertable = false, updatable = false)
    var user: Users? = null,
)
