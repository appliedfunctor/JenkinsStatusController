package com.itv.libs

import java.security.cert
import javax.net.ssl._
import javax.security.cert.X509Certificate

import com.itv._
import io.circe._
import io.circe.parser._

import scala.io.Source

// Bypasses both client and server validation.
object TrustAll extends X509TrustManager {
  val getAcceptedIssuers = null

  def checkClientTrusted(x509Certificates: Array[X509Certificate], s: String) = {}

  def checkServerTrusted(x509Certificates: Array[X509Certificate], s: String) = {}

  override def checkServerTrusted(x509Certificates: Array[cert.X509Certificate], s: String) = {}

  override def checkClientTrusted(x509Certificates: Array[cert.X509Certificate], s: String) = {}
}

// Verifies all host names by simply returning true.
object VerifiesAllHostNames extends HostnameVerifier {
  def verify(s: String, sslSession: SSLSession) = true
}


object Jenkins {
  def checkStatus(): BuildStatus = {
    val url = "https://cdm-jenkins.mgm.grw.itv/cdm/job/contentdelivery-e2e/lastBuild/api/json"
    //result key

    val sslContext = SSLContext.getInstance("SSL")
    sslContext.init(null, Array(TrustAll), new java.security.SecureRandom())
    HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory)
    HttpsURLConnection.setDefaultHostnameVerifier(VerifiesAllHostNames)

    val statusJson = Source.fromURL(url).mkString
    val parseResult: Either[ParsingFailure, Json] = parse(statusJson)

    parseResult match {
      case Right(json) => processJson(json)
      case _ => println("Build status unknown"); BuildUnknown
    }
  }

  def processJson(json: Json): BuildStatus = {

    println(json)

    json.findAllByKey("result").headOption match {
      case None => Building
      case Some(result) => println(result.toString); getStatusFromResult(result.toString)
    }
  }

  def getStatusFromResult(result: String): BuildStatus = result.toLowerCase.replace("\"", "") match {
    case str if str == "success" => println("Build status success"); BuildSuccess
    case str if str == "failure" => println("Build status failed"); BuildFailure
    case str if str == "null" => println("Build status in progress"); Building
    case _ => println("Build status unknown"); BuildUnknown
  }
}
