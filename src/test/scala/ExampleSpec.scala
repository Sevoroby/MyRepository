import Boot.bookList
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.testkit.ScalatestRouteTest
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.{FlatSpec, Matchers}
import spray.json.DefaultJsonProtocol


@RunWith(classOf[JUnitRunner])
class ExampleSpec extends FlatSpec with Matchers with ScalatestRouteTest with SprayJsonSupport with DefaultJsonProtocol {
  implicit val testAPIJsonFormat = jsonFormat3(Book)
  bookList += Book(1,"Горе от ума","Грибоедов А.С.")
  bookList += Book(2,"Война и мир","Толстой Л.Н.")
  bookList += Book(3,"Евгений Онегин","Пушкин А.С.")
  var str: String = ""
  val route =
    get {
      complete {
        str = ""
        bookList.toSeq.foreach(book => str += (book.toString + "\n"))
        str
      }
    } ~
      post {
        entity(as[Book]) { b =>
          complete {
            {
              println(b.toString)
              if (!(bookList.contains(b))) bookList += b
              str = ""
              bookList.toSeq.foreach(book => str += (book.toString + "\n"));
              println(str)
              str
            }
          }
        }
      } ~
      path(Segment) { id =>
        put {
          entity(as[Book]) { b =>
            complete {
              bookList.toSeq.foreach(book => if (book.id.toString == id.toString) {
                bookList -= book;
                bookList += b})
              str = ""
              bookList.toSeq.foreach(book => str += (book.toString + "\n"));
              str
            }
          }
        }
      } ~
      path(Segment) { id =>
        delete {
          complete {
            bookList.toSeq.foreach(b => if (b.id.toString == id.toString) {
              bookList -= b})
            str = ""
            bookList.toSeq.foreach(book => str += (book.toString + "<br>"));
            str

          }
        }
      }
    "Сервис" should "возвращать список книг по запросу GET" in {
      // tests:
      Get() ~> route ~> check {
        responseAs[String] shouldEqual {str = ""
        bookList.toSeq.foreach(book => str += (book.toString + "\n"))
        str}
      }
    }

  "Сервис" should "добавлять книгу по запросу POST" in {
      // tests:
      Post("",Book(5,"Книга","Федор")) ~> route ~> check {
        {
          responseAs[String] shouldEqual {
            bookList += Book(5,"Книга","Федор")
            str = ""
            bookList.toSeq.foreach(book => str += (book.toString + "\n"))
            str}
        }
      }
    }

  "Сервис" should "изменять информацию о книге по запросу PUT" in {
      // tests:
      Put("/2",Book(5,"Книга","Федор")) ~> route ~> check {
        responseAs[String] shouldEqual  {
          bookList += Book(5,"Книга","Федор")
          bookList -=  Book(2,"Война и мир","Толстой Л.Н.")
          str = ""
          bookList.toSeq.foreach(book => str += (book.toString + "\n"));
          str
        }
      }
    }

  "Сервис" should "удалять книгу по запросу DELETE" in {
      // tests:
      Delete("/3") ~> route ~> check {
        responseAs[String] shouldEqual {
          bookList -= Book(3,"Евгений Онегин","Пушкин А.С.")
          str = ""
          bookList.toSeq.foreach(book => str += (book.toString + "<br>"));
          str

        }
      }
    }
  }
