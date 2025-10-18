/*buildCache {
    remote<HttpBuildCache> {
        fun getPropertyOrEnv(key: String): String? {
            return System.getenv(key) ?: providers.gradleProperty(key).orNull
        }

        val cacheUrl = getPropertyOrEnv("GRADLE_REMOTE_CACHE_URL")
            ?: throw GradleException("GRADLE_REMOTE_CACHE_URL must be set in gradle.properties or as an environment variable")
        val pushStr = getPropertyOrEnv("GRADLE_REMOTE_CACHE_PUSH")
            ?: throw GradleException("GRADLE_REMOTE_CACHE_PUSH must be set in gradle.properties or as an environment variable")
        val usernameStr = getPropertyOrEnv("GRADLE_REMOTE_CACHE_USERNAME")
            ?: throw GradleException("GRADLE_REMOTE_CACHE_USERNAME must be set in gradle.properties or as an environment variable")
        val pswdStr = getPropertyOrEnv("GRADLE_REMOTE_CACHE_PASSWORD")
            ?: throw GradleException("GRADLE_REMOTE_CACHE_PASSWORD must be set in gradle.properties or as an environment variable")

        url = uri(cacheUrl)
        isAllowInsecureProtocol = true
        isAllowUntrustedServer = true
        isPush = pushStr.toBoolean()

        credentials {
            username = usernameStr
            password = pswdStr
        }
    }
}*/

rootProject.name = "DevOps"
