package com.itv.libs

object Port {

  def getPorts(): String = {
    import jssc.SerialPortList
    val portNames = SerialPortList.getPortNames.toList
    portNames.foreach(println(_))
    portNames.headOption match {
      case None => "COM1"
      case Some(port) => port
    }
  }
}
