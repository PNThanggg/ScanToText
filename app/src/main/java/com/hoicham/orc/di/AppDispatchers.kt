package com.hoicham.orc.di

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class Dispatcher(val niaDispatcher: AppDispatchers)

enum class AppDispatchers {
    Default, IO,
}
