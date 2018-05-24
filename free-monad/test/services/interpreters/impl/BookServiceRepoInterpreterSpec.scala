package services.interpreters.impl

import cats.{Id, ~>}
import models.{Book, Shelf}
import org.scalatest.{FunSpec, Matchers}
import repositories.BookRepo
import repositories.interpreters.BookRepoInterpreter
import services.BookService

class BookServiceRepoInterpreterSpec extends FunSpec with Matchers {

  val book1 = Book(Book.Id(1), "book1", Shelf.Id(1))
  val book2 = Book(Book.Id(2), "book1", Shelf.Id(1))
  val book3 = Book(Book.Id(3), "book1", Shelf.Id(2))

  trait MockInterpreter extends BookRepoInterpreter[Id] {
    override def get: BookRepo ~> Id = new (BookRepo ~> Id) {
      override def apply[A](fa: BookRepo[A]): A =
        fa match {
          case BookRepo.All => all
          case BookRepo.FindById(_) => findById
          case BookRepo.Create(_, _) => ()
          case BookRepo.Update(_, _, _) => ()
          case BookRepo.Delete(_) => ()
        }
    }

    def all = Seq(book1, book2, book3)
    def findById: Option[Book] = Some(book1)
  }

  val iter = BookServiceRepoInterpreter.get

  describe("findAll") {
    it("全ての Book を取得できること") {
      val result = BookService.findAll().foldMap(iter).foldMap(new MockInterpreter {}.get)
      result shouldBe Seq(book1, book2, book3)
    }
  }

  describe("findById") {
    it("指定した ID の Book が取得できること") {
      val result = BookService.findById(book1.id).foldMap(iter).foldMap(new MockInterpreter {}.get)
      result shouldBe Some(book1)
    }

    it("指定した ID が存在しなかった場合") {
      val repoIter = new MockInterpreter {
        override def findById: Option[Book] = None
      }.get
      val result = BookService.findById(book1.id).foldMap(iter).foldMap(repoIter)
      result shouldBe None
    }
  }
}
