import java.io.File

object TestResourcesUtils {

    private fun resourcesBasePath() = TestResourcesUtils::class.java.getResource("/MEDIAS").path

    fun resolveFilePath(fileName: String) = resourcesBasePath() + "/" + fileName

    fun resolveFile(fileName: String) = File(resourcesBasePath() + fileName)

    fun resolvePath(fileName: String) = resolveFile(fileName).toPath()
}
