pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "CapstoneDesign"
include(":app")
include(":core:domain")
include(":core:presentation:ui")
include(":core:presentation:designsystem")
include(":core:data")
include(":core:network")
include(":feature:home:data")
include(":feature:home:domain")
include(":feature:home:presentation")
include(":feature:search:data")
include(":feature:search:presentation")
include(":feature:search:domain")
include(":feature:detail:data")
include(":feature:detail:presentation")
include(":feature:detail:domain")
include(":feature:search:data")
include(":feature:search:presentation")
include(":feature:search:domain")
include(":feature:mypage:data")
include(":feature:camera:data")
include(":feature:camera:domain")
include(":feature:camera:presentation")
include(":feature:auth:data")
include(":feature:auth:domain")
include(":feature:auth:presentation")
include(":build-logic")
include(":feature:mypage:domain")
include(":feature:mypage:presentation")
