package repositories.impl

import org.scalatest.{Matchers, fixture}
import models.Ids._
import repositories.entities.{Book, Shelf}
import scalikejdbc.SQL
import scalikejdbc.config.DBs
import scalikejdbc.scalatest.AutoRollback

class BookRepoDBInterpreterSpec  extends fixture.FunSpec with Matchers with AutoRollback {

  DBs.setup('default)

  val shelf1 = Shelf(models.Shelf.Id(1), "shelf1")
  val shelf2 = Shelf(models.Shelf.Id(2), "shelf1")
  val book1 = Book(models.Book.Id(1), "Book1", models.Shelf.Id(1))
  val book2 = Book(models.Book.Id(2), "Book2", models.Shelf.Id(1))
  val book3 = Book(models.Book.Id(3), "Book3", models.Shelf.Id(2))

  override def fixture(implicit session: FixtureParam): Unit = {
    for {
      shelf <- Seq(shelf1, shelf2)
    } SQL("""INSERT INTO "shelves" VALUES (?, ?)""").bind(shelf.id.unId, shelf.name).update.apply

    for {
      book <- Seq(book1, book2, book3)
    } SQL("""INSERT INTO "books" VALUES (?, ?, ?)""").bind(book.id.unId, book.name, book.shelfId.unId).update.apply
  }

  val sym = BookRepoDBInterpreter.sym

  describe("all") {
    it("全ての Book を取得できること") { session =>
        sym.all.run(session) shouldBe Seq(book1, book2, book3).map(_.toModel)
    }
  }

  describe("findById") {
    it("正しい ID の Book を返すこと") { implicit session =>
      sym.findById(book1.id).run(session) shouldBe Some(book1.toModel)
    }

    it("存在しない ID を指定した場合、 None を返すこと") { implicit session =>
      sym.findById(42.asBookId).run(session) shouldBe None
    }
  }

}
