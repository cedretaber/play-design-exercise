package repositories.interpreters

import cats.~>
import repositories.BookRepo

import scala.language.higherKinds

trait BookRepoInterpreter[M[_]] {
  def get: BookRepo ~> M
}
