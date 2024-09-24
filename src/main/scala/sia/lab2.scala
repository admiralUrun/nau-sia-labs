package sia

import sia.lab1.generatePassword

import java.security.MessageDigest
import java.util.Date
import scala.util.Random

object lab2 extends App {

  def generateSalt(length: Int = 16): String = {
    Random.alphanumeric.take(length).mkString + new Date().getTime.toString
  }

  def hashPassword(password: String, salt: String): (String, String) = {
    val digest = MessageDigest.getInstance("SHA-256")
    val saltedPassword = password + salt
    val hash = digest.digest(saltedPassword.getBytes("UTF-8"))
    (salt, hash.map("%02x".format(_)).mkString)
  }

  val accessMatrix = Map(
    "User1" -> Map("File1" -> "r,w", "File2" -> "r", "File3" -> "-", "File4" -> "r,w", "File5" -> "-", "File6" -> "-"),
    "User2" -> Map("File1" -> "-", "File2" -> "-", "File3" -> "r,w,x", "File4" -> "-", "File5" -> "-", "File6" -> "-"),
    "User3" -> Map("File2" -> "r", "File2" -> "r", "File3" -> "-", "File4" -> "r,w", "File5" -> "r", "File6" -> "-")
  )

  val users: Map[String, (String, String)] = Map(
    "User1" -> hashPassword("qwerty", generateSalt()),
    "User2" -> hashPassword(generatePassword(16), generateSalt()),
    "User3" -> hashPassword(generatePassword(16), generateSalt())
  )

  def authenticate(username: String, password: String): Boolean = {
    users
      .get(username)
      .exists { case (salt, hashedPassword) => hashedPassword == hashPassword(password, salt)._2
      }
  }

  def checkAccess(username: String, file: String, action: String): Boolean = {
    accessMatrix
      .get(username)
      .flatMap(_.get(file))
      .exists(_.contains(action))
  }


  while(true) {
    print("Enter username: ")
    val username = scala.io.StdIn.readLine()

    print("Enter password: ")
    val password = scala.io.StdIn.readLine()

    if (authenticate(username, password)) {
      println(s"Welcome, $username!")
      print(s"Enter file name ${accessMatrix.getOrElse(username, Map.empty).keys.mkString(", ")}: ")
      val file = scala.io.StdIn.readLine()

      print("Enter action (r - read, w - write, x - execute): ")
      val action = scala.io.StdIn.readLine()

      if (checkAccess(username, file, action)) {
        println(s"Access granted to $file for action '$action'.")
      } else {
        println(s"Access denied to $file for action '$action'.")
      }
    } else {
      println("Invalid username or password.")
    }
  }

}
