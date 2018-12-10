import akka.actor.{Actor, ActorRef, ActorSystem, Props}

case class Ping(count: Int)
case class Pong(count: Int)
case class Start(count: Int, player: ActorRef)

class PingPongPlayer extends Actor {
  override def receive: PartialFunction[Any, Unit] = {
    case Ping(0) => context.system.terminate()
    case Ping(count) => println("Ping")
      sender ! Pong(count - 1)
    case Pong(0) => context.system.terminate()
    case Pong(count) => println("Pong")
      sender ! Ping(count - 1)
    case Start(count, player) => if (count <= 0)
      throw new IllegalArgumentException()
    else player ! Ping(count)
  }
}

object PingPong {
  def main(args: Array[String]): Unit = {
    val system = ActorSystem("MyActorSystem")
    val player1 = system.actorOf(Props(classOf[PingPongPlayer]))
    val player2 = system.actorOf(Props(classOf[PingPongPlayer]))
    player1 ! Start(5, player2)
  }
}
