package persistance.user

import model.User

import scala.concurrent.Future

trait UserRepository {

  def create(user: User): Future[User]
}
