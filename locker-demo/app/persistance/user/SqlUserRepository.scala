package persistance.user

import javax.inject.Inject
import model.User
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile
import slick.jdbc.PostgresProfile.api._

import scala.concurrent.{ExecutionContext, Future}

private class UserTable(tag: Tag) extends Table[User](tag, "users") {

  def userId = column[Option[Long]]("user_id", O.PrimaryKey, O.AutoInc)

  def username = column[String]("username", O.Unique)

  def password = column[String]("password", O.Unique)

  def name = column[String]("name")

  def email = column[String]("email", O.Unique)

  def phoneNumber = column[Long]("phone_number", O.Unique)

  def * = (userId, username, password, name, email, phoneNumber) <> ((User.apply _).tupled, User.unapply)
}

class SqlUserRepository @Inject()(
                                   protected val dbConfigProvider: DatabaseConfigProvider
                                 )
                                 (
                                   implicit ec: ExecutionContext
                                 )
  extends HasDatabaseConfigProvider[JdbcProfile]
    with UserRepository {

  import profile.api._

  private val users = TableQuery[UserTable]

  override def create(user: User): Future[User] = {
    db.run(users.returning(users.map(_.userId)) += user).map { id =>
      user.copy(userId = id)
    }
  }
}
