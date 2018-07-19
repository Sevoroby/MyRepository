import akka.http.scaladsl.Http

object Boot extends App with Serv{
  val serverSource = Http().bindAndHandle(route, interface = "localhost", port = 8080)
  bookList += Book(1,"Горе от ума","Грибоедов А.С.")
  bookList += Book(2,"Война и мир","Толстой Л.Н.")
  bookList += Book(3,"Евгений Онегин","Пушкин А.С.")
}