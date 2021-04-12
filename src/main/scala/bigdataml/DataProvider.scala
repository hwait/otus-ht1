package bigdataml

import breeze.linalg._
import scala.collection.mutable.ArrayBuffer

class DataProvider {
  // Load data and fillna/dropna
  def loadData(path: String): (DenseMatrix[Double], Map[String,Int]) = {
    val rows = ArrayBuffer[Array[Double]]()
    val dropCol="commBase"
    val fillna=Seq("thu_pub","mon_base","mon_pub")

    val bufferedSource = io.Source.fromFile(path)
    
    // Parse first line as a header into Map
    val head=bufferedSource.getLines.take(1).toList(0).split(",").map(_.trim)
    val header=(head zip head.indices).toMap
    val headerRes=for ((k,v) <- header if v!=header.get(dropCol).get) yield {
        if (v<header.get(dropCol).get) (k->v)
        else (k->(v-1))
      }

    // Load csv file line-by-line and parse each line 
    for (line <- bufferedSource.getLines.drop(1)) {
      parseLine(line) match {
        case Some(row) => rows += row
        case None =>    // Nothing
      } 
    }
    bufferedSource.close
    
    // Parse single line
    def parseLine(line: String): Option[Array[Double]] = {
      val row=line.split(",").map(_.trim)
      
      // Fillna
      for (idx<-fillna.map(header.get(_).get)) {
        row(idx)=if (row(idx)=="") "0" else row(idx)
      }
      row(header.get(dropCol).get)="#"                // prepare to remove
       
      if(row.contains("")) None                       // dropna
      else Some(row.filter(_!="#").map(_.toDouble) )
    }
    //val cols_num=Seq("likes", "Checkins", "Returns", "Category", "comm24", "comm48", "comm24_1", "diff2448", "baseTime", "length", "shares", "hrs")
    //(headerRes, scale(DenseMatrix(rows.toSeq:_*), headerRes)(cols_num))
    (DenseMatrix(rows.toSeq:_*),headerRes)
  }

  val scale: (DenseMatrix[Double], Map[String,Int]) => Seq[String] => DenseMatrix[Double] = (data,header) => columns =>
  {
    for (idx<-columns.map(header.get(_).get)) {
      val _min=min(data(::,idx))
      val _diff=max(data(::,idx))-_min
      data(::,idx):=data(::,idx).map(x => (x - _min) / _diff)
    }
    data
  }

  val addOnes: DenseMatrix[Double] => DenseMatrix[Double] = x => DenseMatrix.horzcat(new DenseMatrix(x.rows,1,DenseVector.ones[Double](x.rows).toArray), x)

  // threshold: .7 means 70% to train and 30% to test
  val trainTestSplit: (Double,  DenseMatrix[Double]) => (DenseMatrix[Double], DenseMatrix[Double]) = (threshold, data) => {
    val r=DenseVector.rand(data.rows)
    (data(r.findAll(_<=threshold),::).toDenseMatrix, data(r.findAll(_>threshold),::).toDenseMatrix)
  }

  val xy: DenseMatrix[Double] => (DenseMatrix[Double],DenseMatrix[Double]) = x => (x(::, 0 to -2), new DenseMatrix(x.rows,1,x(::, -1).toArray)) 

}