
object RunApp extends App {
  args(0) match {
    case "client" => {
      new TcpClient
    }
    case "server" => {
      new TcpServer
    }
  }
}