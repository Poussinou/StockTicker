package com.github.premnirmal.ticker.settings

import android.os.AsyncTask
import android.text.TextUtils
import com.github.premnirmal.ticker.AppPreferences
import com.github.premnirmal.ticker.components.Analytics
import com.github.premnirmal.ticker.components.ILogIt
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.util.ArrayList

/**
 * Created by premnirmal on 2/27/16.
 */
internal open class FileExportTask : AsyncTask<List<String>, Void, String>() {

  override fun doInBackground(vararg tickers: List<String>): String? {
    val file = AppPreferences.tickersFile
    val tickerList: List<String> = ArrayList(tickers[0])
    try {
      if (file.exists()) {
        file.delete()
        file.createNewFile()
      }
      val fileOutputStream = FileOutputStream(file)
      for (ticker: String in tickerList) {
        fileOutputStream.write(("$ticker, ").toByteArray())
      }
      fileOutputStream.flush()
      fileOutputStream.close()
    } catch (e: FileNotFoundException) {
      ILogIt.INSTANCE.logException(RuntimeException(e))
      return null
    } catch (e: IOException) {
      ILogIt.INSTANCE.logException(RuntimeException(e))
      return null
    }

    Analytics.INSTANCE.trackSettingsChange("EXPORT", TextUtils.join(",", tickerList))
    return file.path
  }
}