package services.impl

import cats.Id
import models.Ids.BookId
import models.{Book, Ids, Shelf}
import org.scalatest.{FunSpec, Matchers}
import repositories.BookRepoSym

class BookServiceRepoInterpreterSpec extends FunSpec with Matchers {

  val book1 = Book(Book.Id(1), "book1", Shelf.Id(1))
  val book2 = Book(Book.Id(2), "book1", Shelf.Id(1))
  val book3 = Book(Book.Id(3), "book1", Shelf.Id(2))

  trait MockBookRepo extends BookRepoSym[Id] {
    override def all: Seq[Book] = Seq(book1, book2, book3)
    override def findById(id: BookId): Option[Book] = Some(book1)
    override def create(name: String, shelfId: Shelf.Id): Unit = ???
    override def update(id: Ids.BookId, name: String, shelfId: Shelf.Id): Unit = ???
    override def delete(id: _root_.models.Book.Id): Unit = ???
  }

  describe("findAll") {
    it("全ての Book を取得できること") {
      val intr = BookServiceRepoInterpreter(new MockBookRepo {})
      intr.sym.findAll shouldBe Seq(book1, book2, book3)
    }
  }

  describe("findById") {
    it("指定した ID の Book が取得できること") {
      val intr = BookServiceRepoInterpreter(new MockBookRepo {})
      intr.sym.findById(book1.id) shouldBe Some(book1)
    }

    it("指定した ID が存在しなかった場合") {
      val mock = new MockBookRepo {
        override def findById(id: BookId): Option[Book] = None
      }
      val intr = BookServiceRepoInterpreter(mock)
      intr.sym.findById(book1.id) shouldBe None
    }
  }
}
