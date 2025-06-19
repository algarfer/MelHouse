package com.uniovi.melhouse.di.modules

import android.util.Log
import com.uniovi.melhouse.data.model.Bill
import com.uniovi.melhouse.data.model.BillUser
import com.uniovi.melhouse.data.model.Flat
import com.uniovi.melhouse.data.model.Task
import com.uniovi.melhouse.data.model.TaskUser
import com.uniovi.melhouse.data.model.User
import com.uniovi.melhouse.data.repository.bill.BillRepository
import com.uniovi.melhouse.data.repository.bill.BillRepositorySupabase
import com.uniovi.melhouse.data.repository.billuser.BillUserRepository
import com.uniovi.melhouse.data.repository.billuser.BillUserRepositorySupabase
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
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import java.time.LocalDate
import java.util.UUID
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [RepositoriesModule::class]
)
object TestRepositoriesModule {
    private val identifiedUserId = UUID.fromString("11111111-1111-1111-1111-111111111111")
    private val identifiedFlatId = UUID.fromString("22222222-2222-2222-2222-222222222222")
    private val users: MutableList<User> = mutableListOf()
    private val tasks: MutableList<Task> = mutableListOf()
    private val flats: MutableList<Flat> = mutableListOf()
    private val taskUsers: MutableList<TaskUser> = mutableListOf()
    private val bills: MutableStateFlow<List<Bill>> = MutableStateFlow(emptyList())
    private val billUsers: MutableStateFlow<List<BillUser>> = MutableStateFlow(emptyList())

    fun clearAll() {
        users.clear()
        tasks.clear()
        flats.clear()
        taskUsers.clear()
        val billId = UUID.randomUUID()
        bills.value = listOf(
            Bill(
                id = billId,
                amount = 100.0,
                flatId = identifiedFlatId,
                concept = "Test Bill",
            )
        )
        billUsers.value = listOf(
            BillUser(
                billId = billId,
                userId = identifiedUserId,
                amount = 100.0,
                paid = false
            )
        )

        users.add(
            User(
                id = identifiedUserId,
                email = "mel@mel.mel",
                name = "Mel",
                flatId = identifiedFlatId,
                fcmToken = ""
            )
        )
        flats.add(
            Flat(
                UUID.randomUUID(),
                "Flat1",
                "Flat1",
                1,
                "Flat1",
                "Flat1",
                "JAVIMONT",
                identifiedUserId
            )
        )
    }

    @Provides
    @Singleton
    fun provideUserRepository(): UserRepository {
        val mockk = mockk<UserRepository>()

        coEvery { mockk.insert(any()) } answers {
            val user = arg<User>(0)
            users.add(user)
            Unit
        }

        coEvery { mockk.findById(any()) } answers {
            val userId = arg<UUID>(0)
            users.find { user -> user.id == userId }
        }

        coEvery { mockk.findByIds(any()) } answers {
            val userIds = arg<List<UUID>>(0)
            users.filter { user -> userIds.contains(user.id) }
        }

        coEvery { mockk.findByEmail(any()) } answers {
            val email = arg<String>(0)
            users.find { user -> user.email == email }
        }

        coEvery { mockk.getRoommates(any()) } returns listOf(
            User(
                id = identifiedUserId,
                name = "Roommate1",
                email = "roommate1@email.com",
                flatId = null,
                fcmToken = ""
            ),
            User(
                name = "Roommate2",
                email = "roommate2@email.com",
                flatId = null,
                fcmToken = ""
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

        coEvery { mockk.findAllAsFlow() } answers {
            flow { mockk.findAll() }
        }

        coEvery { mockk.findByIdAsFlow(any()) } answers {
            flow { mockk.findById(arg(0)) }
        }

        coEvery { mockk.getRoommatesAsFlow(any()) } answers {
            flow { mockk.getRoommates(arg(0)) }
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

        coEvery { mockk.findByFlatId(any()) } answers {
            val flatId = arg<UUID>(0)
            tasks.filter { task -> task.flatId == flatId }
        }

        coEvery { mockk.findAllAsFlow() } answers {
            flow { mockk.findAll() }
        }

        coEvery { mockk.findByIdAsFlow(any()) } answers {
            flow { mockk.findById(arg(0)) }
        }

        coEvery { mockk.findByFlatIdAsFlow(any()) } answers {
            flow { mockk.findByFlatId(arg(0)) }
        }

        coEvery { mockk.findAssignedByDateAsFlow(any()) } answers {
            flow { mockk.findByDate(arg(0)) }
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
            flats.add(flat.copy(id = identifiedFlatId))
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

        coEvery { mockk.joinFlat(any()) } answers {
            val invitationCode = arg<String>(0)
            val flat = flats.find { flat -> flat.invitationCode == invitationCode }!!
            users.find { user -> user.id == identifiedUserId }?.let { user ->
                val updatedUser = user.copy(flatId = flat.id)
                users[users.indexOf(user)] = updatedUser
            }
            flat
        }

        coEvery { mockk.findAllAsFlow() } answers {
            flow { mockk.findAll() }
        }

        coEvery { mockk.findByIdAsFlow(any()) } answers {
            flow { mockk.findById(arg(0)) }
        }

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

        coEvery { mockk.findAllAsFlow() } answers {
            flow { mockk.findAll() }
        }

        coEvery { mockk.findByIdAsFlow(any()) } answers {
            flow { mockk.findById(arg(0)) }
        }

        return mockk
    }

    @Provides
    @Singleton
    fun provideBillRepository(): BillRepository {
        val mockk = mockk<BillRepository>()

        coEvery { mockk.findAllAsFlow() } answers {
            bills
        }

        coEvery { mockk.findAll() } answers {
            bills.value
        }

        coEvery { mockk.findAllByFlatIdAsFlow(any()) } answers {
            bills
        }

        coEvery { mockk.insert(any()) } answers {
            val entity = arg<Bill>(0)
            bills.value += entity
        }

        coEvery { mockk.update(any()) } answers {
            val entity = arg<Bill>(0)
            bills.value = bills.value.map { if (it.id == entity.id) entity else it }
        }

        coEvery { mockk.delete(any()) } answers {
            val entityId = arg<UUID>(0)
            bills.value = bills.value.filterNot { it.id == entityId }
        }

        coEvery { mockk.findById(any()) } answers {
            val entityId = arg<UUID>(0)
            bills.value.find { it.id == entityId }
        }

        coEvery { mockk.findByIdAsFlow(any()) } answers {
            val entityId = arg<UUID>(0)
            flowOf(bills.value.find { it.id == entityId }!!)
        }

        return mockk
    }

    @Provides
    @Singleton
    fun provideBillUserRepository(): BillUserRepository {
        val mockk = mockk<BillUserRepository>()

        coEvery { mockk.findAllAsFlow() } answers {
            billUsers
        }

        coEvery { mockk.findAll() } answers {
            billUsers.value
        }

        coEvery { mockk.insert(any()) } answers {
            val entity = arg<BillUser>(0)
            billUsers.value += entity
        }

        coEvery { mockk.update(any()) } answers {
            val entity = arg<BillUser>(0)
            billUsers.value = billUsers.value.map {
                if (it.billId == entity.billId && it.userId == entity.userId) entity else it
            }
        }

        coEvery { mockk.delete(any()) } answers {
            val entity = arg<BillUser>(0)
            billUsers.value = billUsers.value.filterNot {
                it.billId == entity.billId && it.userId == entity.userId
            }
        }

        return mockk
    }


}