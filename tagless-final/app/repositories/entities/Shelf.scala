package repositories.entities

import scalikejdbc._

case class Shelf(id: models.Shelf.Id, name: String) {
  lazy val toModel: models.Shelf =
    models.Shelf(id, name, Seq.empty)
}

object Shelves extends SQLSyntaxSupport[Shelf] {

  def apply(rn: ResultName[Shelf])(wr: WrappedResultSet): Shelf =
    Shelf(models.Shelf.Id(wr.long(rn.id)), wr.string(rn.name))
}