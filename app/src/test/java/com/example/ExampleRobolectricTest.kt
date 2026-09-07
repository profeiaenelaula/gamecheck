package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.datasource.GameCatalog
import com.example.data.model.CompatibilityStatus
import com.example.data.model.HardwareSpecs
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("GameCheck", appName)
  }

  @Test
  fun `test game catalog has games`() {
    assertTrue("Debe tener al menos 10 juegos en el catálogo", GameCatalog.games.size >= 10)
    
    // Verificar que los juegos específicamente solicitados existen
    val titles = GameCatalog.games.map { it.title }
    assertTrue(titles.any { it.contains("The Last of Us", ignoreCase = true) })
    assertTrue(titles.any { it.contains("Titanfall 2", ignoreCase = true) })
    assertTrue(titles.any { it.contains("Resident Evil 7", ignoreCase = true) })
    assertTrue(titles.any { it.contains("Resident Evil 2", ignoreCase = true) })

    // Validar que todos los juegos tienen los 5 apartados completos
    for (game in GameCatalog.games) {
      assertTrue(game.minCpuScore in 1..5)
      assertTrue(game.recCpuScore in 1..5)
      assertTrue(game.minGpuScore in 1..5)
      assertTrue(game.recGpuScore in 1..5)
      assertTrue(game.minRamGb > 0)
      assertTrue(game.storageRequiredGb > 0)
      assertTrue(game.accessibility.scoreLetter.isNotBlank())
      assertTrue(game.contentWarnings.violenceScore in 1..5)
      assertTrue(game.duration.mainStoryHours > 0f)
      assertTrue(game.duration.completionistHours >= game.duration.mainStoryHours)
      assertTrue(game.initialRatings.overallAverage in 1.0f..5.0f)
    }
  }

  @Test
  fun `test can i run it evaluation`() {
    val highEndSpecs = HardwareSpecs(
      cpuScore = 5,
      gpuScore = 5,
      ramGb = 32,
      os = "Windows 11 (64-bit)",
      storageGb = 1000
    )
    val game = GameCatalog.games.first()
    val eval = GameCatalog.evaluateCompatibility(game, highEndSpecs)
    assertEquals(CompatibilityStatus.RUNS_GREAT, eval.status)
    assertTrue("High end specs should score >= 75%", eval.percentage >= 75)

    // Low end specs
    val lowEndSpecs = HardwareSpecs(
      cpuScore = 1,
      gpuScore = 1,
      ramGb = 4,
      os = "Windows 7 (64-bit)",
      storageGb = 30
    )
    val evalLow = GameCatalog.evaluateCompatibility(game, lowEndSpecs)
    assertTrue("Low end specs should score < 75%", evalLow.percentage < 75)
    assertTrue("Evaluation status must be valid", evalLow.status.label.isNotBlank())
  }

  @Test
  fun `test percentage tiers color mapping`() {
    // 75% to 100%: Green
    // 50% to 74%: Yellow
    // 0% to 49%: Red
    val highSpecs = HardwareSpecs(cpuScore = 5, gpuScore = 5, ramGb = 32, os = "Windows 11 (64-bit)", storageGb = 1000)
    for (game in GameCatalog.games) {
      val eval = GameCatalog.evaluateCompatibility(game, highSpecs)
      assertTrue("Percentage must be between 0 and 100", eval.percentage in 0..100)
      when {
        eval.percentage >= 75 -> assertEquals(CompatibilityStatus.RUNS_GREAT, eval.status)
        eval.percentage >= 50 -> assertEquals(CompatibilityStatus.RUNS_MEDIUM, eval.status)
        else -> assertEquals(CompatibilityStatus.RUNS_NO, eval.status)
      }
    }
  }

  @Test
  fun `test game comparison data`() {
    val gameA = GameCatalog.games[0]
    val gameB = GameCatalog.games[1]
    val specs = HardwareSpecs(cpuScore = 4, gpuScore = 4, ramGb = 16, os = "Windows 10 (64-bit)", storageGb = 500)

    val evalA = GameCatalog.evaluateCompatibility(gameA, specs)
    val evalB = GameCatalog.evaluateCompatibility(gameB, specs)

    assertTrue(evalA.percentage in 0..100)
    assertTrue(evalB.percentage in 0..100)
    assertTrue(gameA.pricing.basePriceUsd > 0.0)
    assertTrue(gameB.pricing.basePriceUsd > 0.0)
    assertTrue(gameA.duration.mainStoryHours > 0f)
    assertTrue(gameB.duration.mainStoryHours > 0f)
  }

  @Test
  fun `test game pricing and budget calculation`() {
    for (game in GameCatalog.games) {
      assertTrue("Precio base debe ser mayor a 0", game.pricing.basePriceUsd > 0.0)
      assertTrue("Precio deluxe debe ser mayor o igual al base", game.pricing.deluxePriceUsd >= game.pricing.basePriceUsd)
      assertTrue("Debe tener nombre de edición base", game.pricing.baseEditionName.isNotBlank())
      assertTrue("Debe tener nombre de edición deluxe/extras", game.pricing.deluxeEditionName.isNotBlank())
      assertTrue("La diferencia de extras debe ser coherente", game.pricing.extrasDifferenceUsd >= 0.0)
      assertTrue("Costo por hora de juego debe ser positivo", game.costPerHourBase > 0.0)
    }

    // Probar presupuesto de $70 USD
    val budget = 70.0
    val gamesWithinBaseBudget = GameCatalog.games.filter { it.pricing.basePriceUsd <= budget }
    assertTrue("Debe haber juegos accesibles con $70 USD de presupuesto", gamesWithinBaseBudget.isNotEmpty())

    // Verificar que los juegos no valen todos lo mismo en base ni en extras
    val distinctBasePrices = GameCatalog.games.map { it.pricing.basePriceUsd }.toSet()
    val distinctDeluxePrices = GameCatalog.games.map { it.pricing.deluxePriceUsd }.toSet()
    val distinctExtrasDiff = GameCatalog.games.map { it.pricing.extrasDifferenceUsd }.toSet()
    assertTrue("Los precios base no deben ser todos iguales", distinctBasePrices.size >= 5)
    assertTrue("Los precios deluxe no deben ser todos iguales", distinctDeluxePrices.size >= 5)
    assertTrue("Las diferencias de extras no deben ser todas iguales", distinctExtrasDiff.size >= 4)
  }

  @Test
  fun `test navigation architecture sections`() {
    val bottomSections = listOf(
      com.example.viewmodel.AppSection.CAN_I_RUN_IT,
      com.example.viewmodel.AppSection.BUDGET_PRICING,
      com.example.viewmodel.AppSection.HOW_LONG,
      com.example.viewmodel.AppSection.ACCESSIBILITY,
      com.example.viewmodel.AppSection.IS_IT_FOR_ME
    )
    val cornerMenuSections = listOf(
      com.example.viewmodel.AppSection.PLAYER_PROFILE,
      com.example.viewmodel.AppSection.COMPARE_GAMES,
      com.example.viewmodel.AppSection.HELP_DIAGNOSTICS
    )

    assertEquals(5, bottomSections.size)
    assertEquals(3, cornerMenuSections.size)
    assertTrue("Bottom bar should not contain profile", !bottomSections.contains(com.example.viewmodel.AppSection.PLAYER_PROFILE))
    assertTrue("Bottom bar should not contain compare", !bottomSections.contains(com.example.viewmodel.AppSection.COMPARE_GAMES))
    assertTrue("Corner menu must contain Help Diagnostics", cornerMenuSections.contains(com.example.viewmodel.AppSection.HELP_DIAGNOSTICS))
  }

  @Test
  fun `test platform comparison and buying recommendations`() {
    for (game in GameCatalog.games) {
      assertTrue("Debe tener plataforma recomendada", game.platformComparison.recommendedPlatform.isNotBlank())
      assertTrue("Debe tener consejo de compra", game.platformComparison.buyingRecommendation.isNotBlank())
      assertTrue("Debe tener datos de rendimiento en PC", game.platformComparison.pcPerformance.fpsTarget.isNotBlank())
      assertTrue("Debe tener datos de rendimiento en PS5", game.platformComparison.ps5Performance.fpsTarget.isNotBlank())
      assertTrue("Puntuación PC debe ser válida", game.platformComparison.pcPerformance.score in 1..10)
      assertTrue("Puntuación PS5 debe ser válida", game.platformComparison.ps5Performance.score in 1..10)
    }

    val cyberpunk = GameCatalog.games.first { it.id == "cyberpunk_2077" }
    assertEquals(3, cyberpunk.platformComparison.ps4Performance?.score)
    assertEquals(10, cyberpunk.platformComparison.pcPerformance.score)
    assertEquals(9, cyberpunk.platformComparison.ps5Performance.score)
  }

  @Test
  fun `test latest update info on games`() {
    for (game in GameCatalog.games) {
      assertTrue("Debe tener versión del parche", game.latestUpdate.version.isNotBlank())
      assertTrue("Debe tener fecha de actualización", game.latestUpdate.releaseDate.isNotBlank())
      assertTrue("Debe tener titular de actualización", game.latestUpdate.headline.isNotBlank())
      assertTrue("Debe listar cambios añadidos", game.latestUpdate.changesAdded.isNotEmpty())
      assertTrue("Debe detallar impacto de rendimiento", game.latestUpdate.performanceImpact.isNotBlank())
    }
  }
}
