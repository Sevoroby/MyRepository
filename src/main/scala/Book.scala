
case class Book(id: Int,name: String,author: String)  {
  override def toString:String = id+". " + "\""+ name + "\"" + ", " + author
}
/*
curl -H "Content-Type: application/json" -X PUT  -d "{\"id\":10,\"name\":\"Меч без имени\",\"author\":\"Андрей Солодотов\"}" http://localhost:8080/1
curl -X DELETE http://localhost:8080/1
curl -X GET http://localhost:8080
curl -H "Content-Type: application/json" -X POST -d "{\"id\":5,\"name\":\"Книга\",\"author\":\"Федор\"}" http://localhost:8080
 */
