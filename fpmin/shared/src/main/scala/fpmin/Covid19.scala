package fpmin

import fpmin.csv._

/**
 * The Covid19 service provides access to standardized data sets.
 */
trait Covid19 {
  import Covid19._

  /**
   * Loads COVID19 data from the specified day, month, and optionally, region
   * and year.
   */
  def unsafeLoad(day: Int, month: Int, region: Region = Region.Global, year: Int = 2020): Csv
}
object Covid19 {

  /**
   * A production implementation of the Covid19 service that depends on a Github service.
   */
  class Live(github: Github) extends Covid19 {
    def unsafeLoad(day: Int, month: Int, region: Region = Region.Global, year: Int = 2020): Csv =
      Csv.fromString(github.unsafeDownload(Slug, formFullPath(day, month, region, year)))

    private def formFullPath(day: Int, month: Int, region: Region, year: Int): String = {
      def pad(int: Int): String = (if (int < 10) "0" else "") + int.toString

      s"csse_covid_19_data/csse_covid_19_daily_reports${region.suffix}/${pad(month)}-${pad(day)}-${year}.csv"
    }
  }
  private val Slug: String = "CSSEGISandData/COVID-19"

  sealed trait Region {
    def suffix: String
  }
  object Region {
    case object US extends Region {
      def suffix: String = "_us"
    }
    case object Global extends Region {
      def suffix: String = ""
    }
  }
}
