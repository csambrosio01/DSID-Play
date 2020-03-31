package persistance.user

import com.google.inject.ImplementedBy
import model.User

import scala.concurrent.Future

@ImplementedBy(classOf[SqlUserRepository])
trait UserRepository {

  def create(user: User): Future[User]

  def findUserByUsername(username: String): Future[Option[User]]
}
