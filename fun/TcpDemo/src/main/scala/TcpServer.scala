import java.io.{ObjectInputStream, ObjectOutputStream}
import java.net.{ServerSocket, Socket}

class TcpServer {
  val port = 2002
  try {
    val ss = new ServerSocket(port)
    val s = ss.accept
    val is = s.getInputStream
    val ois = new ObjectInputStream(is)
    val to = ois.readObject.asInstanceOf[Test]
    if (to != null)
      to.print
    is.close
    s.close()
    ss.close()

    Thread.sleep(12345)
    val os = s.getOutputStream
    val oos = new ObjectOutputStream(os)
    val tos = new Test("object from Server")
    oos.writeObject(tos)

    os.close()
    oos.close()
  } catch {
    case e: Exception =>
      System.out.println(e)
  }
}