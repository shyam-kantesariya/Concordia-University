import java.io.{ObjectInputStream, ObjectOutputStream}
import java.net.Socket

class TcpClient {
  val s = new Socket("ip-172-31-17-224.us-west-2.compute.internal", 2002)
  val os = s.getOutputStream
  val oos = new ObjectOutputStream(os)
  val to = new Test("object from client")
  oos.writeObject(to)

  val is = s.getInputStream
  val ios = new ObjectInputStream(is)
  ios.available()
  val ito = ios.readObject.asInstanceOf[Test]
  if (ito != null)
    ito.print
  ios.close()
  is.close()
  oos.close()
}