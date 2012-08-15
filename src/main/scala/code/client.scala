package code
import scala.concurrent._
import scala.util._
import java.util.concurrent.Executors
import java.sql.SQLException

package object code{
   implicit val execctx = ExecutionContext.fromExecutor(Executors.newCachedThreadPool()) 
}

object Client{
  import code._
  
  def main(args: Array[String]) {
  
    /*val f:Future[Int] = future{
      WaitService.echo(2)
    }
    
    
    f onComplete {
      case Success(i) => println("Got " + i)
      case Failure(error) => println("got error: " + error.getMessage)
    }
    
    f onSuccess {
      case i => println("Success: " + 1)
    }
    f onFailure {
      case err => println("got error: " + err.getMessage())
    }
    
    val fut:Future[Double] = future{
      2 / 0
    }
    
    fut onFailure {
      case npe:NullPointerException => 
        println("Never going to get here")    
    }
    */
    val priceService = new PriceService
    val productId1 = 1
    val productId2 = 1
    
    /*val priceCheck = future{
      priceService.checkPrice(productId)
    }
    
    priceCheck onSuccess {
      case price =>
        val purchase = future{
          if (price < 100) priceService.purchaseItem(productId)
          else throw new Exception("to rich for my blood")
        }
        
        purchase onSuccess{
          case bool => 
            println("Purchase was a success")
        }
    }
    */
    /*val priceCheck = future{
      priceService.checkPrice(productId)
    }
    
    val purchase = priceCheck map {
      price =>
        if (price < 100) priceService.purchaseItem(productId)
        else throw new Exception("to rich for my blood")
    }    

    purchase onSuccess{
      case bool => 
        println("Purchase was a success")
    } */
    
    val prod1 = future{priceService.checkPrice(productId1)}
    val prod2 = future{priceService.checkPrice(productId2)}
    
    val purchase = for{
      price1 <- prod1
      price2 <- prod2
      if (price1 + price2 < 200)
    } yield priceService.purchaseBoth(productId1, productId2)
    
    purchase onSuccess{
      case bool => 
        println("Purchase was a success")
    }    
  }
}