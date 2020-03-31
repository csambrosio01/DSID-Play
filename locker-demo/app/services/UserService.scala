package services

import javax.inject.Inject
import model.User
import persistance.user.SqlUserRepository

import scala.concurrent.Future

class UserService @Inject()(
                             userRepository: SqlUserRepository
                           ) {

  def createUser(user: User): Future[User] = {
    userRepository.create(user)
  }
}
