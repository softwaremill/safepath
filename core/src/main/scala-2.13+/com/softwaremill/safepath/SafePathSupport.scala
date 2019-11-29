package com.softwaremill.safepath

import com.softwaremill.safepath.SafePathCommonSupport._

import scala.annotation.compileTimeOnly

trait SafePathSupport extends SafePathCommonSupport {
  implicit def traversableSafePathFunctor[F[_], A](
    implicit fac: Factory[A, F[A]],
    ev: F[A] => Iterable[A]
  ): SafePathFunctor[F, A] =
    new SafePathFunctor[F, A] {}

  implicit class SafePathEachMap[F[_, _], K, T](t: F[K, T])(implicit fac: Factory[(K, T), F[K, T]]) {
    @compileTimeOnly(canOnlyBeUsedInsideIgnore("each"))
    def each: T = sys.error("")
  }
}
