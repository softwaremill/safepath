package com.softwaremill.safepath

import scala.annotation.compileTimeOnly
import com.softwaremill.safepath.SafePathCommonSupport._

trait SafePathCommonSupport extends SafePathEitherSupport with SafePathOptionSupport

object SafePathCommonSupport {
  private[safepath] def canOnlyBeUsedInsideIgnore(method: String) =
    s"$method can only be used inside macro"
}

trait SafePathEitherSupport {
  implicit class SafePathEither[T[_, _], L, R](e: T[L, R])(implicit f: SafePathEitherFunctor[T, L, R]) {
    @compileTimeOnly(canOnlyBeUsedInsideIgnore("eachLeft"))
    def eachLeft: L = sys.error("")

    @compileTimeOnly(canOnlyBeUsedInsideIgnore("eachRight"))
    def eachRight: R = sys.error("")
  }

  trait SafePathEitherFunctor[T[_, _], L, R] {
    def eachLeft(e: T[L, R])(f: L => L): T[L, R] = sys.error("")
    def eachRight(e: T[L, R])(f: R => R): T[L, R] = sys.error("")
  }

  implicit def eitherSafePathFunctor[T[_, _], L, R]: SafePathEitherFunctor[Either, L, R] =
    new SafePathEitherFunctor[Either, L, R] {}
}

trait SafePathOptionSupport {
  implicit class SafePathEach[F[_], T](t: F[T])(implicit f: SafePathFunctor[F, T]) {
    @compileTimeOnly(canOnlyBeUsedInsideIgnore("each"))
    def each: T = sys.error("")
  }

  trait SafePathFunctor[F[_], A] {
    @compileTimeOnly(canOnlyBeUsedInsideIgnore("each"))
    def each(fa: F[A])(f: A => A): F[A] = sys.error("")
  }

  implicit def optionSafePathFunctor[A]: SafePathFunctor[Option, A] =
    new SafePathFunctor[Option, A] {}
}
