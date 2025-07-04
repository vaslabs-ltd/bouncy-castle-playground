// Manages keystore operations using Bouncy Castle
package keytoolBc

import java.security.KeyStore
import java.io.{FileInputStream, FileOutputStream, File}
import org.bouncycastle.jce.provider.BouncyCastleProvider
import java.security.Security
import keytoolBc.RSAKeyGen

object Keystore:

  Security.addProvider(new BouncyCastleProvider())

  def createKeystore(ksType: String, provider: String): KeyStore =
    val ks = KeyStore.getInstance(ksType, provider)
    ks.load(null, null) // initialize empty keystore
    ks

  def saveKeystore(ks: KeyStore, filePath: String, password: String): Unit =
    saveKeystore(ks, new File(filePath), password.toCharArray)

  def saveKeystore(ks: KeyStore, file: File, password: Array[Char]): Unit =
    val fos = new FileOutputStream(file)
    try ks.store(fos, password)
    finally fos.close()

  def saveEmptyKeystore(
      filePath: String,
      password: String,
      ksType: String,
      provider: String
  ): Unit =
    val ks = createKeystore(ksType, provider)
    saveKeystore(ks, filePath, password)

  def loadKeystore(filePath: String, password: String, ksType: String, provider: String): KeyStore =
    loadKeystore(new File(filePath), password.toCharArray, ksType, provider)

  def loadKeystore(file: File, password: Array[Char], ksType: String, provider: String): KeyStore =
    val ks = KeyStore.getInstance(ksType, provider)
    val fis = new FileInputStream(file)
    try ks.load(fis, password)
    finally fis.close()
    ks

  def addKeyPair(
      ks: KeyStore,
      alias: String,
      keyPair: java.security.KeyPair,
      password: String
  ): Unit =
    val cert = CertUtil.createSelfSignedCertificate("CN=Test", keyPair)
    ks.setKeyEntry(alias, keyPair.getPrivate, password.toCharArray, Array(cert))

  def listAliases(ks: KeyStore): List[String] =
    val aliases = ks.aliases()
    Iterator
      .continually(aliases)
      .takeWhile(_.hasMoreElements)
      .map(_.nextElement())
      .toList
