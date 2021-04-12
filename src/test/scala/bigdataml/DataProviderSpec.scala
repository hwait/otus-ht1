package bigdataml

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import org.scalactic.TolerantNumerics

class DataProviderSpec extends AnyFlatSpec with Matchers {
  val epsilon = 1e-2f

  implicit val doubleEq = TolerantNumerics.tolerantDoubleEquality(epsilon)


  val dp= new DataProvider()
  val (rows, header) = dp.loadData("data/fbcomments.csv")

  "header" should "have 27 elements" in {
    assert(header.size === 27)
  }
  it should "not contain commBase" in {
    assert(!header.contains("commBase"))
  }
  
  val cols_num=Seq("likes", "Checkins", "Returns", "Category", "comm24", "comm48", "comm24_1", "diff2448", "baseTime", "length", "shares", "hrs")
  val data=dp.scale(rows, header)(cols_num)

  "data" should "be zero on (14)(\"thu_pub\")" in {
    assert(data(14,header.get("thu_pub").get) === 0D)
  }
  it should "be zero on (21)(\"mon_pub\")" in {
    assert(data(21,header.get("mon_pub").get) === 0D)
  }
  it should "be zero on (278)(\"mon_base\")" in {
    assert(data(278,header.get("mon_base").get) === 0D)
  }
  it should "have 38360 elements" in {
    assert(data.rows === 38360)
  }
  it should "be 0.0013038915167285063 at (0,0)" in {
    assert(data(0,0) === 0.0013038915167285063)
  }
  it should "with leading 1.0 after addOnes()" in {
    assert(dp.addOnes(data)(0,0) === 1.0)
  }
  it should "splitted correctly" in {
    val (tr,te) = dp.trainTestSplit(.7,data)
    assert(tr.size.toDouble/(tr.size+te.size) === 0.7)
  }
}
