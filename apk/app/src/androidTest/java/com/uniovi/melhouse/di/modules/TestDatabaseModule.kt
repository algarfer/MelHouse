package com.uniovi.melhouse.di.modules
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.auth.AuthConfig
import io.github.jan.supabase.auth.SessionManager
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email
import io.github.jan.supabase.auth.user.UserInfo
import io.github.jan.supabase.auth.user.UserSession
import javax.inject.Singleton
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [DatabaseModule::class]
)
object TestDatabaseModule {

    @Provides
    @Singleton
    fun provideSupabase() : SupabaseClient {
        val supabaseClient = mockk<SupabaseClient>()
        val auth = mockk<Auth>()
        val sessionM = mockk<SessionManager>()
        val userSession = mockk<UserSession>()
        val userInfo = mockk<UserInfo>()
        //val postgrestBuilder = mockk<PostgrestBuilder>()
        //val postgrestResult = PostgrestResult(body = null, headers = Headers.Empty)

        /*every { supabaseClient.pluginManager } returns mockk {
            every { installedPlugins[Auth.key] } returns auth
            every { getPlugin(Auth) } returns auth
        }

        every { supabaseClient.pluginManager.getPlugin(Auth) } returns auth

        every { supabaseClient.auth} returns auth*/

        every { supabaseClient.pluginManager } returns mockk {
            every { installedPlugins[Auth.key] } returns mockk<Auth> {
                every { config } returns mockk<AuthConfig> {
                    every { defaultRedirectUrl } returns "<http://www.redirect.com|www.redirect.com>"
                    every { alwaysAutoRefresh } returns false
                }

                coEvery { loadFromStorage(any())} returns false

                coEvery { signUpWith<Email.Config, UserInfo, Email>(any(), any(), any()) } returns null

                coEvery { signInWith<Email.Config, UserInfo, Email>(any(), any(), any()) } returns Unit

                every { sessionManager } returns sessionM

                every { currentSessionOrNull() } returns userSession

                every { currentUserOrNull() } returns userInfo
            }
        }

        /*coEvery { auth.loadFromStorage()} returns false

        coEvery { auth.signUpWith<Email.Config, UserInfo, Email>(any(), any(), any()) } returns null

        coEvery { auth.signInWith<Email.Config, UserInfo, Email>(any(), any(), any()) } returns Unit

        every { auth.sessionManager } returns sessionManager*/

        coEvery { sessionM.saveSession(any()) } returns Unit

        /*every { auth.currentSessionOrNull() } returns userSession

        every {auth.currentUserOrNull()} returns userInfo*/

        every {userInfo.id} returns "11111111-1111-1111-1111-111111111111"

        return supabaseClient
    }

}
