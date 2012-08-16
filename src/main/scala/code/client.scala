package code
import scala.concurrent._
import scala.util._
import java.util.concurrent.Executors
import java.sql.SQLException
import util.duration._

package object code{
   implicit val execctx = ExecutionContext.fromExecutor(Executors.newCachedThreadPool()) 
}

object Client{
  import code._
  val priceService = new PriceService
  
  def main(args: Array[String]) {
    basic
    nested
    map
    await
    forcomp
    recover
    fallback
    trysuccess
  }
  
  def basic() = {
    val f:Future[Int] = future{
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
  }
  
  def nested = {
    val productId1 = 1
    val productId2 = 1
    
    val priceCheck = future{
      priceService.checkPrice(productId1)
    }
    
    priceCheck onSuccess {
      case price =>
        val purchase = future{
          if (price < 100) priceService.purchaseItem(productId1)
          else throw new Exception("to rich for my blood")
        }
        
        purchase onSuccess{
          case bool => 
            println("Purchase was a success")
        }
    }    
  }
  
  def map = {
    val priceCheck = future{
      priceService.checkPrice(123)
    }
    
    val purchase = priceCheck map {
      price =>
        if (price < 100) priceService.purchaseItem(123)
        else throw new Exception("to rich for my blood")
    }   
    
    purchase onSuccess{
      case bool => 
        println("Purchase was a success")
    }     
  }
  
  def await = {
    val purchase = future {
      priceService.checkPrice(123)
    }
    val result = Await.result(purchase, 1 second)
  }
  
  def forcomp = {
    val prod1 = future{priceService.checkPrice(123)}
    val prod2 = future{priceService.checkPrice(456)}
    
    val purchase = for{
      price1 <- prod1
      price2 <- prod2
      if (price1 + price2 < 200)
    } yield priceService.purchaseBoth(123, 456)
    
    purchase onSuccess{
      case bool => 
        println("Purchase was a success")
    }     
  }
  
  def recover = {
    val prod = future{priceService.checkPrice(123)}
    val purchase = prod map{
      price => priceService.purchaseItem(999)
    } recover {
      case ne:NotAvailableException => false
    }  
    
    purchase onSuccess {
      case b => println("Got value: " + b)
    }
  }
  
  def fallback = {
    val prod1Res = future{
      priceService.checkPrice(123)
    } map{
      price => "Product 1 price: " + price
    }
    
    val prod2Res = future{
      priceService.checkPrice(456)
    } map{
      price => "Product 2 price: " + price
    }   
    
    val result = prod1Res fallbackTo prod2Res
    result onSuccess {case str => println(str)}    
  }
  
  def trysuccess = {
    val p = Promise[Int]
    val pf = p.future
    val fut1 = future{
      val i = someLongRunningIntOp()
      p trySuccess i
    }
    val fut2 = future{
      val i = someOtherLongRunningIntOp()
      p trySuccess i
    }
    
    pf onSuccess { case i => println("Got value: " + i)}    
  }
  
  def someLongRunningIntOp() = {1}
  def someOtherLongRunningIntOp() = {2}
}