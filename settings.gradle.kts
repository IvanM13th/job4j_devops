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

rootProject.name = "DevOps"
