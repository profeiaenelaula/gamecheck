package com.example.data.datasource

import com.example.data.model.LatestUpdateInfo
import com.example.data.model.PlatformComparison
import com.example.data.model.PlatformPerformance

object GamePlatformAndUpdatesData {

    fun getLatestUpdate(gameId: String): LatestUpdateInfo {
        return updatesMap[gameId] ?: LatestUpdateInfo(
            version = "Parche Reciente",
            releaseDate = "2024",
            headline = "Mejoras de rendimiento y optimizaciones del motor",
            changesAdded = listOf(
                "Ajustes de estabilidad en combates intensos",
                "Reducción de consumo de memoria VRAM",
                "Corrección de fallos en guardado en la nube"
            ),
            performanceImpact = "Aumento de fluidez general de entre 5% y 10% en equipos compatibles."
        )
    }

    fun getPlatformComparison(gameId: String): PlatformComparison {
        return platformMap[gameId] ?: PlatformComparison(
            recommendedPlatform = "PlayStation 5 o PC",
            buyingRecommendation = "Recomendamos comprarlo en PS5 para una experiencia inmediata a 60 FPS estables, o en PC si cuentas con tarjeta gráfica dedicada y deseas aprovechar tasas de refresco superiores y mods.",
            pcPerformance = PlatformPerformance(
                platformName = "PC",
                fpsTarget = "60 - 120+ FPS",
                resolution = "Hasta 4K escalable",
                loadingTime = "Ultrarrápido (< 5 seg en SSD)",
                stabilityBadge = "Excelente",
                pros = listOf("Personalización gráfica total", "Soporte de mods y resolución ultrawide", "Tasa de refresco variable"),
                cons = listOf("Depende de tu hardware"),
                score = 9
            ),
            ps4Performance = PlatformPerformance(
                platformName = "PlayStation 4",
                fpsTarget = "30 FPS",
                resolution = "1080p nativo",
                loadingTime = "Lento (35 - 55 seg en HDD)",
                stabilityBadge = "Ajustado",
                pros = listOf("Compatible con consola base"),
                cons = listOf("Cargas lentas", "Sin modo 60 FPS"),
                score = 6
            ),
            ps5Performance = PlatformPerformance(
                platformName = "PlayStation 5",
                fpsTarget = "60 FPS",
                resolution = "1440p / 4K reescalado",
                loadingTime = "Instantáneo (< 6 seg)",
                stabilityBadge = "Rendimiento Óptimo",
                pros = listOf("60 FPS sólidos", "Tiempos de carga mínimos", "Gatillos hápticos DualSense"),
                cons = listOf("Sin soporte de modificaciones comunitarias"),
                score = 9
            )
        )
    }

    private val updatesMap = mapOf(
        "cyberpunk_2077" to LatestUpdateInfo(
            version = "Parche 2.13",
            releaseDate = "Septiembre 2024",
            headline = "Integración de AMD FSR 3 con Frame Generation e Intel XeSS 1.3",
            changesAdded = listOf(
                "Añadido soporte oficial para AMD FidelityFX Super Resolution 3 con generación de fotogramas",
                "Soporte actualizado para Intel XeSS 1.3 con mejor reconstrucción temporal",
                "Corrección de anomalías en el trazado de trayectorias (Path Tracing) en vehículos",
                "Optimizaciones de estabilidad en procesadores con arquitectura híbrida (Intel 12ª-14ª gen)"
            ),
            performanceImpact = "Hasta un +60% de fluidez percibida al activar Frame Generation en GPUs compatibles."
        ),
        "elden_ring" to LatestUpdateInfo(
            version = "Parche 1.14 (Shadow of the Erdtree)",
            releaseDate = "Septiembre 2024",
            headline = "Rebalanceo de jefes finales, hitbox ajustadas y optimización de efectos de partículas",
            changesAdded = listOf(
                "Ajuste en la cadencia de ataques y rango del jefe final del DLC en fases 1 y 2",
                "Optimización de tasa de cuadros en áreas densas de agua y vegetación en el Reino de las Sombras",
                "Aumento de velocidad de inicio de invocaciones de espíritus ceniza",
                "Mejora en la sincronización de animaciones de contraataque con escudo"
            ),
            performanceImpact = "Eliminación de caídas bruscas de FPS en las arenas de jefes del DLC."
        ),
        "baldur_gate_3" to LatestUpdateInfo(
            version = "Parche 7 (Gran Actualización)",
            releaseDate = "Septiembre 2024",
            headline = "Gestor oficial de Mods integrado, nuevos finales cinemáticos malvados y pantalla dividida mejorada",
            changesAdded = listOf(
                "Gestor oficial de mods multiplataforma con descarga directa en el menú",
                "Nuevas cinemáticas y escenas de desenlace para rutas de personajes malvados",
                "Modo cooperativo en pantalla dividida dinámico que se fusiona al estar cerca",
                "Optimizaciones profundas en el Acto 3 para procesadores de gama media"
            ),
            performanceImpact = "Aumento de 15 FPS promedio en la ciudad baja de Baldur's Gate (Acto 3)."
        ),
        "silent_hill_2_remake" to LatestUpdateInfo(
            version = "Parche 1.05",
            releaseDate = "Noviembre 2024",
            headline = "Corrección del rompecabezas del laberinto y optimización de renderizado en Unreal Engine 5",
            changesAdded = listOf(
                "Solución al bloqueo de progreso en el rompecabezas del cubo del laberinto",
                "Optimizaciones en la niebla volumétrica y reducción de ruido en reflejos Lumen",
                "Mejoras de respuesta táctil DualSense en el sonido de la radio y pasos en charcos",
                "Añadido selector de límite de 60/120 FPS en PC"
            ),
            performanceImpact = "Mayor estabilidad a 60 FPS en el Modo Rendimiento de PS5 y menos stuttering en PC."
        ),
        "hades_2" to LatestUpdateInfo(
            version = "The Olympic Update",
            releaseDate = "Octubre 2024",
            headline = "Nueva región Cima del Olimpo, deidad Atenea, nueva arma 'Capa Negra' y familiares animales",
            changesAdded = listOf(
                "Nueva región entera: la Cima del Monte Olimpo con nuevos enemigos y jefes",
                "Nueva arma nocturna: la Capa Negra (Xinth) con ataques de bombardeo",
                "Dos nuevos familiares animales: Raki el cuervo y Hécuba el sabueso",
                "Más de 2500 nuevas líneas de diálogo grabadas y música original de Darren Korb"
            ),
            performanceImpact = "144+ FPS garantizados incluso en portátiles de entrada con gráficos integrados."
        ),
        "hollow_knight" to LatestUpdateInfo(
            version = "Voidheart v1.5",
            releaseDate = "Edición Definitiva",
            headline = "Optimización de latencia en mandos y soporte nativo para monitores de alta frecuencia",
            changesAdded = listOf(
                "Inclusión directa de los 4 paquetes de contenido: Hidden Dreams, The Grimm Troupe, Lifeblood y Godmaster",
                "Latencia de entrada de salto y ataque reducida a menos de 8 ms",
                "Resolución 4K nativa nítida con 60 FPS en consolas y hasta 240 Hz en PC",
                "Añadidos menús de logros internos y galería de arte del Reino de Hallownest"
            ),
            performanceImpact = "Fluidez impecable y perfecta respuesta de salto en plataformas."
        ),
        "celeste" to LatestUpdateInfo(
            version = "Capítulo 9: Farewell v1.4",
            releaseDate = "Actualización Final",
            headline = "100 nuevas pantallas de desafío, mecánicas de impulsos y ampliación del modo asistencia",
            changesAdded = listOf(
                "Capítulo 9 Farewell completo con más de 40 minutos de nueva banda sonora de Lena Raine",
                "Nuevas mecánicas: plataformas de ondas de choque y aves flotantes de doble impulso",
                "Opciones ampliadas de modo asistencia: velocidad de juego ajustable del 50% al 100%",
                "Compatibilidad nativa con pantallas Steam Deck a 90 Hz"
            ),
            performanceImpact = "Cero caídas de cuadros en cualquier dispositivo."
        ),
        "red_dead_redemption_2" to LatestUpdateInfo(
            version = "Parche 1.32",
            releaseDate = "Marzo 2024",
            headline = "Soporte para AMD FSR 2.2, HDR10+ Gaming y parches de estabilidad de Red Dead Online",
            changesAdded = listOf(
                "Implementación oficial de AMD FidelityFX Super Resolution 2.2 en PC",
                "Soporte para calibración avanzada de brillo HDR10+ Gaming en pantallas OLED",
                "Corrección de desconexiones erráticas de servidores en Red Dead Online",
                "Resolución de fallos en texturas de pelaje y agua en configuraciones gráficas ultra"
            ),
            performanceImpact = "Ganancia de entre 15 y 25 FPS con FSR Calidad en resoluciones 1440p y 4K."
        ),
        "stardew_valley" to LatestUpdateInfo(
            version = "Gran Actualización 1.6",
            releaseDate = "2024",
            headline = "Nuevo festival del desierto, granja Meadowlands, multijugador para 8 jugadores y mascotas",
            changesAdded = listOf(
                "Nuevo festival temático de 3 días en el Desierto de Calico",
                "Nuevo tipo de granja 'Meadowlands' con pasto masticable para animales de crianza",
                "Soporte ampliado para multijugador cooperativo de hasta 8 jugadores en PC",
                "Nuevos diálogos estacionales, sombreros para mascotas y sistema de maestrías de nivel 10+"
            ),
            performanceImpact = "Tiempos de carga de guardado casi instantáneos."
        ),
        "alan_wake_2" to LatestUpdateInfo(
            version = "The Lake House & Aniversario",
            releaseDate = "Octubre 2024",
            headline = "Segunda expansión oficial 'The Lake House', inversión de ejes y optimización del menú de inventario",
            changesAdded = listOf(
                "Soporte para la expansión The Lake House protagonizada por la agente Estevez del FBC",
                "Inversión de ejes horizontal y vertical en ratón y mando",
                "Añadido selector de ayuda visual para acertijos de interruptores de luz",
                "Soporte mejorado para DLSS Ray Reconstruction y renderizado de mallas de geometría (Mesh Shaders)"
            ),
            performanceImpact = "Aumento de 10% de FPS en GPUs RTX de serie 40 y modo rendimiento de PS5."
        ),
        "the_last_of_us_part_1" to LatestUpdateInfo(
            version = "Parche 1.1.3",
            releaseDate = "Julio 2024",
            headline = "Optimización drástica de uso de memoria VRAM y compilación acelerada de shaders",
            changesAdded = listOf(
                "Reducción del tiempo de compilación inicial de shaders a menos de 4 minutos",
                "Consumo de memoria de video (VRAM) optimizado en más de 1.5 GB en ajustes altos",
                "Soporte háptico DualSense cableado e inalámbrico en PC",
                "Corrección de anomalías en el movimiento de la linterna y reflejos en charcos"
            ),
            performanceImpact = "Eliminación del stuttering al girar rápido la cámara en PC."
        ),
        "titanfall_2" to LatestUpdateInfo(
            version = "Revival Server Patch",
            releaseDate = "Actualización de Red",
            headline = "Servidores multijugador oficiales completamente restaurados y rotación de modos clásicos",
            changesAdded = listOf(
                "Reparación integral de servidores oficiales contra ataques DoS y desconexiones",
                "Regreso de la rotación de modos favoritos: Atrición, Marcado para Morir y Defensa de la Frontera",
                "Compatibilidad impecable con monitores de 144 Hz, 165 Hz y 240 Hz",
                "Tiempos de búsqueda de partida multijugador reducidos a menos de 30 segundos"
            ),
            performanceImpact = "60 FPS clavados en consolas y 144-240 FPS competitivos en PC."
        ),
        "resident_evil_7" to LatestUpdateInfo(
            version = "Next-Gen Upgrade Patch",
            releaseDate = "Actualización Gratuita",
            headline = "Trazado de rayos nativo, audio 3D Tempest y tiempos de carga instantáneos",
            changesAdded = listOf(
                "Actualización nativa para PlayStation 5 y PC con Ray Tracing en sombras y reflejos",
                "Implementación de Audio Espacial 3D para localizar a la familia Baker por sus pasos",
                "Gatillos adaptativos del DualSense que simulan la resistencia del gatillo de la escopeta",
                "Transferencia de partidas guardadas de PS4 a PS5"
            ),
            performanceImpact = "4K a 60 FPS estables en PS5 y más de 120 FPS en PCs modernas."
        ),
        "resident_evil_2_remake" to LatestUpdateInfo(
            version = "Next-Gen & 120 FPS Patch",
            releaseDate = "Actualización Gratuita",
            headline = "Modo 120 Hz de alta tasa de refresco, Ray Tracing y respuesta háptica en la comisaría",
            changesAdded = listOf(
                "Modo de alta tasa de refresco de hasta 120 FPS en pantallas HDMI 2.1 compatibles",
                "Trazado de rayos en charcos de sangre y cristales de la comisaría de Raccoon City",
                "Resistencia de gatillos DualSense al disparar la pistola Matilda y el lanzagranadas",
                "Audio 3D binaural direccional que delata los pasos pesados de Mr. X"
            ),
            performanceImpact = "Opción de jugar a 120 FPS en PS5 y PC para máxima agilidad de apuntado."
        ),
        "god_of_war" to LatestUpdateInfo(
            version = "Parche v1.0.12 PC",
            releaseDate = "Actualización Oficial",
            headline = "Soporte para AMD FSR 2.0, NVIDIA Reflex para baja latencia y monitores ultrawide 21:9",
            changesAdded = listOf(
                "Integración de AMD FidelityFX Super Resolution 2.0 con excelente calidad de imagen",
                "Soporte para NVIDIA Reflex reduciendo la latencia de respuesta del hacha Leviatán",
                "Soporte panorámico nativo para monitores ultrawide (21:9) y super ultrawide (32:9)",
                "Resolución de fallos de memoria en tarjetas gráficas AMD Radeon RX"
            ),
            performanceImpact = "60 FPS a 4K en PS5 (parche gratuito) y más de 100 FPS en PCs de gama media."
        ),
        "doom_eternal" to LatestUpdateInfo(
            version = "Update 6.66 (Modo Horda)",
            releaseDate = "Actualización Definitiva",
            headline = "Modo Horda arcade, dos nuevos niveles maestros y Ray Tracing a 60 y 120 FPS",
            changesAdded = listOf(
                "Nuevo Modo Horda arcade con puntuaciones competitivas y desafíos por oleadas",
                "Niveles Maestros adicionales: Núcleo de Marte y Lanza del Mundo",
                "Modo Ray Tracing a 60 FPS y Modo Rendimiento a 120 FPS en PlayStation 5",
                "Compatibilidad con DLSS en PC logrando hasta 250 FPS en hardware moderno"
            ),
            performanceImpact = "Uno de los motores (id Tech 7) mejor optimizados de toda la industria."
        ),
        "gta_v" to LatestUpdateInfo(
            version = "Bottom Dollar Bounties",
            releaseDate = "Verano 2024",
            headline = "Nueva agencia de recompensas con Maude Eccles, vehículos patrulla policiales y carreras DRIFT",
            changesAdded = listOf(
                "Negocio de ejecución de fianzas y capturas con la hija de Maude, Jenette",
                "Adición de nuevos vehículos policiales interceptores modificables en el taller",
                "Nuevas mejoras de rendimiento HSW exclusivas de nueva generación en PS5",
                "Filtro de llamadas telefónicas y menú de interrupción de misiones mejorado"
            ),
            performanceImpact = "En PS5 carga en 15 segundos y corre a 60 FPS con Ray Tracing en reflejos."
        ),
        "spider_man_remastered" to LatestUpdateInfo(
            version = "Parche v2.2 PC",
            releaseDate = "Actualización Oficial",
            headline = "Optimización de Ray Tracing en CPUs multinúcleo, soporte Intel XeSS y mejoras para Steam Deck",
            changesAdded = listOf(
                "Optimización de trazado de rayos en rascacielos de Manhattan sin saturar los hilos de la CPU",
                "Compatibilidad oficial con Intel XeSS y escalado dinámico de resolución",
                "Ajustes de interfaz adaptados a la pantalla de Steam Deck y Asus ROG Ally",
                "Soporte completo de respuesta táctil y gatillos adaptativos DualSense en PC y PS5"
            ),
            performanceImpact = "Balanceo fluido por la ciudad a 60 FPS o 120 FPS sin parones."
        )
    )

    private val platformMap = mapOf(
        "cyberpunk_2077" to PlatformComparison(
            recommendedPlatform = "PC (con RTX) o PlayStation 5",
            buyingRecommendation = "Comprar en PlayStation 5 si buscas comodidad en TV con 60 FPS estables y DualSense, o en PC si cuentas con una GPU moderna (RTX 3060 en adelante) para disfrutar del espectacular Path Tracing y mods. ⚠️ Evita comprarlo en PS4 a toda costa.",
            pcPerformance = PlatformPerformance(
                platformName = "PC",
                fpsTarget = "60 - 120+ FPS (con DLSS / FSR)",
                resolution = "Hasta 4K nativo / DLSS",
                loadingTime = "Ultrarrápido (< 4 seg en NVMe)",
                stabilityBadge = "Experiencia Máxima (10/10)",
                pros = listOf("Trazado de rayos y Path Tracing completo", "Comunidad gigantesca de mods", "Soporte FSR 3 / DLSS 3.7"),
                cons = listOf("Exige hardware potente para gráficos ultra"),
                score = 10
            ),
            ps4Performance = PlatformPerformance(
                platformName = "PlayStation 4",
                fpsTarget = "20 - 28 FPS (Caídas severas)",
                resolution = "720p - 900p dinámico borroso",
                loadingTime = "Lento (55 - 85 seg en HDD)",
                stabilityBadge = "No Recomendado (3/10)",
                pros = listOf("Precio bajo de segunda mano"),
                cons = listOf("No incluye la expansión Phantom Liberty", "Población casi vacía", "Texturas tardan en cargar"),
                score = 3
            ),
            ps5Performance = PlatformPerformance(
                platformName = "PlayStation 5",
                fpsTarget = "60 FPS (Rendimiento) / 30 FPS (RT)",
                resolution = "1440p dinámico / 4K reescalado",
                loadingTime = "Instantáneo (< 5 seg en SSD)",
                stabilityBadge = "Rendimiento Óptimo (9/10)",
                pros = listOf("60 FPS muy estables", "Gatillos hápticos en armas y autos", "Incluye Phantom Liberty"),
                cons = listOf("Sin soporte de mods comunitarios"),
                score = 9
            )
        ),
        "elden_ring" to PlatformComparison(
            recommendedPlatform = "PlayStation 5 o PC",
            buyingRecommendation = "Recomendamos comprarlo en PS5 para jugar sin complicaciones con tiempos de carga de 6 segundos tras morir contra jefes, o en PC si quieres mods (como el mod cooperativo sin restricciones Seamless Co-op). En PS4 es jugable a 30 FPS pero sufre caídas a caballo.",
            pcPerformance = PlatformPerformance(
                platformName = "PC",
                fpsTarget = "60 FPS (bloqueado por motor) / 120 con mods",
                resolution = "Hasta 4K nativo",
                loadingTime = "Muy rápido (4 - 7 seg)",
                stabilityBadge = "Excelente (9/10)",
                pros = listOf("Mod Seamless Co-op para jugar con amigos", "Soporte de resoluciones ultrawide con mods", "Ray Tracing opcional"),
                cons = listOf("Motor bloqueado a 60 FPS por defecto"),
                score = 9
            ),
            ps4Performance = PlatformPerformance(
                platformName = "PlayStation 4",
                fpsTarget = "28 - 30 FPS (Tirones al montar a caballo)",
                resolution = "1080p dinámico",
                loadingTime = "Lento (25 - 45 seg entre muertes)",
                stabilityBadge = "Jugable pero Ajustado (6/10)",
                pros = listOf("Acceso a todo el juego y expansión"),
                cons = listOf("Tiempos de carga frustrantes tras morir", "Tasa de cuadros inestable"),
                score = 6
            ),
            ps5Performance = PlatformPerformance(
                platformName = "PlayStation 5",
                fpsTarget = "55 - 60 FPS (Modo Rendimiento)",
                resolution = "1620p dinámico / 4K",
                loadingTime = "Instantáneo (5 - 6 seg)",
                stabilityBadge = "Rendimiento Óptimo (9/10)",
                pros = listOf("Cargas ultrarrápidas", "Compatibilidad con versión PS4 Pro a 60 FPS clavados", "Excelente soporte de mando"),
                cons = listOf("Ligeras fluctuaciones a 55 FPS en zonas abiertas"),
                score = 9
            )
        ),
        "baldur_gate_3" to PlatformComparison(
            recommendedPlatform = "PC (Mejor con Teclado/Ratón y Mods)",
            buyingRecommendation = "La mejor versión sin duda es PC debido a la inmensa cantidad de hechizos e inventario que se gestionan mucho más rápido con ratón, además de la compatibilidad total con mods. En PS5 la adaptación a mando es sobresaliente y el Acto 3 ya está optimizado.",
            pcPerformance = PlatformPerformance(
                platformName = "PC",
                fpsTarget = "60 - 120+ FPS",
                resolution = "Hasta 4K nativo",
                loadingTime = "Ultrarrápido (< 6 seg)",
                stabilityBadge = "Experiencia Definitiva (10/10)",
                pros = listOf("Control con ratón insuperable para CRPG", "Miles de mods y clases comunitarias", "Soporte FSR 2.2 y DLSS"),
                cons = listOf("Acto 3 exige una buena CPU"),
                score = 10
            ),
            ps4Performance = null, // No salió en PS4
            ps5Performance = PlatformPerformance(
                platformName = "PlayStation 5",
                fpsTarget = "60 FPS (Rendimiento) / 30 FPS (Calidad)",
                resolution = "1440p / 4K dinámico",
                loadingTime = "Rápido (8 - 12 seg)",
                stabilityBadge = "Excelente en Consola (9/10)",
                pros = listOf("Menús radiales de mando muy intuitivos", "Cooperativo local a pantalla dividida", "Soporte oficial de mods"),
                cons = listOf("Gestión de inventario algo más lenta que con ratón"),
                score = 9
            )
        ),
        "silent_hill_2_remake" to PlatformComparison(
            recommendedPlatform = "PlayStation 5 o PC con GPU de 8GB+ VRAM",
            buyingRecommendation = "Recomendamos comprarlo en PS5 si quieres la atmósfera sonora más inmersiva gracias al audio 3D y la radio sonando en el altavoz del DualSense. En PC luce increíble con trazado de rayos pero requiere al menos una RTX 3060 / RX 6700 para evitar stuttering de Unreal Engine 5.",
            pcPerformance = PlatformPerformance(
                platformName = "PC",
                fpsTarget = "60 - 90 FPS (con DLSS / FSR)",
                resolution = "Hasta 4K con escalador",
                loadingTime = "Ultrarrápido (< 5 seg)",
                stabilityBadge = "Excelente con buena GPU (9/10)",
                pros = listOf("Reflejos e iluminación Lumen en máxima calidad", "DLSS 3 y FSR 3", "Sin aberración cromática opcional"),
                cons = listOf("Exige al menos 8 GB de VRAM"),
                score = 9
            ),
            ps4Performance = null, // Exclusivo nueva generación
            ps5Performance = PlatformPerformance(
                platformName = "PlayStation 5",
                fpsTarget = "60 FPS (Rendimiento) / 30 FPS (Calidad)",
                resolution = "1080p escalado a 4K",
                loadingTime = "Instantáneo (5 seg)",
                stabilityBadge = "Rendimiento Óptimo (9/10)",
                pros = listOf("Radio de advertencia y niebla en el altavoz del mando", "Audio 3D binaural espeluznante", "60 FPS en Modo Rendimiento"),
                cons = listOf("Ligero efecto borroso por escalado dinámico en modo 60 FPS"),
                score = 9
            )
        ),
        "hades_2" to PlatformComparison(
            recommendedPlatform = "PC (Steam / Epic Games)",
            buyingRecommendation = "Actualmente en Acceso Anticipado en PC, con un rendimiento impecable a 144+ FPS incluso en portátiles modestos o Steam Deck. Si tienes PC o consola portátil PC, cómpralo allí para recibir todas las expansiones y contenidos nuevos de inmediato.",
            pcPerformance = PlatformPerformance(
                platformName = "PC",
                fpsTarget = "144 - 240+ FPS",
                resolution = "Hasta 4K nativo impecable",
                loadingTime = "Instantáneo (< 2 seg)",
                stabilityBadge = "Perfecto (10/10)",
                pros = listOf("Actualizaciones inmediatas cada pocas semanas", "Corre en casi cualquier PC", "Soporte nativo para Steam Deck a 90 Hz"),
                cons = listOf("El juego aún está en desarrollo activo"),
                score = 10
            ),
            ps4Performance = null, // Llegará en lanzamiento 1.0
            ps5Performance = PlatformPerformance(
                platformName = "PlayStation 5",
                fpsTarget = "60 - 120 FPS (previsto)",
                resolution = "4K nativo",
                loadingTime = "Instantáneo",
                stabilityBadge = "Próximamente en Consolas",
                pros = listOf("Llegará optimizado en su versión final 1.0"),
                cons = listOf("Aún no disponible en PlayStation Store"),
                score = 8
            )
        ),
        "red_dead_redemption_2" to PlatformComparison(
            recommendedPlatform = "PC (para 60-120 FPS) o PlayStation 5",
            buyingRecommendation = "Si tienes una PC con tarjeta gráfica de gama media o superior, cómpralo en PC: es la única forma de jugarlo a 60 FPS o más con fidelidad gráfica hiperrealista y mods. En PS5 corre a 30 FPS estables y carga rápido, mientras que en PS4 los tiempos de carga superan el minuto.",
            pcPerformance = PlatformPerformance(
                platformName = "PC",
                fpsTarget = "60 - 120 FPS",
                resolution = "Hasta 4K nativo con FSR / DLSS",
                loadingTime = "Rápido (12 - 18 seg en SSD)",
                stabilityBadge = "Gráficos de Referencia (10/10)",
                pros = listOf("Única plataforma con 60+ FPS oficiales", "Mods fotorrealistas y de vida del salvaje oeste", "Soporte ultrawide"),
                cons = listOf("Ocupa 120 GB de almacenamiento"),
                score = 10
            ),
            ps4Performance = PlatformPerformance(
                platformName = "PlayStation 4",
                fpsTarget = "30 FPS",
                resolution = "1080p nativo",
                loadingTime = "Lento (60 - 90 seg al iniciar partida)",
                stabilityBadge = "Aceptable pero Lento (7/10)",
                pros = listOf("Totalmente jugable a 30 FPS"),
                cons = listOf("Tiempos de carga eternos", "Ventilador de PS4 suele sonar muy fuerte"),
                score = 7
            ),
            ps5Performance = PlatformPerformance(
                platformName = "PlayStation 5",
                fpsTarget = "30 FPS (Retrocompatibilidad)",
                resolution = "4K por tablero de ajedrez (versión PS4 Pro)",
                loadingTime = "Rápido (15 - 20 seg)",
                stabilityBadge = "Estable a 30 FPS (8/10)",
                pros = listOf("30 FPS completamente sólidos como una roca", "Carga 4 veces más rápido que en PS4", "Silenciosa"),
                cons = listOf("Rockstar aún no ha lanzado parche de 60 FPS nativo"),
                score = 8
            )
        ),
        "stardew_valley" to PlatformComparison(
            recommendedPlatform = "PC (por Mods y Actualización 1.6) o PlayStation 5/4",
            buyingRecommendation = "En PC es la plataforma reina gracias al ecosistema SMAPI con miles de expansiones gratuitas (como Stardew Valley Expanded) y por recibir las actualizaciones antes que nadie. En PS4 y PS5 es perfecto para relajarse en el sofá.",
            pcPerformance = PlatformPerformance(
                platformName = "PC",
                fpsTarget = "60 - 144+ FPS",
                resolution = "Cualquier resolución",
                loadingTime = "Instantáneo (< 2 seg)",
                stabilityBadge = "Impecable (10/10)",
                pros = listOf("Miles de mods y expansiones gratuitas", "Actualizaciones inmediatas", "Cooperativo para 8 jugadores"),
                cons = listOf("Ninguno"),
                score = 10
            ),
            ps4Performance = PlatformPerformance(
                platformName = "PlayStation 4",
                fpsTarget = "60 FPS",
                resolution = "1080p nativo",
                loadingTime = "Rápido (4 - 6 seg)",
                stabilityBadge = "Excelente (9/10)",
                pros = listOf("Pantalla dividida para 2 jugadores", "60 FPS fluidos"),
                cons = listOf("Las actualizaciones mayores tardan más en llegar"),
                score = 9
            ),
            ps5Performance = PlatformPerformance(
                platformName = "PlayStation 5",
                fpsTarget = "60 FPS",
                resolution = "4K nativo nítido",
                loadingTime = "Instantáneo (< 2 seg)",
                stabilityBadge = "Rendimiento Óptimo (9/10)",
                pros = listOf("Cero ruido, consumo mínimo de energía", "Partidas cooperativas en TV"),
                cons = listOf("Sin soporte de mods"),
                score = 9
            )
        ),
        "alan_wake_2" to PlatformComparison(
            recommendedPlatform = "PC (con RTX 40 series) o PlayStation 5",
            buyingRecommendation = "Si tienes una GPU potente con arquitectura Ada Lovelace (RTX 4070+) obtendrás la cumbre visual de los videojuegos con Ray Tracing completo. Si no tienes una PC de última generación, la versión de PlayStation 5 es fantástica y ofrece un modo a 60 FPS muy bien conseguido.",
            pcPerformance = PlatformPerformance(
                platformName = "PC",
                fpsTarget = "60 - 100+ FPS (con DLSS)",
                resolution = "Hasta 4K escalado",
                loadingTime = "Ultrarrápido (< 5 seg en SSD)",
                stabilityBadge = "Portento Técnico (10/10)",
                pros = listOf("Mejor implementación de iluminación del mercado", "DLSS 3.7 y Ray Reconstruction"),
                cons = listOf("Exige SSD NVMe y GPU de alta gama"),
                score = 10
            ),
            ps4Performance = null, // No disponible en PS4
            ps5Performance = PlatformPerformance(
                platformName = "PlayStation 5",
                fpsTarget = "60 FPS (Rendimiento) / 30 FPS (Calidad)",
                resolution = "1440p / 4K reescalado",
                loadingTime = "Instantáneo (< 6 seg)",
                stabilityBadge = "Rendimiento Óptimo (9/10)",
                pros = listOf("60 FPS fluidos en Modo Rendimiento", "Inmersión háptica DualSense excepcional", "Audio 3D binaural"),
                cons = listOf("Ligera reducción de resolución en combates oscuros"),
                score = 9
            )
        ),
        "the_last_of_us_part_1" to PlatformComparison(
            recommendedPlatform = "PlayStation 5 (Versión Nativa Definitiva)",
            buyingRecommendation = "La mejor versión para comprarlo es sin duda PlayStation 5: fue diseñado específicamente para la arquitectura y el mando DualSense de esta consola, funcionando a 60 FPS sin ningún tipo de configuración. En PC tras los parches ya funciona bien, pero requiere hardware potente.",
            pcPerformance = PlatformPerformance(
                platformName = "PC",
                fpsTarget = "60 - 100 FPS",
                resolution = "Hasta 4K nativo con FSR / DLSS",
                loadingTime = "Rápido (< 6 seg)",
                stabilityBadge = "Bueno tras parches (9/10)",
                pros = listOf("Soporte ultrawide", "DLSS y FSR 3", "Fotogramas desbloqueados"),
                cons = listOf("Consumo alto de VRAM (requiere 8GB+)"),
                score = 9
            ),
            ps4Performance = PlatformPerformance(
                platformName = "PlayStation 4",
                fpsTarget = "60 FPS (Versión Remastered 2014)",
                resolution = "1080p",
                loadingTime = "Moderado (25 seg)",
                stabilityBadge = "Solo versión 2014 disponible (8/10)",
                pros = listOf("Precio muy económico"),
                cons = listOf("No incluye los gráficos hiperrealistas del Remake Part 1"),
                score = 8
            ),
            ps5Performance = PlatformPerformance(
                platformName = "PlayStation 5",
                fpsTarget = "60 FPS (Rendimiento) / 40 FPS (Modo 120Hz)",
                resolution = "1440p dinámico / 4K nativo",
                loadingTime = "Instantáneo (3 - 4 seg)",
                stabilityBadge = "Experiencia Maestra (10/10)",
                pros = listOf("Gatillos hápticos para tensar el arco", "Audio 3D Tempest", "60 FPS o 40 FPS de alta fidelidad"),
                cons = listOf("Precio de lanzamiento completo"),
                score = 10
            )
        ),
        "titanfall_2" to PlatformComparison(
            recommendedPlatform = "PC o PlayStation 5",
            buyingRecommendation = "En PC se encuentra frecuentemente de oferta por menos de $3 USD y permite jugar a 144-240 FPS con ratón. En PS5 corre a 60 FPS clavados y es una de las mejores campañas de disparos de la historia.",
            pcPerformance = PlatformPerformance(
                platformName = "PC",
                fpsTarget = "144 - 240+ FPS",
                resolution = "Hasta 4K nativo",
                loadingTime = "Instantáneo (< 3 seg)",
                stabilityBadge = "Sobresaliente (10/10)",
                pros = listOf("Ofertas habituales a $2.99 USD", "Precisión con ratón para wallrunning", "Tasa de refresco ultra alta"),
                cons = listOf("Comunidad competitiva veterana"),
                score = 10
            ),
            ps4Performance = PlatformPerformance(
                platformName = "PlayStation 4",
                fpsTarget = "60 FPS (Resolución dinámica)",
                resolution = "900p - 1080p",
                loadingTime = "Aceptable (20 seg)",
                stabilityBadge = "Muy Bueno (8/10)",
                pros = listOf("60 FPS en casi toda la campaña"),
                cons = listOf("Caídas leves en combates masivos de titanes"),
                score = 8
            ),
            ps5Performance = PlatformPerformance(
                platformName = "PlayStation 5",
                fpsTarget = "60 FPS fijos a 1080p/1440p",
                resolution = "1440p dinámico",
                loadingTime = "Ultrarrápido (< 6 seg)",
                stabilityBadge = "Rendimiento Óptimo (9/10)",
                pros = listOf("60 FPS completamente estables", "Campaña cinematográfica perfecta en TV"),
                cons = listOf("No tiene parche nativo de 4K a 120 FPS"),
                score = 9
            )
        ),
        "resident_evil_2_remake" to PlatformComparison(
            recommendedPlatform = "PlayStation 5 o PC",
            buyingRecommendation = "Tanto en PS5 como en PC la experiencia es de diez. En PS5 cuenta con trazado de rayos, modo 120 Hz para televisores compatibles y respuesta háptica en el DualSense. En PC ofrece opciones completas de mods y texturas ultra.",
            pcPerformance = PlatformPerformance(
                platformName = "PC",
                fpsTarget = "60 - 144+ FPS",
                resolution = "Hasta 4K nativo con Ray Tracing",
                loadingTime = "Instantáneo (< 4 seg)",
                stabilityBadge = "Excelente (10/10)",
                pros = listOf("Motor RE Engine sumamente optimizado", "Mods de cámara fija clásica y trajes", "Ray Tracing configurable"),
                cons = listOf("Ninguno reseñable"),
                score = 10
            ),
            ps4Performance = PlatformPerformance(
                platformName = "PlayStation 4",
                fpsTarget = "45 - 60 FPS (fluctuante)",
                resolution = "1080p nativo",
                loadingTime = "Moderado (25 - 35 seg)",
                stabilityBadge = "Bueno (8/10)",
                pros = listOf("Juego completo a precio rebajado"),
                cons = listOf("Sin Ray Tracing", "Cargas más largas"),
                score = 8
            ),
            ps5Performance = PlatformPerformance(
                platformName = "PlayStation 5",
                fpsTarget = "60 FPS (RT) / hasta 120 FPS",
                resolution = "4K dinámico",
                loadingTime = "Instantáneo (< 4 seg)",
                stabilityBadge = "Rendimiento Óptimo (10/10)",
                pros = listOf("Modo 120 Hz", "Ray Tracing en reflejos de charcos", "Audio 3D espacial para oír a Mr. X"),
                cons = listOf("Sin mods"),
                score = 10
            )
        ),
        "god_of_war" to PlatformComparison(
            recommendedPlatform = "PlayStation 5 o PC",
            buyingRecommendation = "Si tienes PS5, la actualización gratuita permite jugar a 60 FPS clavados a resolución 4K con tablero de ajedrez. En PC es fabuloso si quieres soporte ultrawide 21:9 o monitores de 120 Hz.",
            pcPerformance = PlatformPerformance(
                platformName = "PC",
                fpsTarget = "60 - 120 FPS",
                resolution = "Hasta 4K nativo con DLSS / FSR",
                loadingTime = "Ultrarrápido (< 5 seg)",
                stabilityBadge = "Impecable (10/10)",
                pros = listOf("Soporte ultrawide 21:9 para ver todo el plano", "NVIDIA Reflex para reflejos rápidos", "Fotogramas desbloqueados"),
                cons = listOf("Requiere PC de gama media-alta para 4K"),
                score = 10
            ),
            ps4Performance = PlatformPerformance(
                platformName = "PlayStation 4",
                fpsTarget = "30 FPS",
                resolution = "1080p nativo",
                loadingTime = "Lento (35 - 50 seg al cargar)",
                stabilityBadge = "Estable a 30 FPS (8/10)",
                pros = listOf("Experiencia original sólida"),
                cons = listOf("Bloqueado a 30 FPS", "Tiempos de carga notorios al morir"),
                score = 8
            ),
            ps5Performance = PlatformPerformance(
                platformName = "PlayStation 5",
                fpsTarget = "60 FPS fijos",
                resolution = "4K por tablero de ajedrez (2160p)",
                loadingTime = "Rápido (< 8 seg)",
                stabilityBadge = "Rendimiento Óptimo (10/10)",
                pros = listOf("60 FPS rocosos de principio a fin", "Sin ninguna caída de rendimiento", "Actualización gratis"),
                cons = listOf("Sin gatillos adaptativos nativos del DualSense"),
                score = 10
            )
        ),
        "doom_eternal" to PlatformComparison(
            recommendedPlatform = "PC (con ratón) o PlayStation 5 (a 120 FPS)",
            buyingRecommendation = "DOOM Eternal premia la velocidad extrema y el cambio veloz de armas: en PC con ratón y teclado es el shooter más satisfactorio jamás creado, corriendo a más de 144 FPS en cualquier PC moderna. En PS5 es impresionante porque ofrece Modo 120 Hz para televisores compatibles.",
            pcPerformance = PlatformPerformance(
                platformName = "PC",
                fpsTarget = "144 - 240+ FPS",
                resolution = "Hasta 4K con DLSS",
                loadingTime = "Instantáneo (< 3 seg)",
                stabilityBadge = "Perfección de Motor (10/10)",
                pros = listOf("id Tech 7 con optimización insuperable", "Apuntado quirúrgico con ratón", "DLSS con Ray Tracing"),
                cons = listOf("Ninguno"),
                score = 10
            ),
            ps4Performance = PlatformPerformance(
                platformName = "PlayStation 4",
                fpsTarget = "60 FPS",
                resolution = "1080p dinámico",
                loadingTime = "Moderado (25 seg)",
                stabilityBadge = "Muy Bueno (9/10)",
                pros = listOf("Increíblemente corre a 60 FPS en PS4 base"),
                cons = listOf("Tiempos de carga más largos al repetir salas"),
                score = 9
            ),
            ps5Performance = PlatformPerformance(
                platformName = "PlayStation 5",
                fpsTarget = "60 FPS (RT) o 120 FPS (Rendimiento)",
                resolution = "1800p dinámico / 4K",
                loadingTime = "Instantáneo (< 4 seg)",
                stabilityBadge = "Rendimiento Óptimo (10/10)",
                pros = listOf("Modo a 120 FPS ultrasuave", "Ray Tracing a 60 FPS", "Cargas inmediatas"),
                cons = listOf("Apuntar con mando exige práctica"),
                score = 10
            )
        ),
        "gta_v" to PlatformComparison(
            recommendedPlatform = "PlayStation 5 (Versión Oficial) o PC (para FiveM/Mods)",
            buyingRecommendation = "Para GTA Online oficial y campaña en consola, la versión de PlayStation 5 es infinitamente superior a la de PS4: carga en 15 segundos (en vez de 2 minutos y medio) y corre a 60 FPS con Ray Tracing. En PC cómpralo si deseas jugar servidores de Roleplay (FiveM) o mods.",
            pcPerformance = PlatformPerformance(
                platformName = "PC",
                fpsTarget = "60 - 120+ FPS",
                resolution = "Hasta 4K nativo",
                loadingTime = "Rápido (20 seg en SSD)",
                stabilityBadge = "Excelente con Mods (9/10)",
                pros = listOf("Servidores FiveM / Roleplay masivos", "Mods gráficos y vehículos reales", "Precios habituales de $14.99"),
                cons = listOf("GTA Online en PC tiene más presencia de trampas/hackers"),
                score = 9
            ),
            ps4Performance = PlatformPerformance(
                platformName = "PlayStation 4",
                fpsTarget = "24 - 30 FPS",
                resolution = "1080p nativo",
                loadingTime = "Muy Lento (2 a 3 minutos para entrar)",
                stabilityBadge = "Desfasado (6/10)",
                pros = listOf("Campaña de un jugador terminable"),
                cons = listOf("Cargas eternas", "Caídas de FPS en GTA Online", "No incluye mejoras gráficas"),
                score = 6
            ),
            ps5Performance = PlatformPerformance(
                platformName = "PlayStation 5",
                fpsTarget = "60 FPS (Rendimiento RT)",
                resolution = "1440p escalado con Ray Tracing",
                loadingTime = "Rápido (15 seg)",
                stabilityBadge = "Rendimiento Óptimo (9/10)",
                pros = listOf("60 FPS con reflejos Ray Tracing", "Cargas 8 veces más rápidas", "Gatillos hápticos en el acelerador de coches"),
                cons = listOf("Requiere comprar la versión de nueva generación"),
                score = 9
            )
        ),
        "spider_man_remastered" to PlatformComparison(
            recommendedPlatform = "PlayStation 5 o PC",
            buyingRecommendation = "En PS5 el balanceo por Nueva York con los gatillos adaptativos del DualSense es una de las experiencias más placenteras de la generación. En PC con una gráfica potente luce deslumbrante con Ray Tracing completo y soporte para monitores ultrawide.",
            pcPerformance = PlatformPerformance(
                platformName = "PC",
                fpsTarget = "60 - 120+ FPS",
                resolution = "Hasta 4K con DLSS / FSR",
                loadingTime = "Ultrarrápido (< 4 seg)",
                stabilityBadge = "Excelente (10/10)",
                pros = listOf("Trazado de rayos en cristales de rascacielos", "Soporte para monitores panorámicos 21:9 y 32:9", "Tasas altas de refresco"),
                cons = listOf("Exige buena CPU al balancearse a toda velocidad"),
                score = 10
            ),
            ps4Performance = PlatformPerformance(
                platformName = "PlayStation 4",
                fpsTarget = "30 FPS",
                resolution = "1080p nativo",
                loadingTime = "Moderado (25 - 35 seg)",
                stabilityBadge = "Bueno (8/10)",
                pros = listOf("Juego base muy disfrutable"),
                cons = listOf("Bloqueado a 30 FPS", "Sin trazado de rayos ni 60 FPS"),
                score = 8
            ),
            ps5Performance = PlatformPerformance(
                platformName = "PlayStation 5",
                fpsTarget = "60 FPS (Performance RT)",
                resolution = "1440p dinámico / 4K",
                loadingTime = "Instantáneo (2 - 3 seg)",
                stabilityBadge = "Experiencia Definitiva (10/10)",
                pros = listOf("Resistencia de la telaraña en los gatillos DualSense", "Viaje rápido instantáneo sin pantalla de carga", "60 FPS con Ray Tracing"),
                cons = listOf("Ninguno"),
                score = 10
            )
        )
    )
}
