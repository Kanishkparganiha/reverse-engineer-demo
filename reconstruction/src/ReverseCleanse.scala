// ReverseCleanse.scala
// Built ONLY from rea's recovered forward logic (transformRow / <clinit> decompiled from
// customer-cleanse.apk via JADX), NOT from the original CleanseJob.scala source.
//
// Recovered forward rules being inverted here:
//   Rule 3 (reversible): signup_date MM/DD/YYYY -> ISO YYYY-MM-DD
//   Rule 4 (reversible): region_code "1"->"NA", "2"->"EU", "3"->"APAC" (REGION_MAP, recovered
//                        from <clinit> bytecode); unmapped codes fall through to "UNKNOWN"
//   Rule 5 (reversible): output column order [customer_id, name, signup_date, region]
//   Rule 2 (lossy, but inferable): only ACTIVE rows survive to the output, so status is
//                        re-derived as "ACTIVE" for every surviving row
//   Rule 1 (lossy, NOT inferable): rows with null/empty customer_id are gone; nothing here
//                        recovers them
//   Rule 6 (lossy bytes, re-derived): status was dropped from the output columns but is
//                        re-derived via Rule 2 above
//
// region="UNKNOWN" is irreversible: REGION_MAP only defines codes 1/2/3, so codes 4 and 5 both
// collapse to UNKNOWN going forward. The reverse transform cannot tell which original code it
// was and honestly emits "?" rather than guessing.
import scala.io.Source
import java.io.PrintWriter

object ReverseCleanse {
  // Inverse of the recovered REGION_MAP (1->NA, 2->EU, 3->APAC)
  val REGION_INV: Map[String, String] = Map("NA" -> "1", "EU" -> "2", "APAC" -> "3")

  def main(args: Array[String]): Unit = {
    val inPath  = args(0) // output.csv (forward output, tab-separated)
    val outPath = args(1) // reconstructed.csv
    val lines   = Source.fromFile(inPath).getLines().toList
    val pw = new PrintWriter(outPath)
    pw.println("customer_id,status,signup_date,region_code,name")
    lines.drop(1).foreach { line =>
      val parts  = line.split("\t")
      val id     = parts(0)
      val name   = parts(1)
      val iso    = parts(2)
      val region = parts(3)
      val Array(y, m, d) = iso.split("-") // invert Rule 3: ISO -> MM/DD/YYYY
      val mdy    = s"$m/$d/$y"
      val code   = REGION_INV.getOrElse(region, "?") // invert Rule 4; UNKNOWN -> "?" (ambiguous)
      val status = "ACTIVE"                          // re-derived from Rule 2
      pw.println(s"$id,$status,$mdy,$code,$name")     // invert Rule 5 column order
    }
    pw.close()
    println(s"wrote $outPath")
  }
}
