buildCache {
    remote<HttpBuildCache> {
        url = uri(providers.gradleProperty("GRADLE_REMOTE_CACHE_URL"))
        isAllowInsecureProtocol = true
        isAllowUntrustedServer = true
        val pushStr = providers.gradleProperty("GRADLE_REMOTE_CACHE_PUSH").orNull ?: "false"
        isPush = pushStr.toBoolean()
        credentials {
            username = providers.gradleProperty("GRADLE_REMOTE_CACHE_USERNAME").orNull
            password = providers.gradleProperty("GRADLE_REMOTE_CACHE_PASSWORD").orNull
        }
    }
}

buildCache {
    remote<HttpBuildCache> {

        fun getPropertyOrEnv(gradleKey: String, envKey: String): String? {
            return providers.gradleProperty(gradleKey).orNull
                ?: System.getenv(envKey)
        }
        val cacheUrl = getPropertyOrEnv("GRADLE_REMOTE_CACHE_URL", "GRADLE_REMOTE_CACHE_URL")
            ?: throw GradleException(
                "Remote build cache URL is not configured. " +
                        "Please set 'GRADLE_REMOTE_CACHE_URL' in gradle.properties or as an environment variable."
            )

        url = uri(cacheUrl)
        isAllowInsecureProtocol = true
        isAllowUntrustedServer = true
        val pushStr = getPropertyOrEnv("GRADLE_REMOTE_CACHE_PUSH", "GRADLE_REMOTE_CACHE_PUSH") ?: "false"
        isPush = pushStr.toBoolean()

        credentials {
            username = getPropertyOrEnv("GRADLE_REMOTE_CACHE_USERNAME", "GRADLE_REMOTE_CACHE_USERNAME")
            password = getPropertyOrEnv("GRADLE_REMOTE_CACHE_PASSWORD", "GRADLE_REMOTE_CACHE_PASSWORD")
        }
    }
}

rootProject.name = "DevOps"
