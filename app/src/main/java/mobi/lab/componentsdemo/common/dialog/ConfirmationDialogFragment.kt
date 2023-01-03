package mobi.lab.componentsdemo.common.dialog

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.afollestad.materialdialogs.MaterialDialog
import mobi.lab.componentsdemo.common.eventbus.Bus

class ConfirmationDialogFragment : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val context = requireContext()
        val dialog = MaterialDialog(context)
        arguments?.let {
            it.getString(ARG_TITLE)?.let { value -> dialog.title(text = value) }
            it.getString(ARG_MESSAGE)?.let { value -> dialog.message(text = value) }
            it.getString(ARG_BUTTON_POS)?.let { value ->
                dialog.positiveButton(text = value) { sendEvent(DialogEvent.Action.BUTTON_POSITIVE) }
            }
            it.getString(ARG_BUTTON_NEG)?.let { value ->
                dialog.negativeButton(text = value) { sendEvent(DialogEvent.Action.BUTTON_NEGATIVE) }
            }
        }
        return dialog
    }

    override fun onCancel(dialog: DialogInterface) {
        super.onCancel(dialog)
        sendEvent(DialogEvent.Action.CANCELLED)
    }

    private fun sendEvent(action: DialogEvent.Action) {
        Bus.post(DialogEvent(getSafeTag(), action, getExtras()))
    }

    private fun getSafeTag(): String {
        return tag ?: ""
    }

    private fun getExtras(): Bundle? {
        return arguments?.getBundle(ARG_EXTRAS)
    }

    companion object {
        private const val ARG_TITLE = "ARG_TITLE"
        private const val ARG_MESSAGE = "ARG_MESSAGE"
        private const val ARG_BUTTON_POS = "ARG_BUTTON_POS"
        private const val ARG_BUTTON_NEG = "ARG_BUTTON_NEG"
        private const val ARG_EXTRAS = "ARG_EXTRAS"

        fun newInstance(
            title: String? = null,
            message: String,
            positiveButton: String,
            negativeButton: String? = null,
            extras: Bundle? = null
        ): ConfirmationDialogFragment {
            val args = Bundle()
            args.putString(ARG_TITLE, title)
            args.putString(ARG_MESSAGE, message)
            args.putString(ARG_BUTTON_POS, positiveButton)
            args.putString(ARG_BUTTON_NEG, negativeButton)
            args.putBundle(ARG_EXTRAS, extras)
            val fragment = ConfirmationDialogFragment()
            fragment.arguments = args
            return fragment
        }
    }
}
