package templateworld

object Utils {
  def intPair2Long(a:Int,b:Int)=
    a.toLong << 32 | b.toLong

  def long2IntPair(v:Long)=
    ((v >> 32).toInt,(v&0xFFFFFFFF).toInt)

}
