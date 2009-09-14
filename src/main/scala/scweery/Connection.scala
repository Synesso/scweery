package scweery

import java.sql.{DriverManager, Connection => JavaDBConnection}

/** Contains the credentials necessary to create a JDBC connection */
class Connection(val url: String, val user: String, val pass: String) {
  var jdbc: Option[JavaDBConnection] = None

  /** Create the connection and set it as the current state */
  def connect: JavaDBConnection = {
    jdbc = Some(DriverManager.getConnection(url, user, pass))
    jdbc.get
  }

  /** Disconnect the current connection, if it exists */
  def disconnect: Unit = {
    jdbc.foreach(_.close)
    jdbc = None
  }
}