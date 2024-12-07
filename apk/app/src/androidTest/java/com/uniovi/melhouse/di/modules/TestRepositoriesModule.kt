package com.uniovi.melhouse.di.modules

import android.util.Log
import com.uniovi.melhouse.data.model.Flat
import com.uniovi.melhouse.data.model.Task
import com.uniovi.melhouse.data.model.TaskUser
import com.uniovi.melhouse.data.model.User
import com.uniovi.melhouse.data.repository.flat.FlatRepository
import com.uniovi.melhouse.data.repository.task.TaskRepository
import com.uniovi.melhouse.data.repository.taskuser.TaskUserRepository
import com.uniovi.melhouse.data.repository.user.UserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import io.mockk.coEvery
import io.mockk.mockk
import java.time.LocalDate
import java.util.UUID
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [RepositoriesModule::class]
)
object TestRepositoriesModule {
    private val users: MutableList<User> = mutableListOf()
    private val tasks: MutableList<Task> = mutableListOf()
    private val flats: MutableList<Flat> = mutableListOf()
    private val taskUsers: MutableList<TaskUser> = mutableListOf()

    @Provides
    @Singleton
    fun provideUserRepository(): UserRepository {
        val mockk = mockk<UserRepository>()

        coEvery { mockk.insert(any()) } answers {
            val user = arg<User>(0)
            Log.d("hola", "hola")
            //if(!user.email.startsWith("mel"))
            users.add(user)
            Unit
        }

        coEvery { mockk.findById(any()) } answers {
            val userId = arg<UUID>(0)
            Log.i("userId", userId.toString())
            val ret: User?
            if(userId.toString() == "11111111-1111-1111-1111-111111111111")
                ret = User(id = UUID.fromString("11111111-1111-1111-1111-111111111111"), email = "mel@mel.mel", name = "Mel", flatId = UUID.randomUUID())
            else
                ret = users.find { user -> user.id == userId }
            ret
        }

        coEvery { mockk.findByIds(any()) } answers {
            val userIds = arg<List<UUID>>(0)
            users.filter { user -> userIds.contains(user.id) }
        }

        coEvery { mockk.findByEmail(any()) } answers {
            val email = arg<String>(0)
            users.find { user -> user.email == email }
        }

        coEvery { mockk.getRoommates() } returns listOf(
            User(
                name = "Roommate1",
                email = "roommate1@email.com",
                flatId = null
            ),
            User(
                name = "Roommate2",
                email = "roommate2@email.com",
                flatId = null
            )
        )

        coEvery { mockk.findAsigneesById(any()) } answers {
            val taskId = arg<UUID>(0)
            val asigneesIds = taskUsers.filter { taskUser -> taskUser.taskId == taskId }
                .map { asignee -> asignee.userId }
            users.filter { user -> asigneesIds.contains(user.id) }
        }

        coEvery { mockk.findAll() } answers {
            users
        }

        coEvery { mockk.update(any()) } answers {
            val newUser = arg<User>(0)
            users.add(users.indexOfFirst { task -> task.id == newUser.id }, newUser)
        }

        coEvery { mockk.delete(any()) } answers {
            val userId = arg<UUID>(0)
            users.removeIf { user -> user.id == userId }
            Unit
        }

        return mockk
    }

    @Provides
    @Singleton
    fun provideTaskRepository(): TaskRepository {
        val mockk = mockk<TaskRepository>()

        coEvery { mockk.insert(any()) } answers {
            val task = arg<Task>(0)
            tasks.add(task)
            Unit
        }

        coEvery { mockk.findById(any()) } answers {
            val taskId = arg<UUID>(0)
            tasks.find { task -> task.id == taskId }
        }

        coEvery { mockk.findAll() } answers {
            tasks
        }

        coEvery { mockk.findByDate(any()) } answers {
            val date = arg<LocalDate>(0)
            tasks.filter { task -> date == task.endDate }
        }

        coEvery { mockk.update(any()) } answers {
            val newTask = arg<Task>(0)
            tasks.add(tasks.indexOfFirst { task -> task.id == newTask.id }, newTask)
        }

        coEvery { mockk.delete(any()) } answers {
            val taskId = arg<UUID>(0)
            tasks.removeIf { task -> task.id == taskId }
            Unit
        }

        return mockk
    }

    @Provides
    @Singleton
    fun provideFlatRepository(): FlatRepository {
        val mockk = mockk<FlatRepository>()

        coEvery { mockk.insert(any()) } answers {
            val flat = arg<Flat>(0)
            flats.add(flat)
            Unit
        }

        coEvery { mockk.createFlat(any()) } answers {
            val flat = arg<Flat>(0)
            flats.add(flat)
            flat
        }

        coEvery { mockk.findById(any()) } answers {
            val flatId = arg<UUID>(0)
            flats.find { flat -> flat.id == flatId }
        }

        coEvery { mockk.findAll() } answers {
            flats
        }

        coEvery { mockk.update(any()) } answers {
            val newFlat = arg<Flat>(0)
            flats.add(flats.indexOfFirst { flat -> flat.id == newFlat.id }, newFlat)
        }

        coEvery { mockk.delete(any()) } answers {
            val flatId = arg<UUID>(0)
            flats.removeIf { flat -> flat.id == flatId }
            Unit
        }

        //TODO
        //coEvery { mockk.joinFlat(any()) } answers {

        return mockk
    }

    @Provides
    @Singleton
    fun provideTaskUserRepository(): TaskUserRepository {
        val mockk = mockk<TaskUserRepository>()

        coEvery { mockk.insert(any()) } answers {
            val task = arg<TaskUser>(0)
            taskUsers.add(task)
            Unit
        }

        coEvery { mockk.findAll() } answers {
            taskUsers
        }

        //TODO A espera del cambio de UUID a entidad
        //coEvery { mockk.findById(any()) }
        //coEvery { mockk.update(any()) }
        //coEvery { mockk.delete(any()) }

        coEvery { mockk.insertAsignees(any(), any()) } answers {
            val taskId = arg<UUID>(0)
            val userIds = arg<List<UUID>>(1)

            userIds.forEach { userId -> taskUsers.add(TaskUser(userId = userId, taskId = taskId)) }
        }

        coEvery { mockk.deleteAllAsignees(any()) } answers {
            val taskId = arg<UUID>(0)

            taskUsers.removeIf { taskUser -> taskUser.taskId == taskId }
            Unit
        }

        return mockk
    }
}