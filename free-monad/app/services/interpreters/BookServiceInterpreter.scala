package services.interpreters

import cats.~>
import services.BookService

import scala.language.higherKinds

trait BookServiceInterpreter[M[_]] {
  def get: BookService ~> M
}
