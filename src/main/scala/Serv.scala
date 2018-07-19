import akka.actor.ActorSystem
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.server.Directives
import akka.stream.ActorMaterializer
import spray.json.DefaultJsonProtocol
trait JsonSupport extends SprayJsonSupport with DefaultJsonProtocol {
  implicit val testAPIJsonFormat = jsonFormat3(Book)
}
 trait Serv extends Directives with JsonSupport {
   implicit val system = ActorSystem()
   implicit val materializer = ActorMaterializer()
   var bookList = Set[Book]()
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
 }
