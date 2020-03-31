package services

import exception.WrongCredentialsException
import javax.inject.Inject
import model.{Login, User}
import persistance.user.UserRepository

import scala.concurrent.{ExecutionContext, Future}

class UserService @Inject()(
                             userRepository: UserRepository
                           )
                           (
                             implicit ec: ExecutionContext
                           ) {

  def createUser(user: User): Future[User] = {
    userRepository.create(user)
  }

  def login(login: Login): Future[User] = {
    userRepository
      .findUserByUsername(login.username)
      .map { user =>
        user.fold(throw WrongCredentialsException("Wrong credentials")) { user =>
          if (user.password == login.password) {
            user
          } else {
            throw WrongCredentialsException("Wrong credentials")
          }
        }
      }
  }
}
