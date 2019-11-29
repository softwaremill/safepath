package com.softwaremill.safepath

import java.time.Instant
import org.scalatest.{FlatSpec, Matchers}

class SafePathTest extends FlatSpec with Matchers with SafePathSupport {
  it should "extract names from path" in {
    namesFromPath[Family](_.first.name) shouldBe List("first", "name")
  }

  it should "extract names from list" in {
    namesFromPath[Organization](_.people.each.name) shouldBe List("people", "name")
  }

  it should "extract name from object wrapped with either" in {
    namesFromPath[Either[Person, Person]](_.eachRight.name) shouldBe List("name")
    namesFromPath[Either[Person, Person]](_.eachLeft.name) shouldBe List("name")
  }

  it should "extract name from object wrapped with option" in {
    namesFromPath[Option[Person]](_.each.name) shouldBe List("name")
  }

  it should "extract names from map" in {
    namesFromPath[Map[String, Person]](_.each.name) shouldBe List("name")
  }

  def namesFromPath[T](path: T => Any): List[String] =
    macro SafePath.stringsFromPathMacro[T]
}

case class Person(name: String, age: Int, in: Instant)
case class Family(first: Person, second: Person)
case class Organization(people: List[Person])
