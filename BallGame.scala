import akka.actor.{Actor, ActorRef, ActorSystem, Props}

import scala.util.Random

case class Ball(count: Int)

class BallPlayer(val id: Int, val allPlayers: Array[ActorRef]) extends Actor {
  override def receive = {
    case Ball(1000) => context.system.terminate()
    case Ball(count) => println(count + " thrown by player with id " + id); getRandomPlayer ! Ball(count + 1)
    case _ => println("Cannot parse msg");
      context.system.terminate()
  }

  private def getRandomPlayer: ActorRef = {
    val random = Random.nextInt(allPlayers.length)
    if (random == id) allPlayers((random + 1) % allPlayers.length)
    else allPlayers(random)
  }
}

object BallGame {
  def main(args: Array[String]): Unit = {
    val playersLimit = 3

    val players: Array[ActorRef] = new Array(playersLimit)
    val actorSystem: ActorSystem = ActorSystem("BallGame")

    for (i <- 0 until playersLimit) {
      val player = actorSystem.actorOf(Props(classOf[BallPlayer], i, players))
      players(i) = player
    }

    players(0) ! Ball(0)
  }
}