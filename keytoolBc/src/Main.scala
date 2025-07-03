package keytoolBc
import Keystore.*

object Main {

  def main(args: Array[String]): Unit = {
    println("Hello!!")
    val keyPair = RSAKeyGen.generateKeyPair(2048)
    println(s"Generated key pair: ${keyPair.getPublic}")
    saveEmptyKeystore("keystore_sun.jks", "password", "JKS", "SUN")
    saveEmptyKeystore("keystore_sun.p12", "password", "PKCS12", "SUN")
    saveEmptyKeystore("keystore_bc.p12", "password", "PKCS12", "BC")

    val ks1 = loadKeystore("keystore_sun.jks", "password", "JKS", "SUN")
    val ks2 = loadKeystore("keystore_sun.p12", "password", "PKCS12", "SUN")
    val ks3 = loadKeystore("keystore_bc.p12", "password", "PKCS12", "BC")

    addKeyPair(ks1, "mykey", keyPair, "password")
    addKeyPair(ks2, "mykey", keyPair, "password")
    addKeyPair(ks3, "mykey", keyPair, "password")

    println(s"Aliases in keystore_sun.jks: ${listAliases(ks1)}")
    println(s"Aliases in keystore_sun.p12: ${listAliases(ks2)}")
    println(s"Aliases in keystore_bc.p12: ${listAliases(ks3)}")

    saveKeystore(ks1, "keystore_sun.jks", "password")
    saveKeystore(ks2, "keystore_sun.p12", "password")
    saveKeystore(ks3, "keystore_bc.p12", "password")

  }
}
