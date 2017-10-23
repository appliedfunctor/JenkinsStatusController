package com.itv.libs

import com.itv.{BuildFailure, BuildSuccess, BuildUnknown, Building}
import org.scalatest.{FunSpec, Matchers}

class JenkinsSpec extends FunSpec with Matchers {

  describe("getStatusFromResult") {

    it("should correctly match on SUCCESS") {
      Jenkins.getStatusFromResult("SUCCESS") shouldBe BuildSuccess
    }

    it("should correctly match on FAILURE") {
      Jenkins.getStatusFromResult("FAILURE") shouldBe BuildFailure
    }

    it("should correctly match on null") {
      Jenkins.getStatusFromResult("null") shouldBe Building
    }

    it("should correctly match on an unknown") {
      Jenkins.getStatusFromResult("gfs") shouldBe BuildUnknown
    }
  }

}
