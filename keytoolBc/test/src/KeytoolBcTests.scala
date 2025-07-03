package keytoolBc   

import utest.*


def getKeytoolOutput(args: String*): String = {
  import scala.sys.process._
  val cmd = Seq("keytool") ++ args
  cmd.!!
}

def removeFile(filename: String): Unit = {
  import java.io.File
  val file = new File(filename)
  if (file.exists()) {
    if (!file.delete()) {
      throw new RuntimeException(s"Failed to delete file: $filename")
    }
  }
}


object KeytoolBcTests extends TestSuite {
  val tests = Tests {
    test("Creates and Saves an Empty Keystore SUN") {
        val filename = "keystore_sun.test"
        val password = "password"
        Keystore.saveEmptyKeystore(filename, password, "PKCS12", "SUN")
        val output = getKeytoolOutput("-list", "-keystore", filename, "-storepass", password)
        removeFile(filename)
        assert(output.contains("Your keystore contains 0 entries"))
    }
    test("Creates and Saves an Empty Keystore BC") {
        val filename = "keystore_bc.test"
        val password = "password"
        Keystore.saveEmptyKeystore(filename, password, "PKCS12", "BC")
        val output = getKeytoolOutput("-list", "-keystore", filename, "-storepass", password)
        removeFile(filename)
        assert(output.contains("Your keystore contains 0 entries"))
    }
    test("Creates a keystore and adds a key pair SUN") {
        val filename = "keystore_sun_with_key.test"
        val password = "password"
        val keyPair = RSAKeyGen.generateKeyPair(2048)
        Keystore.saveEmptyKeystore(filename, password, "PKCS12", "SUN")
        val ks = Keystore.loadKeystore(filename, password, "PKCS12", "SUN")
        Keystore.addKeyPair(ks, "mykey", keyPair, password)
        Keystore.saveKeystore(ks, filename, password)
        val output = getKeytoolOutput("-list", "-keystore", filename, "-storepass", password)
        removeFile(filename)
        assert(output.contains("Your keystore contains 1 entry") && output.contains("mykey"))
    }
    test("Creates a keystore and adds a key pair BC") {
        val filename = "keystore_bc_with_key.test"
        val password = "password"
        val keyPair = RSAKeyGen.generateKeyPair(2048)
        Keystore.saveEmptyKeystore(filename, password, "PKCS12", "BC")
        val ks = Keystore.loadKeystore(filename, password, "PKCS12", "BC")
        Keystore.addKeyPair(ks, "mykey", keyPair, password)
        Keystore.saveKeystore(ks, filename, password)
        val output = getKeytoolOutput("-list", "-keystore", filename, "-storepass", password)
        removeFile(filename)
        assert(output.contains("Your keystore contains 1 entry") && output.contains("mykey"))
    }
  }
}