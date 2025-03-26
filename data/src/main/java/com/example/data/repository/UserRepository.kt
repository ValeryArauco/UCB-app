import com.example.data.persistence.DAOs.UserDao
import com.example.data.persistence.entities.UserEntity

// Ejemplo de uso
class UserRepository(private val userDao: UserDao) {
    suspend fun addUser(user: UserEntity) {
        userDao.insertUser(user)
    }

    suspend fun getUserById(id: Int): UserEntity? {
        return userDao.getUserById(id)
    }
}