package bigdataml

object LinearRegressionRunner extends App {
  val cols_num=Seq("likes", "Checkins", "Returns", "Category", "comm24", "comm48", "comm24_1", "diff2448", "baseTime", "length", "shares", "hrs")
  
  val dp= new DataProvider()
  val data=dp.scale.tupled(dp.loadData("data/fbcomments.csv"))(cols_num)
  val (train, test) = dp.trainTestSplit(.7, dp.addOnes(data))
  val (train_x, train_y) = dp.xy(train)
  val (test_x, test_y) = dp.xy(test)
  
  val regressor = new LinearRegression()
  val yhat=regressor.pred(test_x)(regressor.fit(train_x, train_y))
  val mae=regressor.mae(test_y,yhat)
  val mse=regressor.mse(test_y,yhat)
  val rmse=regressor.rmse(test_y,yhat)
  
  println(f"MAE: $mae%1.2f; MSE: $mse%1.2f; RMSE: $rmse%1.2f; ")
  
}
