package repositories

import cats.free.Free
import cats.~>
import models.Ids.ShelfId
import models.Shelf

import scala.language.higherKinds

sealed trait ShelfRepo[A]

object ShelfRepo {
  case object All extends ShelfRepo[Seq[Shelf]]
  case class FindById(id: ShelfId) extends ShelfRepo[Option[Shelf]]

  type ShelfRepoF[A] = Free[ShelfRepo, A]

  def all: ShelfRepoF[Seq[Shelf]] = Free.liftF(All)
  def findById(id: ShelfId): ShelfRepoF[Option[Shelf]] = Free.liftF(FindById(id))

  trait Interpreter[M[_]] {
    def get: ShelfRepo ~> M
  }
}