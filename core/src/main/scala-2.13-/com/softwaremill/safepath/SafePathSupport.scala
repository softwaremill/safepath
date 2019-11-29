package com.softwaremill.safepath

import scala.annotation.compileTimeOnly
import scala.collection.TraversableLike
import scala.collection.generic.CanBuildFrom
import scala.language.experimental.macros
import scala.language.higherKinds
import com.softwaremill.safepath.SafePathCommonSupport._

trait SafePathSupport extends SafePathCommonSupport {
  implicit def traversableSafePathFunctor[F[_], A](
      implicit cbf: CanBuildFrom[F[A], A, F[A]],
      ev: F[A] => TraversableLike[A, F[A]]
  ): SafePathFunctor[F, A] =
    new SafePathFunctor[F, A] {}

  trait SafePathMapAtFunctor[F[_, _], K, T] {
    @compileTimeOnly(canOnlyBeUsedInsideIgnore("each"))
    def each(fa: F[K, T])(f: T => T): F[K, T] = sys.error("")
  }

  implicit def mapSafePathFunctor[M[KT, TT] <: Map[KT, TT], K, T](
      implicit cbf: CanBuildFrom[M[K, T], (K, T), M[K, T]]
  ): SafePathMapAtFunctor[M, K, T] = new SafePathMapAtFunctor[M, K, T] {}

  implicit class SafePathEachMap[F[_, _], K, T](t: F[K, T])(implicit f: SafePathMapAtFunctor[F, K, T]) {
    @compileTimeOnly(canOnlyBeUsedInsideIgnore("each"))
    def each: T = sys.error("")
  }
}
