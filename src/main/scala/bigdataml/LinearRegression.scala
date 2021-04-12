package bigdataml

import breeze.linalg._
import breeze.linalg.operators.OpSub
import breeze.math._
import breeze.numerics._

class LinearRegression {
  def fit(x:DenseMatrix[Double], y:DenseMatrix[Double]): DenseMatrix[Double] = {
      //np.matmul(np.linalg.inv( np.matmul(X_train_0.T,X_train_0) ), np.matmul(X_train_0.T,y_train))
      inv( x.t * x) * (x.t * y)
  }

  val pred: DenseMatrix[Double] => DenseMatrix[Double] => DenseMatrix[Double] = x_test => theta => x_test * theta
  
  val mae: (DenseMatrix[Double],DenseMatrix[Double]) => Double = (y, yhat) => {
      val subt = OpSub(y(::,0), yhat(::,0))
      sum(abs(subt)) / y.rows
    }

  val mse: (DenseMatrix[Double],DenseMatrix[Double]) => Double = (y, yhat) => {
      val subt = OpSub(y(::,0), yhat(::,0))
      val powrd = pow(subt, 2)
      sum(powrd) / y.rows
    }

  val rmse: (DenseMatrix[Double],DenseMatrix[Double]) => Double = (y, yhat) => sqrt(mse(y, yhat))


}
