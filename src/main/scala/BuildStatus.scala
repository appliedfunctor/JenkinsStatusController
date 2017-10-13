package com.itv

sealed trait BuildStatus

case object BuildSuccess extends BuildStatus
case object BuildFailure extends BuildStatus
case object Building extends BuildStatus
case object BuildUnknown extends BuildStatus