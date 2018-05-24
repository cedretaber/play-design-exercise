package controllers.entities

import play.api.libs.json.{Format, Json}

case class Book(id: Long, name: String, shelfId: Long)

object Book {
  def fromModel(model: models.Book): Book =
    Book(model.id.unId, model.name, model.shelfId.unId)

  implicit val format: Format[Book] = Json.format[Book]
}
