package sia

import java.util.Date
import scala.util.Random


object lab1 extends App {

  def generatePassword(length: Int, withNumbers:Boolean = true, withSymbols: Boolean = true): String = {
    val chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"
    val numbers = "0123456789"
    val symbols = "!@#$%^&*()-_=+<>?"

    val sourceOfSymbols = Random.shuffle(chars + (if (withNumbers) numbers else "") + (if (withSymbols) symbols else ""))
    (0 until length)
      .map(_ => sourceOfSymbols(Random.nextInt(sourceOfSymbols.length)))
      .mkString("")
  }

  val start = new Date()
  val checkSize = 10000000
  if (Vector.fill(checkSize)(generatePassword(8)).distinct.size != checkSize) throw new Exception("Знайдено пароль що повторився!")
  val end = new Date()
  println(s"Виконання зайняло: ${(end.getTime - start.getTime) / 1000} секунд.")
  println(s"Було згенеровано ${checkSize} паролів, жодного повтору не виявлено")
}