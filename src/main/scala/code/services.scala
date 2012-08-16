package code
import java.util.Random

object WaitService{
  def echo(i:Int):Int = {
    Thread.sleep(i * 1000)
    i
  }
}

class PriceService{
  val r = new Random(System.currentTimeMillis)
  
  def checkPrice(productId:Long):Double = {
    Thread.sleep(r.nextInt(1000))
    if (productId == 123) 25.99
    else if (productId == 456) 35.99
    else 99.99
  }
  
  def purchaseItem(productId:Long):Boolean = {
    Thread.sleep(r.nextInt(1000))
    if (productId == 999) throw new NotAvailableException
    true
  }
  
  def purchaseBoth(product1:Long, product2:Long):Boolean ={
    Thread.sleep(r.nextInt(1000))
    true
  }
}

class NotAvailableException extends Exception