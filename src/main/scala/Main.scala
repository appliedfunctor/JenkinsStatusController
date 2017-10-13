package com.itv

import java.util.concurrent.{ScheduledThreadPoolExecutor, TimeUnit}
import scala.concurrent.ExecutionContext.global

import com.itv.libs.Jenkins
import jssc.{SerialPort, SerialPortEvent, SerialPortEventListener, SerialPortException}

object Main extends App{

  var buildStatus = BuildUnknown
  val portName = libs.Port.getPorts
  println("Creating port " + portName)
  val serialPort: SerialPort = new SerialPort(portName)

  try {

    println("Opening port ...")
    serialPort.openPort

    serialPort.addEventListener(new SerialEventListener(), SerialPort.MASK_RXCHAR)

    //We expose the settings. You can also use this line - serialPort.setParams(9600, 8, 1, 0);
    println("Setting params ...")
    serialPort.setParams(SerialPort.BAUDRATE_9600, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE)

    //wait long enough for initialisation to complete and everything to be ready
    Thread.sleep(2000)

    //simulate
    run

    while(true){}

    //Closing the port
    println("Closing port")
    serialPort.closePort
  } catch {
    case ex: SerialPortException => println(ex)
  }

  def simulate = {
    while(true) {
      //buildFailed

      var x = 20
      while (x > 0) {
        buildProgress
        x = x - 1
      }
      Thread.sleep(12000)
    }
  }

//  def run = {
//    val ex = new ScheduledThreadPoolExecutor(1)
//    val f = ex.scheduleAtFixedRate(poll, 1, 1, TimeUnit.MINUTES)
//    f.cancel(false)
//  }

  def run = {
    while (true) {
      handleStatus(Jenkins.checkStatus)
    }
  }

//  def poll = new Runnable {
//    def run() = handleStatus(Jenkins.checkStatus)
//  }

  def handleStatus(buildStatus: BuildStatus) = buildStatus match {
    case BuildUnknown => var x = 60; while (x > 0) { buildProgress; x = x-1 }
    case BuildFailure => buildFailed; waitAMinute
    case BuildSuccess => buildPassed; waitAMinute
    case Building => var x = 60; while (x > 0) { buildProgress; x = x-1 }
  }

  def waitAMinute = {
    Thread.sleep(60000)
  }

  def buildFailed = {
    serialPort.writeString("21")
    serialPort.writeString("30")
  }

  def buildPassed = {
    serialPort.writeString("20")
    serialPort.writeString("31")
  }

  def buildProgress = {
      buildFailed
      Thread.sleep(250)
      buildPassed
      Thread.sleep(250)
  }

  class SerialEventListener extends SerialPortEventListener {
    override def serialEvent(serialPortEvent: SerialPortEvent) = {
      if (serialPortEvent.isRXCHAR && serialPortEvent.getEventValue > 0) try {
        val receivedData = serialPort.readString(serialPortEvent.getEventValue)
        print(receivedData)
      } catch {
        case ex: SerialPortException => println("Error in receiving string from serial port: " + ex)
      }
    }
  }

}

