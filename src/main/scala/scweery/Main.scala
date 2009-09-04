package scweery

import Connection._

object Main {
  def main(args: Array[String]) {

    val connectionDetails = args.firstOption match {
      case Some("stg") => ("jdbc:oracle:thin:@(DESCRIPTION=(ADDRESS=(PROTOCOL = TCP)(HOST=corrac-rac10g-drs-vip1.sensis.com.au)(PORT = 1521))(ADDRESS=(PROTOCOL=TCP)(HOST=corrac-rac10g-drs-vip2.sensis.com.au)(PORT=1521))(ADDRESS=(PROTOCOL=TCP)(HOST=corrac-rac10g-drs-vip3.sensis.com.au)(PORT=1521))(LOAD_BALANCE=yes)(CONNECT_DATA=(SERVER=DEDICATED)(SERVICE_NAME=CORS1SRV)))", "BIDSMART_IF_READ", "BIDSMART_IF_READ")
      case Some("prd") => ("jdbc:oracle:thin:@(DESCRIPTION=(ADDRESS=(PROTOCOL = TCP)(HOST=corrac-rac10g-prod-vip1.sensis.com.au)(PORT = 1521))(ADDRESS=(PROTOCOL=TCP)(HOST=corrac-rac10g-prod-vip2.sensis.com.au)(PORT=1521))(ADDRESS=(PROTOCOL=TCP)(HOST=corrac-rac10g-prod-vip3.sensis.com.au)(PORT=1521))(LOAD_BALANCE=yes)(CONNECT_DATA=(SERVER=DEDICATED)(SERVICE_NAME=CORP1SRV)))", "BIDSMART_IF", "BIDSMART_IF")
      case _ => throw new RuntimeException("First argument must be stg or prd")
    }

    val bidsmart = new Connection(connectionDetails._1, connectionDetails._2, connectionDetails._3)
    using(bidsmart) { connection =>
      val accountToProduct = connection.make[(String, String)]("select productid, pfpaccountid from sponsored_listing") { row =>
        (row.string("PFPACCOUNTID"), row.string("PRODUCTID"))
      }

      val accountToDistinctProductCount = accountToProduct.foldLeft(Map.empty[String, Set[String]]){ (acc, next) =>
        acc.update(next._1, acc.getOrElse(next._1, Set.empty[String]) + next._2)
      }

      val duplicates = accountToDistinctProductCount.filter(_._2.size > 1)
      val productsThatFail = duplicates.foldLeft(Set.empty[String]) { (acc, next) =>
        acc ++ next._2
      }
      if (productsThatFail.isEmpty) {
        println("-- no duplicates to correct")
      } else {
        val failSet = productsThatFail.mkString(", ")
        println("-- delete duplicate products")
        println("delete from rawmessage_response where orig_msg_id in (select rawmessage_id from status_history where productid in (" + failSet + "));")
        println("delete from rawmessage_response where resp_msg_id in (select rawmessage_id from status_history where productid in (" + failSet + "));")
        println("delete from STATUS_HISTORY where RAWMESSAGE_ID in (select rawmessage_id from status_history where productid in (" + failSet + "));")
        println("delete from STATUS_HISTORY where PRODUCTID in (" + failSet + ");")
        println("delete from APPEARANCE where PRODUCTID in (" + failSet + ");")
        println("delete from SPONSORED_LISTING where PRODUCTID in (" + failSet + ");")
      }
    }
  }
}