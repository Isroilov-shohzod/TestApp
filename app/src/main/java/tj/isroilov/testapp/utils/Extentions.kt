package tj.isroilov.testapp.utils

import android.view.View
import com.google.android.material.snackbar.Snackbar


fun View.snackBar(message: String?) {
    if (message != null)
        Snackbar.make(this, message, Snackbar.LENGTH_LONG).also { snackBar ->
            snackBar.setAction("Ok") { snackBar.dismiss() }
        }.show()
}
