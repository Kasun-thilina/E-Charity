package com.kasuncreations.echarity.data.firebase

import android.app.Application
import com.kasuncreations.echarity.data.repository.PostsRepository
import com.kasuncreations.echarity.data.repository.UserRepository
import com.kasuncreations.echarity.presentation.auth.AuthViewModelFactory
import com.kasuncreations.echarity.presentation.post.PostViewModelFactory
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton

class FirebaseApplication : Application(), KodeinAware {

    override val kodein = Kodein.lazy {
        import(androidXModule(this@FirebaseApplication))

        bind() from singleton { FirebaseFunctions() }
        bind() from singleton { PostFunctions() }
        bind() from singleton { UserRepository(instance()) }
        bind() from singleton { PostsRepository(instance()) }
        bind() from provider { AuthViewModelFactory(instance(), this@FirebaseApplication) }
        bind() from provider {
            PostViewModelFactory(
                instance(),
                instance(),
                this@FirebaseApplication
            )
        }
    }
}