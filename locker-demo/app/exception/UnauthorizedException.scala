package exception

case class UnauthorizedException(message: String) extends Exception(message)
