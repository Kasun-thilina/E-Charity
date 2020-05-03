package com.kasuncreations.echarity.data.di

import android.app.Application
import android.content.SharedPreferences
import com.kasuncreations.echarity.data.repository.ChatRepository
import com.kasuncreations.echarity.data.repository.PostsRepository
import com.kasuncreations.echarity.data.repository.UserRepository
import com.kasuncreations.echarity.presentation.auth.AuthViewModelFactory
import com.kasuncreations.echarity.presentation.chat.ChatViewModelFactory
import com.kasuncreations.echarity.presentation.post.PostViewModelFactory
import com.kasuncreations.echarity.utils.CONSTANTS
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

        //bind() from singleton { FirebaseFunctions() }
        bind() from singleton { PostFunctions() }
        bind() from singleton { ChatFunctions() }
        bind() from singleton { UserRepository(instance()) }
        bind() from singleton { PostsRepository(instance()) }
        bind() from singleton { ChatRepository(instance()) }
        bind() from provider { AuthViewModelFactory(instance(), this@FirebaseApplication) }
        bind() from provider {
            PostViewModelFactory(
                instance(),
                instance(),
                this@FirebaseApplication
            )
        }
        bind() from provider { ChatViewModelFactory(instance(), this@FirebaseApplication) }
        bind<FirebaseFunctions>() with singleton {
            val pref: SharedPreferences by this.kodein.instance(arg = CONSTANTS.PREF_NAME)
            FirebaseFunctions(pref)
        }
    }
}