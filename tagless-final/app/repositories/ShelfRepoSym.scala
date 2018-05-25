package repositories

import models.Ids.ShelfId
import models.Shelf

import scala.language.higherKinds

trait ShelfRepoSym[R[_]] {
  def all: R[Seq[Shelf]]
  def findById(id: ShelfId): R[Option[Shelf]]
}