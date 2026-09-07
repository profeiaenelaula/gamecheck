package com.example.data.datasource

import com.example.data.model.*

object GameCatalog {

    val cpuOptions = listOf(
        HardwareTier("cpu_1", "Intel Core i3-4130 / AMD FX-4300 (Básico / 2 Núcleos)", 1),
        HardwareTier("cpu_2", "Intel Core i5-7400 / AMD Ryzen 3 1200 (Gama Entrada)", 2),
        HardwareTier("cpu_3", "Intel Core i5-10400 / AMD Ryzen 5 3600 (Gama Media)", 3),
        HardwareTier("cpu_4", "Intel Core i7-12700K / AMD Ryzen 7 5800X3D (Gama Alta)", 4),
        HardwareTier("cpu_5", "Intel Core i9-14900K / AMD Ryzen 7 7800X3D (Extremo)", 5)
    )

    val gpuOptions = listOf(
        HardwareTier("gpu_1", "Gráficos Integrados (Intel UHD 630 / Radeon Vega 8)", 1),
        HardwareTier("gpu_2", "NVIDIA GTX 1050 Ti / AMD RX 560 (Gama Entrada)", 2),
        HardwareTier("gpu_3", "NVIDIA GTX 1660 Super / AMD RX 5600 XT (Gama Media)", 3),
        HardwareTier("gpu_4", "NVIDIA RTX 3070 / AMD RX 6700 XT (Gama Alta)", 4),
        HardwareTier("gpu_5", "NVIDIA RTX 4080 / 4090 / AMD RX 7900 XTX (Extrema)", 5)
    )

    val ramOptions = listOf(4, 8, 16, 32, 64)

    val osOptions = listOf(
        "Windows 11 (64-bit)",
        "Windows 10 (64-bit)",
        "Linux / SteamOS",
        "macOS"
    )

    val storageOptions = listOf(60, 120, 250, 500, 1000, 2000)

    val games: List<Game> = listOf(
        Game(
            id = "cyberpunk_2077",
            title = "Cyberpunk 2077: Phantom Liberty",
            tagline = "Sumérgete en el futurista bajo mundo de Night City con Ray Tracing",
            synopsis = "Un juego de rol, acción y aventura en mundo abierto ambientado en la megalópolis de Night City, donde te pondrás en la piel de V, un mercenario cibermejorado.",
            developer = "CD Projekt RED",
            publisher = "CD Projekt RED",
            releaseYear = 2023,
            genres = listOf("RPG", "Acción", "Ciencia Ficción", "Mundo Abierto"),
            bannerColorHex = 0xFFFEE715,
            coverEmoji = "🦾",
            minCpuScore = 3,
            recCpuScore = 4,
            minCpuText = "Core i7-6700 o Ryzen 5 1600",
            recCpuText = "Core i7-12700 o Ryzen 7 7800X",
            minGpuScore = 3,
            recGpuScore = 4,
            minGpuText = "GTX 1060 (6 GB) o RX 580 (8 GB)",
            recGpuText = "RTX 3070 (8 GB) o RX 6800 XT",
            minRamGb = 12,
            recRamGb = 16,
            supportedOs = listOf("Windows 10 (64-bit)", "Windows 11 (64-bit)", "Linux / SteamOS"),
            storageRequiredGb = 70,
            accessibility = AccessibilityFeatures(
                subtitleSizeAdjustable = true,
                subtitleBackgroundContrast = true,
                speakerIdentification = true,
                directionalSoundCues = true,
                fullSpanishAudioAndSub = true,
                fullButtonRemapping = true,
                toggleVsHoldOption = true,
                aimAssistAdjustable = true,
                simplifiedControlScheme = true,
                adaptiveControllerCompatible = true,
                colorblindFilters = listOf("Protanopia", "Deuteranopia", "Tritanopia"),
                highContrastMode = false,
                hudScaling = true,
                textToSpeechScreenReader = false,
                reduceMotionAndFlashes = true,
                scoreLetter = "A"
            ),
            contentWarnings = ContentWarnings(
                violenceLevel = "Alta / Brutal",
                violenceScore = 5,
                bloodGore = "Gore explícito y desmembramiento",
                bloodScore = 5,
                horrorLevel = "Tensión atmosférica",
                horrorScore = 3,
                strongLanguage = "Explícito y constante",
                languageScore = 5,
                sensitiveThemes = listOf("Consumo de drogas/alcohol", "Nudidad y contenido sexual", "Suicidio y trauma", "Parpadeo de luces"),
                ageRating = "PEGI 18 / ESRB M",
                detailedSummary = "Contiene combates viscerales en primera persona con armas de fuego y armas blancas, desmembramiento corporal, escenas de sexo y uso constante de lenguaje vulgar."
            ),
            duration = GameDuration(
                mainStoryHours = 25.5f,
                mainPlusExtraHours = 60.0f,
                completionistHours = 104.0f,
                speedrunHours = 18.0f,
                relaxedHours = 130.0f
            ),
            initialRatings = CategoryRatings(
                historia = 4.8f,
                jugabilidad = 4.5f,
                graficos = 4.9f,
                estrategia = 3.6f,
                sonido = 4.9f,
                optimizacion = 4.2f
            ),
            totalCommunityReviews = 1420,
            pricing = GamePricing(
                basePriceUsd = 59.99,
                baseEditionName = "Edición Estándar",
                baseIncludes = listOf("Juego base Cyberpunk 2077", "Actualización gratuita 2.0 (árbol de habilidades y combate vehicular)"),
                deluxePriceUsd = 79.99,
                deluxeEditionName = "Cyberpunk 2077: Ultimate Edition",
                deluxeIncludes = listOf("Expansión completa Phantom Liberty", "Vehículo Quadra Sport R-7 exclusivo", "Banda sonora digital oficial", "Libro de arte digital"),
                dlcPriceUsd = 29.99,
                dlcName = "Phantom Liberty",
                dlcIncludes = listOf("Campaña de espionaje en Dogtown", "Nuevas armas icónicas y ciberware"),
                salePriceUsd = 29.99,
                dealsNote = "Frecuentes ofertas al 50% ($29.99 base / $42.99 Ultimate)"
            )
        ),
        Game(
            id = "elden_ring",
            title = "Elden Ring",
            tagline = "El aclamado RPG de acción y fantasía oscura en las Tierras Intermedias",
            synopsis = "Levántate, tiznado, y déjate guiar por la gracia para esgrimir el poder del Círculo de Elden y convertirte en el Señor del Círculo en las Tierras Intermedias.",
            developer = "FromSoftware",
            publisher = "Bandai Namco",
            releaseYear = 2022,
            genres = listOf("Action RPG", "Fantasía Oscura", "Mundo Abierto", "Souls-like"),
            bannerColorHex = 0xFFD4AF37,
            coverEmoji = "⚔️",
            minCpuScore = 3,
            recCpuScore = 4,
            minCpuText = "Intel Core i5-8400 o AMD Ryzen 3 3300X",
            recCpuText = "Intel Core i7-8700K o AMD Ryzen 5 3600X",
            minGpuScore = 3,
            recGpuScore = 4,
            minGpuText = "NVIDIA GTX 1060 (3 GB) o AMD RX 580 (4 GB)",
            recGpuText = "NVIDIA RTX 2060 (6 GB) o AMD RX 5700 XT",
            minRamGb = 12,
            recRamGb = 16,
            supportedOs = listOf("Windows 10 (64-bit)", "Windows 11 (64-bit)", "Linux / SteamOS"),
            storageRequiredGb = 60,
            accessibility = AccessibilityFeatures(
                subtitleSizeAdjustable = false,
                subtitleBackgroundContrast = true,
                speakerIdentification = true,
                directionalSoundCues = false,
                fullSpanishAudioAndSub = false, // Subtítulos sí, voces en inglés
                fullButtonRemapping = true,
                toggleVsHoldOption = false,
                aimAssistAdjustable = true,
                simplifiedControlScheme = false,
                adaptiveControllerCompatible = true,
                colorblindFilters = emptyList(),
                highContrastMode = false,
                hudScaling = true,
                textToSpeechScreenReader = false,
                reduceMotionAndFlashes = false,
                scoreLetter = "C"
            ),
            contentWarnings = ContentWarnings(
                violenceLevel = "Alta / Brutal",
                violenceScore = 4,
                bloodGore = "Sangre moderada a abundante",
                bloodScore = 4,
                horrorLevel = "Tensión psicológica y diseños grotescos",
                horrorScore = 3,
                strongLanguage = "Limpio",
                languageScore = 1,
                sensitiveThemes = listOf("Body horror / Criaturas grotescas", "Fobia a insectos/arañas", "Muerte y desesperanza"),
                ageRating = "PEGI 16 / ESRB M",
                detailedSummary = "Presenta batallas intensas contra monstruos grotescos y no-muertos, salpicaduras de sangre y ambientación fúnebre opresiva. No incluye sustos sorpresa modernos."
            ),
            duration = GameDuration(
                mainStoryHours = 58.0f,
                mainPlusExtraHours = 101.0f,
                completionistHours = 134.0f,
                speedrunHours = 35.0f,
                relaxedHours = 180.0f
            ),
            initialRatings = CategoryRatings(
                historia = 4.6f,
                jugabilidad = 4.9f,
                graficos = 4.7f,
                estrategia = 4.9f,
                sonido = 4.8f,
                optimizacion = 4.3f
            ),
            totalCommunityReviews = 2150,
            pricing = GamePricing(
                basePriceUsd = 59.99,
                baseEditionName = "Elden Ring Estándar",
                baseIncludes = listOf("Juego base completo en las Tierras Intermedias"),
                deluxePriceUsd = 79.99,
                deluxeEditionName = "Elden Ring: Shadow of the Erdtree Edition",
                deluxeIncludes = listOf("Expansión masiva Shadow of the Erdtree", "Libro de arte digital", "Banda sonora original oficial"),
                dlcPriceUsd = 39.99,
                dlcName = "Shadow of the Erdtree",
                dlcIncludes = listOf("Nueva región Reino de las Sombras", "Nuevos jefes y más de 100 armas"),
                salePriceUsd = 35.99,
                dealsNote = "Histórico mínimo en rebajas de temporada: $35.99"
            )
        ),
        Game(
            id = "baldur_gate_3",
            title = "Baldur's Gate 3",
            tagline = "El RPG táctico definitivo basado en Dungeons & Dragons con libertad total",
            synopsis = "Reúne a tu grupo y regresa a los Reinos Olvidados en una historia de compañerismo y traición, sacrificio y supervivencia, y la tentación del poder absoluto.",
            developer = "Larian Studios",
            publisher = "Larian Studios",
            releaseYear = 2023,
            genres = listOf("RPG", "Estrategia por turnos", "Fantasía", "Aventura"),
            bannerColorHex = 0xFF9E2A2B,
            coverEmoji = "🎲",
            minCpuScore = 3,
            recCpuScore = 4,
            minCpuText = "Intel Core i5-4690 o AMD FX 8350",
            recCpuText = "Intel Core i7-8700K o AMD Ryzen 5 3600X",
            minGpuScore = 3,
            recGpuScore = 4,
            minGpuText = "NVIDIA GTX 970 o AMD RX 480",
            recGpuText = "NVIDIA RTX 2060 Super o AMD RX 5700 XT",
            minRamGb = 8,
            recRamGb = 16,
            supportedOs = listOf("Windows 10 (64-bit)", "Windows 11 (64-bit)", "macOS"),
            storageRequiredGb = 150,
            accessibility = AccessibilityFeatures(
                subtitleSizeAdjustable = true,
                subtitleBackgroundContrast = true,
                speakerIdentification = true,
                directionalSoundCues = true,
                fullSpanishAudioAndSub = false, // Subtítulos español, voces inglés
                fullButtonRemapping = true,
                toggleVsHoldOption = true,
                aimAssistAdjustable = false, // Combate por turnos sin prisa
                simplifiedControlScheme = true,
                adaptiveControllerCompatible = true,
                colorblindFilters = listOf("Protanopia", "Deuteranopia", "Tritanopia"),
                highContrastMode = true,
                hudScaling = true,
                textToSpeechScreenReader = true,
                reduceMotionAndFlashes = true,
                scoreLetter = "A"
            ),
            contentWarnings = ContentWarnings(
                violenceLevel = "Moderada a Alta",
                violenceScore = 4,
                bloodGore = "Sangre y desmembramientos en combate",
                bloodScore = 4,
                horrorLevel = "Tensión y horror cósmico (Azotamentes)",
                horrorScore = 3,
                strongLanguage = "Moderado",
                languageScore = 3,
                sensitiveThemes = listOf("Parásitos corporales", "Tortura narrativa", "Contenido sexual maduro", "Manipulación mental"),
                ageRating = "PEGI 18 / ESRB M",
                detailedSummary = "Incluye temas adultos explícitos, posibilidad de romance íntimo con desnudez, mutilaciones y situaciones de horror visceral relacionadas con renacuajos cerebrales."
            ),
            duration = GameDuration(
                mainStoryHours = 75.0f,
                mainPlusExtraHours = 110.0f,
                completionistHours = 155.0f,
                speedrunHours = 45.0f,
                relaxedHours = 200.0f
            ),
            initialRatings = CategoryRatings(
                historia = 4.9f,
                jugabilidad = 4.8f,
                graficos = 4.8f,
                estrategia = 5.0f,
                sonido = 4.9f,
                optimizacion = 4.4f
            ),
            totalCommunityReviews = 1890,
            pricing = GamePricing(
                basePriceUsd = 59.99,
                baseEditionName = "Baldur's Gate 3 Estándar",
                baseIncludes = listOf("Campaña RPG completa con más de 100 horas y ramificaciones"),
                deluxePriceUsd = 69.99,
                deluxeEditionName = "Baldur's Gate 3: Digital Deluxe Edition",
                deluxeIncludes = listOf("Pack de tesoros de Divinity II", "Tema de dados de tirada digital exclusivo", "Banda sonora digital de 3 discos", "Libro de arte conceptual digital"),
                dlcPriceUsd = 9.99,
                dlcName = "Digital Deluxe DLC Upgrade",
                dlcIncludes = listOf("Hojas de personaje de D&D imprimibles", "Bolsa de aventurero"),
                salePriceUsd = 47.99,
                dealsNote = "Descuentos poco frecuentes (máximo -20% a $47.99)"
            )
        ),
        Game(
            id = "silent_hill_2_remake",
            title = "Silent Hill 2 Remake",
            tagline = "La obra maestra del terror psicológico rehecha con gráficos hiperrealistas",
            synopsis = "Habiendo recibido una carta de su difunta esposa, James se dirige al lugar donde compartieron tantos recuerdos con la esperanza de verla una vez más: Silent Hill.",
            developer = "Bloober Team",
            publisher = "Konami",
            releaseYear = 2024,
            genres = listOf("Terror", "Supervivencia", "Psicológico", "Misterio"),
            bannerColorHex = 0xFF354F52,
            coverEmoji = "🌫️",
            minCpuScore = 4,
            recCpuScore = 5,
            minCpuText = "Intel Core i7-6700K o AMD Ryzen 5 3600",
            recCpuText = "Intel Core i7-8700K o AMD Ryzen 5 3600X",
            minGpuScore = 3,
            recGpuScore = 5,
            minGpuText = "NVIDIA GeForce GTX 1070 Ti o AMD RX 5700",
            recGpuText = "NVIDIA GeForce RTX 2080 o AMD Radeon 6800XT",
            minRamGb = 16,
            recRamGb = 16,
            supportedOs = listOf("Windows 11 (64-bit)"),
            storageRequiredGb = 50,
            accessibility = AccessibilityFeatures(
                subtitleSizeAdjustable = true,
                subtitleBackgroundContrast = true,
                speakerIdentification = true,
                directionalSoundCues = true,
                fullSpanishAudioAndSub = false,
                fullButtonRemapping = true,
                toggleVsHoldOption = true,
                aimAssistAdjustable = true,
                simplifiedControlScheme = true,
                adaptiveControllerCompatible = true,
                colorblindFilters = listOf("Protanopia", "Deuteranopia", "Tritanopia"),
                highContrastMode = true,
                hudScaling = true,
                textToSpeechScreenReader = false,
                reduceMotionAndFlashes = true,
                scoreLetter = "A"
            ),
            contentWarnings = ContentWarnings(
                violenceLevel = "Alta / Brutal",
                violenceScore = 5,
                bloodGore = "Gore explícito y criaturas desfiguradas",
                bloodScore = 5,
                horrorLevel = "Terror psicológico extremo y Jump Scares",
                horrorScore = 5,
                strongLanguage = "Moderado",
                languageScore = 3,
                sensitiveThemes = listOf("Depresión clínica severa", "Suicidio y culpa patológica", "Enfermedades terminales", "Asfixia y abuso"),
                ageRating = "PEGI 18 / ESRB M",
                detailedSummary = "Trata en profundidad el dolor por enfermedad terminal, autodesprecio, representaciones gráficas de suicidio y monstruos que simbolizan pulsiones reprimidas y violencia."
            ),
            duration = GameDuration(
                mainStoryHours = 16.0f,
                mainPlusExtraHours = 19.5f,
                completionistHours = 24.0f,
                speedrunHours = 12.0f,
                relaxedHours = 28.0f
            ),
            initialRatings = CategoryRatings(
                historia = 4.9f,
                jugabilidad = 4.4f,
                graficos = 4.9f,
                estrategia = 3.9f,
                sonido = 5.0f,
                optimizacion = 4.1f
            ),
            totalCommunityReviews = 980,
            pricing = GamePricing(
                basePriceUsd = 69.99,
                baseEditionName = "Silent Hill 2 Remake Estándar",
                baseIncludes = listOf("Juego base completo rehecho en Unreal Engine 5"),
                deluxePriceUsd = 79.99,
                deluxeEditionName = "Silent Hill 2: Digital Deluxe Edition",
                deluxeIncludes = listOf("Máscara de Robbie el Conejo", "Máscara de Pyramid Head cosmética", "Libro de arte digital", "Banda sonora oficial digital de Akira Yamaoka"),
                dlcPriceUsd = 9.99,
                dlcName = "Deluxe Upgrade Pack",
                dlcIncludes = listOf("Máscaras cosméticas exclusivas y banda sonora digital"),
                salePriceUsd = 59.49,
                dealsNote = "Lanzamiento reciente con ofertas ocasionales del 15% ($59.49)"
            )
        ),
        Game(
            id = "hades_2",
            title = "Hades II",
            tagline = "Lucha más allá del Inframundo con hechicería oscura contra el Titán del Tiempo",
            synopsis = "La primera secuela de Supergiant Games aprovecha lo mejor del original para ofrecerte una experiencia fascinante e infinitamente rejugable.",
            developer = "Supergiant Games",
            publisher = "Supergiant Games",
            releaseYear = 2024,
            genres = listOf("Roguelike", "Acción", "Mitología", "Indie"),
            bannerColorHex = 0xFF06D6A0,
            coverEmoji = "🔱",
            minCpuScore = 2,
            recCpuScore = 3,
            minCpuText = "Dual Core 2.4 GHz o superior",
            recCpuText = "Quad Core 3.0 GHz o superior",
            minGpuScore = 2,
            recGpuScore = 3,
            minGpuText = "GeForce GTX 950 o Radeon R7 360",
            recGpuText = "GeForce GTX 1060 o Radeon RX 580",
            minRamGb = 8,
            recRamGb = 16,
            supportedOs = listOf("Windows 10 (64-bit)", "Windows 11 (64-bit)", "Linux / SteamOS", "macOS"),
            storageRequiredGb = 10,
            accessibility = AccessibilityFeatures(
                subtitleSizeAdjustable = true,
                subtitleBackgroundContrast = true,
                speakerIdentification = true,
                directionalSoundCues = true,
                fullSpanishAudioAndSub = false, // Subtítulos excelente español
                fullButtonRemapping = true,
                toggleVsHoldOption = true,
                aimAssistAdjustable = true,
                simplifiedControlScheme = true,
                adaptiveControllerCompatible = true,
                colorblindFilters = listOf("Modo de Alto Contraste para proyectiles"),
                highContrastMode = true,
                hudScaling = true,
                textToSpeechScreenReader = false,
                reduceMotionAndFlashes = true,
                scoreLetter = "A+"
            ),
            contentWarnings = ContentWarnings(
                violenceLevel = "Moderada (Animada)",
                violenceScore = 2,
                bloodGore = "Sin sangre realista",
                bloodScore = 1,
                horrorLevel = "Ninguno",
                horrorScore = 1,
                strongLanguage = "Limpio",
                languageScore = 1,
                sensitiveThemes = listOf("Mitología y fantasmas", "Muerte cósmica recurrente"),
                ageRating = "PEGI 12 / ESRB T",
                detailedSummary = "Combates fluidos y estilizados estilo cómic mitológico. Las muertes regresan al personaje al campamento base sin violencia gráfica ni sangre realista."
            ),
            duration = GameDuration(
                mainStoryHours = 22.0f,
                mainPlusExtraHours = 52.0f,
                completionistHours = 95.0f,
                speedrunHours = 15.0f,
                relaxedHours = 120.0f
            ),
            initialRatings = CategoryRatings(
                historia = 4.7f,
                jugabilidad = 5.0f,
                graficos = 4.9f,
                estrategia = 4.6f,
                sonido = 5.0f,
                optimizacion = 4.9f
            ),
            totalCommunityReviews = 1560,
            pricing = GamePricing(
                basePriceUsd = 29.99,
                baseEditionName = "Hades II Early Access",
                baseIncludes = listOf("Acceso anticipado completo con todas las regiones y jefes de Melínoë"),
                deluxePriceUsd = 39.99,
                deluxeEditionName = "Hades II: Supporter Edition",
                deluxeIncludes = listOf("Banda sonora oficial completa de Darren Korb en FLAC", "Libro de arte conceptual digital de Supergiant Games"),
                dlcPriceUsd = 9.99,
                dlcName = "Official Soundtrack",
                dlcIncludes = listOf("OST completa en alta fidelidad"),
                salePriceUsd = 26.99,
                dealsNote = "Precio indie ajustado y accesible por política de Supergiant Games"
            )
        ),
        Game(
            id = "hollow_knight",
            title = "Hollow Knight",
            tagline = "Forja tu propio camino a través de un vasto reino en ruinas de insectos y héroes",
            synopsis = "Desciende a la oscuridad y explora Hallownest, un laberíntico reino subterráneo lleno de criaturas extrañas y misterios ancestrales.",
            developer = "Team Cherry",
            publisher = "Team Cherry",
            releaseYear = 2017,
            genres = listOf("Metroidvania", "Acción", "Plataformas", "Indie"),
            bannerColorHex = 0xFF4A4E69,
            coverEmoji = "🪲",
            minCpuScore = 1,
            recCpuScore = 2,
            minCpuText = "Intel Core 2 Duo E5200 o equivalente",
            recCpuText = "Intel Core i5 o superior",
            minGpuScore = 1,
            recGpuScore = 2,
            minGpuText = "GeForce 9800GTX+ (512MB) o equivalente",
            recGpuText = "GeForce GTX 560 o superior",
            minRamGb = 4,
            recRamGb = 8,
            supportedOs = listOf("Windows 10 (64-bit)", "Windows 11 (64-bit)", "Linux / SteamOS", "macOS"),
            storageRequiredGb = 9,
            accessibility = AccessibilityFeatures(
                subtitleSizeAdjustable = false,
                subtitleBackgroundContrast = true,
                speakerIdentification = false,
                directionalSoundCues = true,
                fullSpanishAudioAndSub = false, // Textos traducidos
                fullButtonRemapping = true,
                toggleVsHoldOption = false,
                aimAssistAdjustable = false,
                simplifiedControlScheme = false,
                adaptiveControllerCompatible = true,
                colorblindFilters = emptyList(),
                highContrastMode = false,
                hudScaling = false,
                textToSpeechScreenReader = false,
                reduceMotionAndFlashes = true,
                scoreLetter = "B"
            ),
            contentWarnings = ContentWarnings(
                violenceLevel = "Moderada (Animación 2D)",
                violenceScore = 2,
                bloodGore = "Fluidos de insectos / Sin sangre humana",
                bloodScore = 1,
                horrorLevel = "Atmósfera sombría y misteriosa",
                horrorScore = 2,
                strongLanguage = "Limpio",
                languageScore = 1,
                sensitiveThemes = listOf("Aracnofobia (Zona de Nido Profundo)", "Infección y decadencia"),
                ageRating = "PEGI 7 / ESRB E10+",
                detailedSummary = "Aventuras en 2D dibujadas a mano. Advertencia especial: la zona 'Nido Profundo' (Deepnest) puede incomodar a jugadores con fobia a arañas e insectos veloces."
            ),
            duration = GameDuration(
                mainStoryHours = 27.0f,
                mainPlusExtraHours = 41.5f,
                completionistHours = 63.0f,
                speedrunHours = 18.0f,
                relaxedHours = 80.0f
            ),
            initialRatings = CategoryRatings(
                historia = 4.8f,
                jugabilidad = 4.9f,
                graficos = 4.9f,
                estrategia = 4.5f,
                sonido = 5.0f,
                optimizacion = 4.9f
            ),
            totalCommunityReviews = 2300,
            pricing = GamePricing(
                basePriceUsd = 14.99,
                baseEditionName = "Hollow Knight Edición Completa",
                baseIncludes = listOf("Juego base completo", "4 DLCs de contenido gratuitos (Hidden Dreams, Grimm Troupe, Lifeblood, Godmaster)"),
                deluxePriceUsd = 24.99,
                deluxeEditionName = "Hollow Knight: Collector's & OST Bundle",
                deluxeIncludes = listOf("Banda sonora oficial completa de Christopher Larkin", "OST de Gods & Nightmares", "Libro digital Wanderer's Journal"),
                dlcPriceUsd = 9.99,
                dlcName = "Soundtrack Bundle",
                dlcIncludes = listOf("Banda sonora orquestal completa"),
                salePriceUsd = 7.49,
                dealsNote = "¡Suele estar de oferta al -50% por tan solo $7.49!"
            )
        ),
        Game(
            id = "celeste",
            title = "Celeste",
            tagline = "Ayuda a Madeline a sobrevivir a sus demonios internos en el ascenso a la montaña",
            synopsis = "Un juego de plataformas sobre escalar una montaña mítica, mientras superas tus propios miedos en una aventura guiada por una narrativa conmovedora.",
            developer = "Extremely OK Games",
            publisher = "Extremely OK Games",
            releaseYear = 2018,
            genres = listOf("Plataformas", "Indie", "Superación", "Precisión"),
            bannerColorHex = 0xFFFF6B6B,
            coverEmoji = "🍓",
            minCpuScore = 1,
            recCpuScore = 1,
            minCpuText = "Intel Core i3 M380 2.53GHz",
            recCpuText = "Cualquier CPU moderna de 2 núcleos",
            minGpuScore = 1,
            recGpuScore = 1,
            minGpuText = "Intel HD Graphics 4000",
            recGpuText = "GeForce GT 730 o integrada moderna",
            minRamGb = 2,
            recRamGb = 4,
            supportedOs = listOf("Windows 10 (64-bit)", "Windows 11 (64-bit)", "Linux / SteamOS", "macOS"),
            storageRequiredGb = 2,
            accessibility = AccessibilityFeatures(
                subtitleSizeAdjustable = true,
                subtitleBackgroundContrast = true,
                speakerIdentification = true,
                directionalSoundCues = true,
                fullSpanishAudioAndSub = false,
                fullButtonRemapping = true,
                toggleVsHoldOption = true,
                aimAssistAdjustable = true, // Modo Asistencia galardonado
                simplifiedControlScheme = true,
                adaptiveControllerCompatible = true,
                colorblindFilters = listOf("Modo de Alto Contraste"),
                highContrastMode = true,
                hudScaling = true,
                textToSpeechScreenReader = false,
                reduceMotionAndFlashes = true,
                scoreLetter = "A+"
            ),
            contentWarnings = ContentWarnings(
                violenceLevel = "Nula / Mínima",
                violenceScore = 1,
                bloodGore = "Sin sangre",
                bloodScore = 1,
                horrorLevel = "Ninguno",
                horrorScore = 1,
                strongLanguage = "Limpio",
                languageScore = 1,
                sensitiveThemes = listOf("Ataques de pánico y ansiedad", "Depresión y autoexigencia"),
                ageRating = "PEGI 7 / ESRB E10+",
                detailedSummary = "Presenta temas de salud mental, ansiedad y ataques de pánico tratados con mucho respeto y empatía, incluyendo ejercicios de respiración interactivos."
            ),
            duration = GameDuration(
                mainStoryHours = 8.0f,
                mainPlusExtraHours = 14.5f,
                completionistHours = 38.5f,
                speedrunHours = 5.0f,
                relaxedHours = 50.0f
            ),
            initialRatings = CategoryRatings(
                historia = 4.9f,
                jugabilidad = 5.0f,
                graficos = 4.7f,
                estrategia = 4.1f,
                sonido = 5.0f,
                optimizacion = 5.0f
            ),
            totalCommunityReviews = 1750,
            pricing = GamePricing(
                basePriceUsd = 19.99,
                baseEditionName = "Celeste Estándar",
                baseIncludes = listOf("Juego base completo (Capítulos 1 al 8)", "Capítulo 9: Farewell (DLC masivo gratuito)"),
                deluxePriceUsd = 27.99,
                deluxeEditionName = "Celeste: Complete Audio & Art Edition",
                deluxeIncludes = listOf("Banda sonora original de Lena Raine", "Celeste B-Sides OST digital", "Celeste: Farewell OST digital"),
                dlcPriceUsd = 7.99,
                dlcName = "Complete Soundtrack Pack",
                dlcIncludes = listOf("Banda sonora completa en FLAC y MP3"),
                salePriceUsd = 4.99,
                dealsNote = "¡Descuento frecuente del 75% a tan solo $4.99!"
            )
        ),
        Game(
            id = "red_dead_redemption_2",
            title = "Red Dead Redemption 2",
            tagline = "La épica historia del forajido Arthur Morgan y la banda de Van der Linde",
            synopsis = "América, 1899. El ocaso del Salvaje Oeste ha comenzado. Tras un atraco fallido, Arthur Morgan y su banda se ven obligados a huir a través del implacable corazón de los Estados Unidos.",
            developer = "Rockstar Games",
            publisher = "Rockstar Games",
            releaseYear = 2019,
            genres = listOf("Acción", "Mundo Abierto", "Western", "Aventura"),
            bannerColorHex = 0xFFC1121F,
            coverEmoji = "🤠",
            minCpuScore = 3,
            recCpuScore = 4,
            minCpuText = "Intel Core i5-2500K o AMD FX-6300",
            recCpuText = "Intel Core i7-4770K o AMD Ryzen 5 1500X",
            minGpuScore = 3,
            recGpuScore = 4,
            minGpuText = "NVIDIA GTX 770 (2 GB) o AMD Radeon R9 280 (3 GB)",
            recGpuText = "NVIDIA GTX 1060 (6 GB) o AMD Radeon RX 480 (4 GB)",
            minRamGb = 8,
            recRamGb = 12,
            supportedOs = listOf("Windows 10 (64-bit)", "Windows 11 (64-bit)"),
            storageRequiredGb = 150,
            accessibility = AccessibilityFeatures(
                subtitleSizeAdjustable = true,
                subtitleBackgroundContrast = true,
                speakerIdentification = true,
                directionalSoundCues = true,
                fullSpanishAudioAndSub = false, // Subtítulos en español
                fullButtonRemapping = true,
                toggleVsHoldOption = true,
                aimAssistAdjustable = true, // Auto-apuntado asistido completo
                simplifiedControlScheme = true,
                adaptiveControllerCompatible = true,
                colorblindFilters = listOf("Protanopia", "Deuteranopia", "Tritanopia"),
                highContrastMode = false,
                hudScaling = true,
                textToSpeechScreenReader = false,
                reduceMotionAndFlashes = true,
                scoreLetter = "A"
            ),
            contentWarnings = ContentWarnings(
                violenceLevel = "Alta / Brutal",
                violenceScore = 5,
                bloodGore = "Gore y heridas balísticas realistas",
                bloodScore = 4,
                horrorLevel = "Tensión y encuentros nocturnos",
                horrorScore = 2,
                strongLanguage = "Explícito",
                languageScore = 4,
                sensitiveThemes = listOf("Consumo de tabaco y alcohol", "Violencia de época", "Enfermedades mortales"),
                ageRating = "PEGI 18 / ESRB M",
                detailedSummary = "Mundo crudo con tiroteos, caza de animales con despellejamiento realista, sangre, consumo de licor y temas dramáticos sobre la mortalidad y la moral."
            ),
            duration = GameDuration(
                mainStoryHours = 50.0f,
                mainPlusExtraHours = 82.0f,
                completionistHours = 182.0f,
                speedrunHours = 35.0f,
                relaxedHours = 220.0f
            ),
            initialRatings = CategoryRatings(
                historia = 5.0f,
                jugabilidad = 4.7f,
                graficos = 5.0f,
                estrategia = 3.5f,
                sonido = 5.0f,
                optimizacion = 4.6f
            ),
            totalCommunityReviews = 3100,
            pricing = GamePricing(
                basePriceUsd = 59.99,
                baseEditionName = "Red Dead Redemption 2 Estándar",
                baseIncludes = listOf("Historia completa de Arthur Morgan", "Acceso completo a Red Dead Online"),
                deluxePriceUsd = 99.99,
                deluxeEditionName = "Red Dead Redemption 2: Ultimate Edition",
                deluxeIncludes = listOf("Misión exclusiva de atraco a banco y escondite en modo historia", "Caballo pura sangre tordillo negro", "Atuendo de pistolero de Nuevo Paraíso", "Bonificaciones de dinero y rango en Red Dead Online"),
                dlcPriceUsd = 39.99,
                dlcName = "Ultimate Content Upgrade",
                dlcIncludes = listOf("Mejoras de juego para un jugador y bonos online"),
                salePriceUsd = 19.79,
                dealsNote = "Suele estar en gran oferta a $19.79 (-67%) para la edición base"
            )
        ),
        Game(
            id = "stardew_valley",
            title = "Stardew Valley",
            tagline = "Heredaste la vieja granja de tu abuelo en un valle lleno de vida y tranquilidad",
            synopsis = "Armado con herramientas de segunda mano y unas pocas monedas, te dispones a empezar una nueva vida cultivando, conociendo a los vecinos y restaurando el pueblo.",
            developer = "ConcernedApe",
            publisher = "ConcernedApe",
            releaseYear = 2016,
            genres = listOf("Simulación", "Granja", "Relajante", "Indie"),
            bannerColorHex = 0xFF588157,
            coverEmoji = "🌾",
            minCpuScore = 1,
            recCpuScore = 1,
            minCpuText = "2 GHz o cualquier procesador",
            recCpuText = "Cualquier procesador moderno",
            minGpuScore = 1,
            recGpuScore = 1,
            minGpuText = "256 MB de memoria de vídeo (Shader Model 3.0+)",
            recGpuText = "Cualquier GPU o gráfica integrada",
            minRamGb = 2,
            recRamGb = 4,
            supportedOs = listOf("Windows 10 (64-bit)", "Windows 11 (64-bit)", "Linux / SteamOS", "macOS"),
            storageRequiredGb = 1,
            accessibility = AccessibilityFeatures(
                subtitleSizeAdjustable = false,
                subtitleBackgroundContrast = true,
                speakerIdentification = true,
                directionalSoundCues = false,
                fullSpanishAudioAndSub = false, // Textos y diálogos completamente en español
                fullButtonRemapping = true,
                toggleVsHoldOption = true,
                aimAssistAdjustable = false,
                simplifiedControlScheme = true,
                adaptiveControllerCompatible = true,
                colorblindFilters = emptyList(),
                highContrastMode = false,
                hudScaling = true,
                textToSpeechScreenReader = false,
                reduceMotionAndFlashes = true,
                scoreLetter = "B+"
            ),
            contentWarnings = ContentWarnings(
                violenceLevel = "Nula / Mínima",
                violenceScore = 1,
                bloodGore = "Sin sangre",
                bloodScore = 1,
                horrorLevel = "Ninguno",
                horrorScore = 1,
                strongLanguage = "Limpio",
                languageScore = 1,
                sensitiveThemes = listOf("Consumo ocasional de cerveza en taberna", "Temas de estrés laboral inicial"),
                ageRating = "PEGI 12 / ESRB E10+",
                detailedSummary = "Experiencia sumamente pacífica y saludable apta para todas las edades. Combate menor contra babas y murciélagos en minas sin sangre ni violencia explícita."
            ),
            duration = GameDuration(
                mainStoryHours = 52.5f,
                mainPlusExtraHours = 94.0f,
                completionistHours = 160.0f,
                speedrunHours = 30.0f,
                relaxedHours = 250.0f
            ),
            initialRatings = CategoryRatings(
                historia = 4.5f,
                jugabilidad = 4.9f,
                graficos = 4.7f,
                estrategia = 4.3f,
                sonido = 4.9f,
                optimizacion = 5.0f
            ),
            totalCommunityReviews = 2800,
            pricing = GamePricing(
                basePriceUsd = 14.99,
                baseEditionName = "Stardew Valley Estándar",
                baseIncludes = listOf("Juego base completo con granjas, minería, cooperativo y actualización masiva 1.6"),
                deluxePriceUsd = 21.99,
                deluxeEditionName = "Stardew Valley: Complete Soundtrack Edition",
                deluxeIncludes = listOf("Banda sonora oficial con más de 70 temas de ConcernedApe", "Guía digital de cultivos y aldeanos"),
                dlcPriceUsd = 6.99,
                dlcName = "Original Soundtrack",
                dlcIncludes = listOf("Todas las pistas musicales del juego en alta calidad"),
                salePriceUsd = 9.99,
                dealsNote = "Mínimo histórico: $9.99 (-33%)"
            )
        ),
        Game(
            id = "alan_wake_2",
            title = "Alan Wake 2",
            tagline = "Un monstruo con muchas caras te acecha entre dos realidades que colapsan",
            synopsis = "Una serie de asesinatos rituales amenaza Bright Falls. Saga Anderson llega para investigar, mientras Alan Wake escribe una oscura historia para escapar de su pesadilla.",
            developer = "Remedy Entertainment",
            publisher = "Epic Games Publishing",
            releaseYear = 2023,
            genres = listOf("Survival Horror", "Narrativo", "Misterio", "Acción"),
            bannerColorHex = 0xFF6B2D5C,
            coverEmoji = "🔦",
            minCpuScore = 4,
            recCpuScore = 5,
            minCpuText = "Intel i5-7600K o equivalente AMD",
            recCpuText = "AMD Ryzen 7 3700X o equivalente Intel",
            minGpuScore = 4,
            recGpuScore = 5,
            minGpuText = "GeForce RTX 2060 o Radeon RX 6600 (Requiere Mesh Shaders)",
            recGpuText = "GeForce RTX 3070 o Radeon RX 6700 XT",
            minRamGb = 16,
            recRamGb = 16,
            supportedOs = listOf("Windows 10 (64-bit)", "Windows 11 (64-bit)"),
            storageRequiredGb = 90,
            accessibility = AccessibilityFeatures(
                subtitleSizeAdjustable = true,
                subtitleBackgroundContrast = true,
                speakerIdentification = true,
                directionalSoundCues = true,
                fullSpanishAudioAndSub = true,
                fullButtonRemapping = true,
                toggleVsHoldOption = true,
                aimAssistAdjustable = true,
                simplifiedControlScheme = true,
                adaptiveControllerCompatible = true,
                colorblindFilters = listOf("Protanopia", "Deuteranopia", "Tritanopia"),
                highContrastMode = true,
                hudScaling = true,
                textToSpeechScreenReader = false,
                reduceMotionAndFlashes = true,
                scoreLetter = "A"
            ),
            contentWarnings = ContentWarnings(
                violenceLevel = "Alta / Brutal",
                violenceScore = 5,
                bloodGore = "Gore explícito, autopsias y desmembramiento",
                bloodScore = 5,
                horrorLevel = "Terror psicológico extremo y Jump Scares constantes",
                horrorScore = 5,
                strongLanguage = "Frecuente",
                languageScore = 4,
                sensitiveThemes = listOf("Rituales homicidas", "Alucinaciones y psicosis", "Jump scares audiovisuales intensos"),
                ageRating = "PEGI 18 / ESRB M",
                detailedSummary = "Presenta sustos instantáneos con destellos visuales y de sonido ensordecedor ('screamer flashes'), investigación forense con cuerpos mutilados y atmósfera angustiante."
            ),
            duration = GameDuration(
                mainStoryHours = 18.0f,
                mainPlusExtraHours = 24.5f,
                completionistHours = 32.0f,
                speedrunHours = 14.0f,
                relaxedHours = 38.0f
            ),
            initialRatings = CategoryRatings(
                historia = 4.9f,
                jugabilidad = 4.5f,
                graficos = 5.0f,
                estrategia = 3.8f,
                sonido = 5.0f,
                optimizacion = 4.2f
            ),
            totalCommunityReviews = 1120,
            pricing = GamePricing(
                basePriceUsd = 49.99,
                baseEditionName = "Alan Wake 2 Estándar",
                baseIncludes = listOf("Campaña de Saga Anderson y Alan Wake en dos realidades"),
                deluxePriceUsd = 69.99,
                deluxeEditionName = "Alan Wake 2: Deluxe Edition",
                deluxeIncludes = listOf("Pase de expansión con 'Night Springs' y 'The Lake House'", "Atuendo de cortavientos carmesí de Saga", "Atuendo de esmoquin de celebridad de Alan", "Aspecto de escopeta parlamentaria"),
                dlcPriceUsd = 20.00,
                dlcName = "Expansion Pass",
                dlcIncludes = listOf("2 expansiones de historia completas"),
                salePriceUsd = 34.99,
                dealsNote = "Cupones frecuentes en Epic Games Store dejándolo en $34.99"
            )
        ),
        Game(
            id = "the_last_of_us_part_1",
            title = "The Last of Us Parte I",
            tagline = "Resiste y sobrevive a través de una América postpandémica implacable",
            synopsis = "En una civilización devastada donde proliferan infectados y supervivientes curtidos, Joel, un exhausto protagonista, es contratado para sacar de contrabando a Ellie, una niña de 14 años, de una zona militar en cuarentena.",
            developer = "Naughty Dog",
            publisher = "PlayStation PC LLC",
            releaseYear = 2023,
            genres = listOf("Acción", "Aventura", "Supervivencia", "Narrativo"),
            bannerColorHex = 0xFF2D3E4E,
            coverEmoji = "🌿",
            minCpuScore = 3,
            recCpuScore = 4,
            minCpuText = "Intel Core i7-4770K o AMD Ryzen 5 1500X",
            recCpuText = "Intel Core i7-8700 o AMD Ryzen 5 3600X",
            minGpuScore = 3,
            recGpuScore = 4,
            minGpuText = "NVIDIA GTX 970 / GTX 1050 Ti (4 GB) o AMD Radeon RX 470 (4 GB)",
            recGpuText = "NVIDIA RTX 2070 Super (8 GB) o AMD Radeon RX 6600 XT (8 GB)",
            minRamGb = 16,
            recRamGb = 16,
            supportedOs = listOf("Windows 10 (64-bit)", "Windows 11 (64-bit)"),
            storageRequiredGb = 100,
            accessibility = AccessibilityFeatures(
                subtitleSizeAdjustable = true,
                subtitleBackgroundContrast = true,
                speakerIdentification = true,
                directionalSoundCues = true,
                fullSpanishAudioAndSub = true,
                fullButtonRemapping = true,
                toggleVsHoldOption = true,
                aimAssistAdjustable = true,
                simplifiedControlScheme = true,
                adaptiveControllerCompatible = true,
                colorblindFilters = listOf("Protanopia", "Deuteranopia", "Tritanopia"),
                highContrastMode = true,
                hudScaling = true,
                textToSpeechScreenReader = true,
                reduceMotionAndFlashes = true,
                scoreLetter = "A+"
            ),
            contentWarnings = ContentWarnings(
                violenceLevel = "Alta / Brutal",
                violenceScore = 5,
                bloodGore = "Gore explícito, desmembramientos y heridas abiertas",
                bloodScore = 5,
                horrorLevel = "Terror tenso, chasqueadores y sustos acústicos",
                horrorScore = 4,
                strongLanguage = "Explícito y frecuente",
                languageScore = 4,
                sensitiveThemes = listOf("Pérdida traumática y duelo infantil", "Infección fúngica violenta", "Violencia humana despiadada"),
                ageRating = "PEGI 18 / ESRB M",
                detailedSummary = "Presenta secuencias de combate viscerales y brutales, decapitaciones provocadas por armas de fuego y escopetas, zombis fúngicos (chasqueadores/hinchados) y lenguaje profano intenso."
            ),
            duration = GameDuration(
                mainStoryHours = 15.0f,
                mainPlusExtraHours = 18.5f,
                completionistHours = 23.5f,
                speedrunHours = 9.0f,
                relaxedHours = 27.0f
            ),
            initialRatings = CategoryRatings(
                historia = 5.0f,
                jugabilidad = 4.8f,
                graficos = 4.9f,
                estrategia = 4.3f,
                sonido = 5.0f,
                optimizacion = 4.0f
            ),
            totalCommunityReviews = 3450,
            pricing = GamePricing(
                basePriceUsd = 59.99,
                baseEditionName = "The Last of Us Parte I Estándar",
                baseIncludes = listOf("Historia para un jugador de The Last of Us", "Capítulo precuela aclamado Left Behind"),
                deluxePriceUsd = 69.99,
                deluxeEditionName = "The Last of Us Parte I: Digital Deluxe",
                deluxeIncludes = listOf("Desbloqueo anticipado de 2 mejoras de habilidad", "Flechas explosivas anticipadas", "Aspecto de pistola 9 mm en oro", "Filtro visual Dither Punk", "Modo Speedrun desbloqueado"),
                dlcPriceUsd = 10.00,
                dlcName = "Digital Deluxe Upgrade",
                dlcIncludes = listOf("Modificadores de juego anticipados y skins cosméticas"),
                salePriceUsd = 39.99,
                dealsNote = "Rebajas de temporada de PlayStation PC a $39.99 (-33%)"
            )
        ),
        Game(
            id = "titanfall_2",
            title = "Titanfall 2",
            tagline = "Piloto y Titán se unen en una de las campañas FPS más celebradas de la historia",
            synopsis = "Atrapado tras las líneas enemigas contra probabilidades abrumadoras, debes asociarte con un titán veterano clase Vanguardia, BT-7274, para llevar a cabo una misión que nunca debió ser tuya.",
            developer = "Respawn Entertainment",
            publisher = "Electronic Arts",
            releaseYear = 2016,
            genres = listOf("FPS", "Ciencia Ficción", "Acción", "Mechas"),
            bannerColorHex = 0xFFE07A5F,
            coverEmoji = "🤖",
            minCpuScore = 2,
            recCpuScore = 3,
            minCpuText = "Intel Core i3-6300t o equivalente AMD",
            recCpuText = "Intel Core i5-6600K o equivalente AMD",
            minGpuScore = 2,
            recGpuScore = 3,
            minGpuText = "NVIDIA GeForce GTX 660 (2 GB) o AMD Radeon HD 7850 (2 GB)",
            recGpuText = "NVIDIA GeForce GTX 1060 (6 GB) o AMD Radeon RX 480 (8 GB)",
            minRamGb = 8,
            recRamGb = 16,
            supportedOs = listOf("Windows 10 (64-bit)", "Windows 11 (64-bit)"),
            storageRequiredGb = 45,
            accessibility = AccessibilityFeatures(
                subtitleSizeAdjustable = true,
                subtitleBackgroundContrast = true,
                speakerIdentification = true,
                directionalSoundCues = true,
                fullSpanishAudioAndSub = true,
                fullButtonRemapping = true,
                toggleVsHoldOption = true,
                aimAssistAdjustable = true,
                simplifiedControlScheme = true,
                adaptiveControllerCompatible = true,
                colorblindFilters = listOf("Protanopia", "Deuteranopia", "Tritanopia"),
                highContrastMode = false,
                hudScaling = true,
                textToSpeechScreenReader = false,
                reduceMotionAndFlashes = true,
                scoreLetter = "B+"
            ),
            contentWarnings = ContentWarnings(
                violenceLevel = "Moderada a Alta",
                violenceScore = 4,
                bloodGore = "Salpicaduras balísticas y desintegración de mechas",
                bloodScore = 3,
                horrorLevel = "Ninguno",
                horrorScore = 1,
                strongLanguage = "Moderado",
                languageScore = 3,
                sensitiveThemes = listOf("Guerra futurista interestelar", "Pérdida y sacrificio militar"),
                ageRating = "PEGI 16 / ESRB M",
                detailedSummary = "Disparos frenéticos en primera persona contra soldados y titanes robóticos. Incluye ejecuciones cinemáticas de mechas y algunas muertes de soldados humanos sin desmembramiento gore excesivo."
            ),
            duration = GameDuration(
                mainStoryHours = 6.0f,
                mainPlusExtraHours = 9.0f,
                completionistHours = 15.5f,
                speedrunHours = 3.5f,
                relaxedHours = 18.0f
            ),
            initialRatings = CategoryRatings(
                historia = 4.9f,
                jugabilidad = 5.0f,
                graficos = 4.6f,
                estrategia = 3.8f,
                sonido = 4.8f,
                optimizacion = 4.9f
            ),
            totalCommunityReviews = 2650,
            pricing = GamePricing(
                basePriceUsd = 19.99,
                baseEditionName = "Titanfall 2 Estándar",
                baseIncludes = listOf("Campaña estelar de Jack Cooper y BT-7274", "Modo multijugador completo"),
                deluxePriceUsd = 29.99,
                deluxeEditionName = "Titanfall 2: Ultimate Edition",
                deluxeIncludes = listOf("Desbloqueo inmediato de todos los titanes y tácticas", "Pintura de guerra personalizada Underground", "500 fichas de equipamiento", "10 fichas de XP doble"),
                dlcPriceUsd = 9.99,
                dlcName = "Ultimate Upgrade Pack",
                dlcIncludes = listOf("Desbloqueos instantáneos de titanes y fichas"),
                salePriceUsd = 2.99,
                dealsNote = "¡Históricas rebajas en Steam por tan solo $2.99 a $4.99 (-85% a -90%)!"
            )
        ),
        Game(
            id = "resident_evil_7",
            title = "Resident Evil 7: Biohazard",
            tagline = "El regreso definitivo al terror claustrofóbico en primera persona",
            synopsis = "Ethan Winters se adentra en una decrépita casa de campo en Dulvey, Louisiana, tras recibir un mensaje de su esposa Mia, supuestamente muerta hace 3 años, topándose con la aterradora familia Baker.",
            developer = "Capcom",
            publisher = "Capcom",
            releaseYear = 2017,
            genres = listOf("Survival Horror", "Primera Persona", "Terror Psicológico", "Misterio"),
            bannerColorHex = 0xFF3D5A80,
            coverEmoji = "🏚️",
            minCpuScore = 2,
            recCpuScore = 3,
            minCpuText = "Intel Core i5-4460 3.20GHz o AMD FX-6300",
            recCpuText = "Intel Core i7-3770 3.4GHz o equivalente AMD",
            minGpuScore = 2,
            recGpuScore = 3,
            minGpuText = "NVIDIA GeForce GTX 760 o AMD Radeon R7 260x (2 GB VRAM)",
            recGpuText = "NVIDIA GeForce GTX 1060 (3 GB) o AMD Radeon RX 480 (4 GB)",
            minRamGb = 8,
            recRamGb = 8,
            supportedOs = listOf("Windows 10 (64-bit)", "Windows 11 (64-bit)"),
            storageRequiredGb = 24,
            accessibility = AccessibilityFeatures(
                subtitleSizeAdjustable = true,
                subtitleBackgroundContrast = true,
                speakerIdentification = true,
                directionalSoundCues = true,
                fullSpanishAudioAndSub = true,
                fullButtonRemapping = true,
                toggleVsHoldOption = true,
                aimAssistAdjustable = true,
                simplifiedControlScheme = true,
                adaptiveControllerCompatible = true,
                colorblindFilters = listOf("Filtro de retícula adaptable"),
                highContrastMode = false,
                hudScaling = true,
                textToSpeechScreenReader = false,
                reduceMotionAndFlashes = true,
                scoreLetter = "B"
            ),
            contentWarnings = ContentWarnings(
                violenceLevel = "Alta / Brutal",
                violenceScore = 5,
                bloodGore = "Amputaciones corporales, mutilaciones y vísceras grotescas",
                bloodScore = 5,
                horrorLevel = "Terror extremo, jump scares y claustrofobia agobiante",
                horrorScore = 5,
                strongLanguage = "Explícito y continuo",
                languageScore = 4,
                sensitiveThemes = listOf("Tortura doméstica sádica", "Canibalismo y podredumbre", "Infección biológica parasitaria"),
                ageRating = "PEGI 18 / ESRB M",
                detailedSummary = "Presenta mutilación directa de extremidades (amputación de manos con motosierra), consumo forzado de vísceras putrefactas, decapitación y sobresaltos constantes en pasillos cerrados."
            ),
            duration = GameDuration(
                mainStoryHours = 9.5f,
                mainPlusExtraHours = 12.0f,
                completionistHours = 21.0f,
                speedrunHours = 3.5f,
                relaxedHours = 25.0f
            ),
            initialRatings = CategoryRatings(
                historia = 4.7f,
                jugabilidad = 4.6f,
                graficos = 4.8f,
                estrategia = 4.2f,
                sonido = 4.9f,
                optimizacion = 4.8f
            ),
            totalCommunityReviews = 2100,
            pricing = GamePricing(
                basePriceUsd = 19.99,
                baseEditionName = "Resident Evil 7 Estándar",
                baseIncludes = listOf("Campaña completa de Ethan Winters en la mansión Baker"),
                deluxePriceUsd = 39.99,
                deluxeEditionName = "Resident Evil 7: Gold Edition",
                deluxeIncludes = listOf("DLC Banned Footage Vol. 1", "DLC Banned Footage Vol. 2", "DLC historia End of Zoe", "DLC Not a Hero con Chris Redfield"),
                dlcPriceUsd = 14.99,
                dlcName = "Season Pass",
                dlcIncludes = listOf("Todas las grabaciones inéditas y episodios de historia"),
                salePriceUsd = 7.99,
                dealsNote = "Suele estar en gran rebaja histórica a $7.99 (-60%)"
            )
        ),
        Game(
            id = "resident_evil_2_remake",
            title = "Resident Evil 2 (Remake)",
            tagline = "Sobrevive a la pesadilla zombi en la comisaría de Raccoon City",
            synopsis = "Un virus letal envuelve a los residentes de Raccoon City en septiembre de 1998. El policía novato Leon S. Kennedy y la universitaria Claire Redfield intentan descubrir la verdad tras el brote mientras escapan con vida.",
            developer = "Capcom",
            publisher = "Capcom",
            releaseYear = 2019,
            genres = listOf("Survival Horror", "Tercera Persona", "Zombis", "Acción"),
            bannerColorHex = 0xFF1D3557,
            coverEmoji = "🧟",
            minCpuScore = 2,
            recCpuScore = 3,
            minCpuText = "Intel Core i5-4460 o AMD FX-6300",
            recCpuText = "Intel Core i7-3770 o AMD FX-9590",
            minGpuScore = 2,
            recGpuScore = 3,
            minGpuText = "NVIDIA GeForce GTX 760 o AMD Radeon R7 260x (2 GB)",
            recGpuText = "NVIDIA GeForce GTX 1060 (3 GB) o AMD Radeon RX 480 (4 GB)",
            minRamGb = 8,
            recRamGb = 16,
            supportedOs = listOf("Windows 10 (64-bit)", "Windows 11 (64-bit)"),
            storageRequiredGb = 26,
            accessibility = AccessibilityFeatures(
                subtitleSizeAdjustable = true,
                subtitleBackgroundContrast = true,
                speakerIdentification = true,
                directionalSoundCues = true,
                fullSpanishAudioAndSub = true,
                fullButtonRemapping = true,
                toggleVsHoldOption = true,
                aimAssistAdjustable = true,
                simplifiedControlScheme = true,
                adaptiveControllerCompatible = true,
                colorblindFilters = listOf("Modo contraste para miras y HUD"),
                highContrastMode = false,
                hudScaling = true,
                textToSpeechScreenReader = false,
                reduceMotionAndFlashes = true,
                scoreLetter = "B+"
            ),
            contentWarnings = ContentWarnings(
                violenceLevel = "Alta / Brutal",
                violenceScore = 5,
                bloodGore = "Daño balístico anatómico en tiempo real y desgarramientos",
                bloodScore = 5,
                horrorLevel = "Tensión alta y persecución incesante por Mr. X",
                horrorScore = 4,
                strongLanguage = "Frecuente",
                languageScore = 4,
                sensitiveThemes = listOf("Gore anatómico forense hiperrealista", "Claustrofobia y acoso imparable", "Bioterrorismo"),
                ageRating = "PEGI 18 / ESRB M",
                detailedSummary = "Presenta desmembramiento detallado mediante físicas realistas de impacto de bala, mandíbulas destrozadas, cuerpos devorados por zombis y la constante persecución opresiva de Tyrant (Mr. X)."
            ),
            duration = GameDuration(
                mainStoryHours = 8.5f,
                mainPlusExtraHours = 15.0f,
                completionistHours = 34.0f,
                speedrunHours = 3.0f,
                relaxedHours = 22.0f
            ),
            initialRatings = CategoryRatings(
                historia = 4.8f,
                jugabilidad = 4.9f,
                graficos = 4.9f,
                estrategia = 4.5f,
                sonido = 4.9f,
                optimizacion = 4.9f
            ),
            totalCommunityReviews = 3200,
            pricing = GamePricing(
                basePriceUsd = 39.99,
                baseEditionName = "Resident Evil 2 Remake Estándar",
                baseIncludes = listOf("Campañas completas de Leon S. Kennedy y Claire Redfield"),
                deluxePriceUsd = 49.99,
                deluxeEditionName = "Resident Evil 2: Deluxe Edition",
                deluxeIncludes = listOf("Traje Arklay Sheriff y Noir para Leon", "Traje Militar, Noir y Elza Walker para Claire", "Arma Samurai Edge (Modelo Albert)", "Banda sonora original clásica de 1998"),
                dlcPriceUsd = 9.99,
                dlcName = "Extra DLC Pack",
                dlcIncludes = listOf("Trajes adicionales y banda sonora retro"),
                salePriceUsd = 9.99,
                dealsNote = "Descuento frecuente del 75% a tan solo $9.99 en Steam"
            )
        ),
        Game(
            id = "god_of_war",
            title = "God of War (2018)",
            tagline = "Un nuevo comienzo para Kratos y su hijo Atreus en el reino de los dioses nórdicos",
            synopsis = "Viviendo como un mortal en las tierras de los dioses nórdicos, Kratos debe adaptarse a un territorio desconocido, amenazas inesperadas y una segunda oportunidad para ser padre junto a Atreus.",
            developer = "Santa Monica Studio",
            publisher = "PlayStation PC LLC",
            releaseYear = 2022,
            genres = listOf("Acción", "Aventura", "Mitología Nórdica", "Hack and Slash"),
            bannerColorHex = 0xFF582F0E,
            coverEmoji = "🪓",
            minCpuScore = 2,
            recCpuScore = 3,
            minCpuText = "Intel Core i5-2500k (4 core 3.3 GHz) o AMD Ryzen 3 1200",
            recCpuText = "Intel Core i5-6600k (4 core 3.5 GHz) o AMD Ryzen 5 2400 G",
            minGpuScore = 2,
            recGpuScore = 3,
            minGpuText = "NVIDIA GTX 960 (4 GB) o AMD R9 290X (4 GB)",
            recGpuText = "NVIDIA GTX 1060 (6 GB) o AMD RX 570 (4 GB)",
            minRamGb = 8,
            recRamGb = 16,
            supportedOs = listOf("Windows 10 (64-bit)", "Windows 11 (64-bit)"),
            storageRequiredGb = 70,
            accessibility = AccessibilityFeatures(
                subtitleSizeAdjustable = true,
                subtitleBackgroundContrast = true,
                speakerIdentification = true,
                directionalSoundCues = true,
                fullSpanishAudioAndSub = true,
                fullButtonRemapping = true,
                toggleVsHoldOption = true,
                aimAssistAdjustable = true,
                simplifiedControlScheme = true,
                adaptiveControllerCompatible = true,
                colorblindFilters = listOf("Protanopia", "Deuteranopia", "Tritanopia"),
                highContrastMode = true,
                hudScaling = true,
                textToSpeechScreenReader = false,
                reduceMotionAndFlashes = true,
                scoreLetter = "A"
            ),
            contentWarnings = ContentWarnings(
                violenceLevel = "Alta / Brutal",
                violenceScore = 5,
                bloodGore = "Desmembramiento de criaturas mitológicas y ejecuciones con hacha",
                bloodScore = 4,
                horrorLevel = "Tensión mitológica y monstruos gigantes",
                horrorScore = 2,
                strongLanguage = "Moderado",
                languageScore = 2,
                sensitiveThemes = listOf("Duelo por la pérdida de una madre/esposa", "Violencia familiar divina"),
                ageRating = "PEGI 18 / ESRB M",
                detailedSummary = "Combate contundente en primer plano con el hacha Leviatán, desgarramiento con las manos desnudas de troles, ogros y draugrs con salpicaduras de sangre y decapitaciones fantásticas."
            ),
            duration = GameDuration(
                mainStoryHours = 20.5f,
                mainPlusExtraHours = 32.5f,
                completionistHours = 51.5f,
                speedrunHours = 12.0f,
                relaxedHours = 60.0f
            ),
            initialRatings = CategoryRatings(
                historia = 5.0f,
                jugabilidad = 4.9f,
                graficos = 4.9f,
                estrategia = 4.0f,
                sonido = 5.0f,
                optimizacion = 4.7f
            ),
            totalCommunityReviews = 3800,
            pricing = GamePricing(
                basePriceUsd = 49.99,
                baseEditionName = "God of War Estándar",
                baseIncludes = listOf("Aventura completa de Kratos y Atreus en Midgard"),
                deluxePriceUsd = 59.99,
                deluxeEditionName = "God of War: Digital Deluxe Edition",
                deluxeIncludes = listOf("Conjunto de armadura Death's Vow para Kratos y Atreus", "Escudo Exile's Guardian", "Cómic digital de Dark Horse", "Mini libro de arte digital"),
                dlcPriceUsd = 9.99,
                dlcName = "Digital Deluxe Upgrade",
                dlcIncludes = listOf("Equipamiento cosmético de inicio y contenidos artísticos"),
                salePriceUsd = 24.99,
                dealsNote = "Rebajas frecuentes al 50% ($24.99) en Steam y Epic Games"
            )
        ),
        Game(
            id = "doom_eternal",
            title = "DOOM Eternal",
            tagline = "Lo único que temen los ejércitos del infierno... eres tú",
            synopsis = "Experimenta la combinación definitiva de velocidad y potencia mientras te abres paso a través de dimensiones en un combate implacable en primera persona.",
            developer = "id Software",
            publisher = "Bethesda Softworks",
            releaseYear = 2020,
            genres = listOf("FPS", "Acción Frenética", "Ciencia Ficción", "Gore"),
            bannerColorHex = 0xFF9B2226,
            coverEmoji = "🔥",
            minCpuScore = 2,
            recCpuScore = 3,
            minCpuText = "Intel Core i5 @ 3.3 GHz o AMD Ryzen 3 @ 3.1 GHz",
            recCpuText = "Intel Core i7-6700K o AMD Ryzen 7 1800X",
            minGpuScore = 2,
            recGpuScore = 3,
            minGpuText = "NVIDIA GeForce GTX 1050 Ti (4 GB) o AMD Radeon RX 470 (4 GB)",
            recGpuText = "NVIDIA GeForce GTX 1060 (6 GB) o AMD Radeon RX 480 (8 GB)",
            minRamGb = 8,
            recRamGb = 8,
            supportedOs = listOf("Windows 10 (64-bit)", "Windows 11 (64-bit)"),
            storageRequiredGb = 50,
            accessibility = AccessibilityFeatures(
                subtitleSizeAdjustable = true,
                subtitleBackgroundContrast = true,
                speakerIdentification = true,
                directionalSoundCues = true,
                fullSpanishAudioAndSub = true,
                fullButtonRemapping = true,
                toggleVsHoldOption = true,
                aimAssistAdjustable = true,
                simplifiedControlScheme = true,
                adaptiveControllerCompatible = true,
                colorblindFilters = listOf("Protanopia", "Deuteranopia", "Tritanopia"),
                highContrastMode = true,
                hudScaling = true,
                textToSpeechScreenReader = false,
                reduceMotionAndFlashes = true,
                scoreLetter = "A"
            ),
            contentWarnings = ContentWarnings(
                violenceLevel = "Máxima / Extrema",
                violenceScore = 5,
                bloodGore = "Glory kills con motosierra, mutilaciones y desmembramientos constantes",
                bloodScore = 5,
                horrorLevel = "Imaginería demoníaca infernal",
                horrorScore = 3,
                strongLanguage = "Limpio / Ocasional",
                languageScore = 2,
                sensitiveThemes = listOf("Iconografía demoníaca e infernal", "Hiperviolencia estilizada", "Luces estroboscópicas rápidas"),
                ageRating = "PEGI 18 / ESRB M",
                detailedSummary = "Presenta hiperviolencia constante con 'Glory Kills' donde el jugador parte demonios a la mitad con motosierras, arranca ojos y extremidades en primer plano a velocidades vertiginosas."
            ),
            duration = GameDuration(
                mainStoryHours = 14.5f,
                mainPlusExtraHours = 19.5f,
                completionistHours = 26.0f,
                speedrunHours = 7.0f,
                relaxedHours = 30.0f
            ),
            initialRatings = CategoryRatings(
                historia = 4.0f,
                jugabilidad = 5.0f,
                graficos = 4.9f,
                estrategia = 4.5f,
                sonido = 5.0f,
                optimizacion = 5.0f
            ),
            totalCommunityReviews = 2900,
            pricing = GamePricing(
                basePriceUsd = 39.99,
                baseEditionName = "DOOM Eternal Estándar",
                baseIncludes = listOf("Campaña principal brutal del Doom Slayer en la Tierra y el Infierno"),
                deluxePriceUsd = 69.99,
                deluxeEditionName = "DOOM Eternal: Deluxe Edition",
                deluxeIncludes = listOf("Pase del Año Uno: Expansiones 'The Ancient Gods Parte 1' y 'Parte 2'", "Skin Demonic Slayer para campaña y Battlemode", "Paquete de sonidos clásicos de armas"),
                dlcPriceUsd = 29.99,
                dlcName = "The Ancient Gods Expansion Pass",
                dlcIncludes = listOf("Las dos expansiones de campaña completas"),
                salePriceUsd = 9.99,
                dealsNote = "En rebajas de Bethesda cae a $9.99 el juego base y $17.49 la Deluxe"
            )
        ),
        Game(
            id = "gta_v",
            title = "Grand Theft Auto V",
            tagline = "El fenómeno de Los Santos y el condado de Blaine en su máxima expresión",
            synopsis = "Tres criminales muy diferentes arriesgan todo en una serie de atrevidos y peligrosos atracos que podrían solucionarles la vida para siempre en la soleada y decadente metrópolis de Los Santos.",
            developer = "Rockstar North",
            publisher = "Rockstar Games",
            releaseYear = 2015,
            genres = listOf("Mundo Abierto", "Acción", "Crimen", "Aventura"),
            bannerColorHex = 0xFF2B9348,
            coverEmoji = "🚗",
            minCpuScore = 1,
            recCpuScore = 2,
            minCpuText = "Intel Core 2 Quad CPU Q6600 @ 2.40GHz / AMD Phenom 9850 Quad-Core",
            recCpuText = "Intel Core i5 3470 @ 3.2GHz / AMD X8 FX-8350 @ 4GHz",
            minGpuScore = 1,
            recGpuScore = 2,
            minGpuText = "NVIDIA 9800 GT 1GB / AMD HD 4870 1GB",
            recGpuText = "NVIDIA GTX 660 2GB / AMD HD 7870 2GB",
            minRamGb = 4,
            recRamGb = 8,
            supportedOs = listOf("Windows 10 (64-bit)", "Windows 11 (64-bit)"),
            storageRequiredGb = 110,
            accessibility = AccessibilityFeatures(
                subtitleSizeAdjustable = true,
                subtitleBackgroundContrast = true,
                speakerIdentification = true,
                directionalSoundCues = true,
                fullSpanishAudioAndSub = false,
                fullButtonRemapping = true,
                toggleVsHoldOption = true,
                aimAssistAdjustable = true,
                simplifiedControlScheme = true,
                adaptiveControllerCompatible = true,
                colorblindFilters = listOf("Modo daltónico para radar y retícula"),
                highContrastMode = false,
                hudScaling = true,
                textToSpeechScreenReader = false,
                reduceMotionAndFlashes = true,
                scoreLetter = "B"
            ),
            contentWarnings = ContentWarnings(
                violenceLevel = "Alta / Brutal",
                violenceScore = 4,
                bloodGore = "Heridas de bala, atropellos y sangre realista",
                bloodScore = 4,
                horrorLevel = "Ninguno",
                horrorScore = 1,
                strongLanguage = "Sumamente explícito y constante",
                languageScore = 5,
                sensitiveThemes = listOf("Consumo explícito de drogas y alcohol", "Escena interactiva de interrogatorio/tortura", "Contenido sexual adulto y clubes nocturnos"),
                ageRating = "PEGI 18 / ESRB M",
                detailedSummary = "Contiene lenguaje altamente vulgar, robos a mano armada, tiroteos callejeros, consumo de estupefacientes y una controversial misión interactiva de tortura."
            ),
            duration = GameDuration(
                mainStoryHours = 31.5f,
                mainPlusExtraHours = 48.5f,
                completionistHours = 83.0f,
                speedrunHours = 15.0f,
                relaxedHours = 110.0f
            ),
            initialRatings = CategoryRatings(
                historia = 4.7f,
                jugabilidad = 4.8f,
                graficos = 4.6f,
                estrategia = 3.5f,
                sonido = 4.9f,
                optimizacion = 4.8f
            ),
            totalCommunityReviews = 4100,
            pricing = GamePricing(
                basePriceUsd = 29.99,
                baseEditionName = "Grand Theft Auto V: Premium Edition",
                baseIncludes = listOf("Modo historia completo en Los Santos con Michael, Franklin y Trevor", "Acceso a GTA Online", "Criminal Enterprise Starter Pack"),
                deluxePriceUsd = 49.99,
                deluxeEditionName = "GTA V: Premium + Megalodon Shark Card",
                deluxeIncludes = listOf("Todo el contenido de la Premium Edition", "Tarjeta Tiburón Megalodón con $10,000,000 de saldo GTA$", "Flota de 10 vehículos de alta gama", "Propiedades comerciales y búnker"),
                dlcPriceUsd = 19.99,
                dlcName = "Whale Shark Cash Card ($4,250,000)",
                dlcIncludes = listOf("Saldo digital para GTA Online"),
                salePriceUsd = 14.99,
                dealsNote = "Suele rondar los $14.99 en rebajas periódicas"
            )
        ),
        Game(
            id = "spider_man_remastered",
            title = "Marvel's Spider-Man Remastered",
            tagline = "Siente el poder y la agilidad de Spider-Man balanceándote por Manhattan",
            synopsis = "Peter Parker combate grandes villanos mientras intenta mantener en pie su vida personal. Cuando los Demonios Internos amenazan la Gran Manzana, Peter debe estar dispuesto a alzarse y ser más grande.",
            developer = "Insomniac Games / Nixxes",
            publisher = "PlayStation PC LLC",
            releaseYear = 2022,
            genres = listOf("Acción", "Superhéroes", "Mundo Abierto", "Acrobático"),
            bannerColorHex = 0xFFBA181B,
            coverEmoji = "🕷️",
            minCpuScore = 2,
            recCpuScore = 3,
            minCpuText = "Intel Core i3-4160 @ 3.6 GHz o equivalente AMD",
            recCpuText = "Intel Core i5-4670 @ 3.4 GHz o AMD Ryzen 5 1600",
            minGpuScore = 2,
            recGpuScore = 3,
            minGpuText = "NVIDIA GeForce GTX 950 o AMD Radeon RX 470",
            recGpuText = "NVIDIA GeForce GTX 1060 (6 GB) o AMD Radeon RX 580 (8 GB)",
            minRamGb = 8,
            recRamGb = 16,
            supportedOs = listOf("Windows 10 (64-bit)", "Windows 11 (64-bit)"),
            storageRequiredGb = 75,
            accessibility = AccessibilityFeatures(
                subtitleSizeAdjustable = true,
                subtitleBackgroundContrast = true,
                speakerIdentification = true,
                directionalSoundCues = true,
                fullSpanishAudioAndSub = true,
                fullButtonRemapping = true,
                toggleVsHoldOption = true,
                aimAssistAdjustable = true,
                simplifiedControlScheme = true,
                adaptiveControllerCompatible = true,
                colorblindFilters = listOf("Protanopia", "Deuteranopia", "Tritanopia"),
                highContrastMode = true,
                hudScaling = true,
                textToSpeechScreenReader = true,
                reduceMotionAndFlashes = true,
                scoreLetter = "A+"
            ),
            contentWarnings = ContentWarnings(
                violenceLevel = "Moderada (Estilo Cómic/Superhéroes)",
                violenceScore = 3,
                bloodGore = "Sin sangre ni gore gráfico",
                bloodScore = 1,
                horrorLevel = "Ninguno",
                horrorScore = 1,
                strongLanguage = "Leve",
                languageScore = 2,
                sensitiveThemes = listOf("Ataques terroristas en la urbe", "Enfermedades y virus respiratorios letales", "Pérdida trágica de seres queridos"),
                ageRating = "PEGI 16 / ESRB T",
                detailedSummary = "Combate acrobático contra pandillas criminales con telarañas y golpes contundentes sin sangre letal. Temas sobre pérdidas familiares y un brote químico en Manhattan."
            ),
            duration = GameDuration(
                mainStoryHours = 17.0f,
                mainPlusExtraHours = 25.0f,
                completionistHours = 35.0f,
                speedrunHours = 8.0f,
                relaxedHours = 40.0f
            ),
            initialRatings = CategoryRatings(
                historia = 4.8f,
                jugabilidad = 4.9f,
                graficos = 4.9f,
                estrategia = 3.7f,
                sonido = 4.8f,
                optimizacion = 4.7f
            ),
            totalCommunityReviews = 3300,
            pricing = GamePricing(
                basePriceUsd = 59.99,
                baseEditionName = "Marvel's Spider-Man Remastered",
                baseIncludes = listOf("Aventura completa remasterizada de Peter Parker en Nueva York", "Los 3 capítulos del DLC 'La ciudad que nunca duerme' (The Heist, Turf Wars, Silver Lining)"),
                deluxePriceUsd = 69.99,
                deluxeEditionName = "Marvel's Spider-Man Remastered: Deluxe Bonus",
                deluxeIncludes = listOf("Desbloqueo anticipado de 3 trajes arácnidos (Iron Spider, Velocity, Spider-Punk)", "Dispositivo Spider-Drone desbloqueado desde el inicio", "5 puntos de habilidad para el árbol de combate"),
                dlcPriceUsd = 9.99,
                dlcName = "Pre-order Trajes & Puntos DLC",
                dlcIncludes = listOf("Trajes cosméticos y puntos de habilidad"),
                salePriceUsd = 35.99,
                dealsNote = "Descuentos en Steam a $35.99 (-40%)"
            )
        )
    )

    fun evaluateCompatibility(game: Game, specs: HardwareSpecs): CompatibilityEvaluation {
        val cpuPassRec = specs.cpuScore >= game.recCpuScore
        val cpuPassMin = specs.cpuScore >= game.minCpuScore
        
        val gpuPassRec = specs.gpuScore >= game.recGpuScore
        val gpuPassMin = specs.gpuScore >= game.minGpuScore
        
        val ramPassRec = specs.ramGb >= game.recRamGb
        val ramPassMin = specs.ramGb >= game.minRamGb
        
        val osPass = game.supportedOs.any { it.contains(specs.os.split(" ")[0], ignoreCase = true) || specs.os.contains("Windows") && it.contains("Windows") }
        val storagePass = specs.storageGb >= game.storageRequiredGb

        // Component individual percentage calculation (0 to 100%)
        val gpuScorePct = when {
            gpuPassRec -> 100
            gpuPassMin -> 70
            else -> ((specs.gpuScore.toFloat() / game.minGpuScore) * 50).toInt().coerceIn(10, 55)
        }

        val cpuScorePct = when {
            cpuPassRec -> 100
            cpuPassMin -> 70
            else -> ((specs.cpuScore.toFloat() / game.minCpuScore) * 50).toInt().coerceIn(15, 55)
        }

        val ramScorePct = when {
            ramPassRec -> 100
            ramPassMin -> 70
            else -> ((specs.ramGb.toFloat() / game.minRamGb) * 55).toInt().coerceIn(10, 55)
        }

        val storageScorePct = when {
            storagePass -> 100
            else -> ((specs.storageGb.toFloat() / game.storageRequiredGb) * 70).toInt().coerceIn(0, 70)
        }

        // Weighted overall percentage:
        // GPU: 35%, CPU: 25%, RAM: 20%, Storage: 10%, OS: 10%
        val gpuWeightPts = when {
            gpuPassRec -> 35
            gpuPassMin -> 21
            else -> ((specs.gpuScore.toFloat() / game.minGpuScore) * 12).toInt().coerceAtLeast(3)
        }
        val cpuWeightPts = when {
            cpuPassRec -> 25
            cpuPassMin -> 16
            else -> ((specs.cpuScore.toFloat() / game.minCpuScore) * 9).toInt().coerceAtLeast(2)
        }
        val ramWeightPts = when {
            ramPassRec -> 20
            ramPassMin -> 13
            else -> ((specs.ramGb.toFloat() / game.minRamGb) * 8).toInt().coerceAtLeast(2)
        }
        val storageWeightPts = if (storagePass) 10 else ((specs.storageGb.toFloat() / game.storageRequiredGb) * 6).toInt().coerceIn(0, 6)
        val osWeightPts = if (osPass) 10 else 0

        var rawPercentage = (gpuWeightPts + cpuWeightPts + ramWeightPts + storageWeightPts + osWeightPts).coerceIn(5, 100)

        // Strict thresholds according to user requirement:
        // Verde: 75% a 100%
        // Amarillo: 50% a 75%
        // Rojo: 0% a 50%
        // If a critical component (GPU or RAM or CPU) fails minimum, it must never reach 75% (and if multiple fail, cap under 50%)
        if (!gpuPassMin || !ramPassMin || !cpuPassMin || !storagePass) {
            if (!gpuPassMin || !ramPassMin) {
                rawPercentage = rawPercentage.coerceAtMost(48) // Red tier
            } else {
                rawPercentage = rawPercentage.coerceIn(50, 72) // Yellow tier
            }
        }

        val bottlenecks = mutableListOf<String>()
        if (!cpuPassMin) bottlenecks.add("Procesador (CPU) insuficiente")
        if (!gpuPassMin) bottlenecks.add("Tarjeta gráfica (GPU) insuficiente")
        if (!ramPassMin) bottlenecks.add("Memoria RAM insuficiente (${specs.ramGb}GB de ${game.minRamGb}GB requeridos)")
        if (!storagePass) bottlenecks.add("Espacio insuficiente (${specs.storageGb}GB disponibles de ${game.storageRequiredGb}GB)")
        if (!osPass) bottlenecks.add("Sistema Operativo no compatible")

        val status = when {
            rawPercentage >= 75 -> CompatibilityStatus.RUNS_GREAT
            rawPercentage >= 50 -> CompatibilityStatus.RUNS_MEDIUM
            else -> CompatibilityStatus.RUNS_NO
        }

        return CompatibilityEvaluation(
            status = status,
            percentage = rawPercentage,
            cpuPassMin = cpuPassMin,
            cpuPassRec = cpuPassRec,
            gpuPassMin = gpuPassMin,
            gpuPassRec = gpuPassRec,
            ramPassMin = ramPassMin,
            ramPassRec = ramPassRec,
            osPass = osPass,
            storagePass = storagePass,
            bottlenecks = bottlenecks,
            cpuScorePct = cpuScorePct,
            gpuScorePct = gpuScorePct,
            ramScorePct = ramScorePct,
            storageScorePct = storageScorePct
        )
    }
}
