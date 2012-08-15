package code

object WaitService{
  def echo(i:Int):Int = {
    Thread.sleep(i * 1000)
    i
  }
}

class PriceService{
  def checkPrice(productId:Long):Double = {
    90
  }
  
  def purchaseItem(productId:Long):Boolean = {
    true
  }
  
  def purchaseBoth(product1:Long, product2:Long):Boolean ={
    true
  }
}