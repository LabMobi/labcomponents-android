package mobi.lab.componentsdemo.login

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import mobi.lab.componentsdemo.R
import mobi.lab.componentsdemo.common.BaseFragment
import mobi.lab.componentsdemo.common.FragmentBindingHolder
import mobi.lab.componentsdemo.common.ViewBindingHolder
import mobi.lab.componentsdemo.common.dialog.ConfirmationDialogFragment
import mobi.lab.componentsdemo.common.dialog.ProgressDialogFragment
import mobi.lab.componentsdemo.common.util.DialogUtil
import mobi.lab.componentsdemo.common.util.formatErrorCode
import mobi.lab.componentsdemo.databinding.FragmentLoginBinding
import mobi.lab.componentsdemo.di.Injector
import mobi.lab.componentsdemo.domain.entities.ErrorCode
import mobi.lab.componentsdemo.main.MainActivity
import mobi.lab.componentsdemo.mvvm.assistedSavedStateViewModels
import javax.inject.Inject

class LoginFragment : BaseFragment(), ViewBindingHolder<FragmentLoginBinding> by FragmentBindingHolder() {

    @Inject lateinit var factory: LoginViewModel.Factory

    /**
     * Create the [LoginViewModel] via [LoginViewModel.Factory] by providing the [androidx.lifecycle.SavedStateHandle].
     * assistedSavedStateViewModels wraps a call to [androidx.lifecycle.AbstractSavedStateViewModelFactory] with this Fragment's
     * context and using the factory method here to create the actual ViewModel.
     *
     * Optionally, the assistedSavedStateViewModels takes a second argument in the form () -> Bundle?.
     * This can be used to provide an initial state for the [androidx.lifecycle.SavedStateHandle]
     * and is passed to [androidx.lifecycle.AbstractSavedStateViewModelFactory].
     */
    private val viewModel: LoginViewModel by assistedSavedStateViewModels { handle ->
        factory.create(handle)
    }

    init {
        Injector.inject(this)
    }

    override fun getLifecycleOwner(): LifecycleOwner {
        return this
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return createBinding(FragmentLoginBinding.inflate(inflater), this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireBinding {
            buttonLogin.setOnClickListener { viewModel.onLoginClicked(getEmailString(), getPasswordString()) }
        }

        /**
         * Init ViewModel in onViewCreated as they are connected to View's lifecycle.
         */
        initViewModel()
    }

    private fun initViewModel() {
        viewModel.action.onEachEvent { action ->
            if (action == LoginViewModel.Action.OpenApplication) {
                activity?.let {
                    startActivity(MainActivity.getIntent(it))
                    it.finishAffinity()
                }
            }
        }
        viewModel.state.onEachNotNull { state ->
            when (state) {
                LoginViewModel.State.Default -> {
                    clearErrors()
                    hideProgress()
                }
                LoginViewModel.State.Progress -> {
                    clearErrors()
                    showProgress()
                }
                is LoginViewModel.State.Error -> {
                    hideProgress()
                    if (state.errorCode == ErrorCode.LOCAL_INVALID_CREDENTIALS) {
                        showInputErrors()
                    } else {
                        showLoginError(state.errorCode)
                    }
                }
            }
        }
    }

    private fun showInputErrors() {
        val binding = requireBinding()
        if (TextUtils.isEmpty(getEmailString())) {
            binding.inputLayoutEmail.error = getString(R.string.text_required)
        }
        if (TextUtils.isEmpty(getPasswordString())) {
            binding.inputLayoutPassword.error = getString(R.string.text_required)
        }
    }

    private fun showLoginError(error: ErrorCode) {
        DialogUtil.show(
            this,
            ConfirmationDialogFragment.newInstance(
                title = getString(R.string.title_error),
                message = formatErrorCode(context, error),
                positiveButton = getString(android.R.string.ok)
            ),
            TAG_DIALOG_ERROR
        )
    }

    private fun clearErrors() {
        requireBinding {
            inputLayoutEmail.error = null
            inputLayoutPassword.error = null
        }
    }

    private fun getEmailString(): String {
        return requireBinding().editTextEmail.text.toString()
    }

    private fun getPasswordString(): String {
        return requireBinding().editTextPassword.text.toString()
    }

    private fun showProgress() {
        if (isProgressShown()) {
            // Already there
            return
        }
        DialogUtil.show(this, ProgressDialogFragment.newInstance(), TAG_DIALOG_PROGRESS)
    }

    private fun hideProgress() {
        if (!isProgressShown()) {
            // Already there
            return
        }
        DialogUtil.dismiss(this, TAG_DIALOG_PROGRESS)
    }

    private fun isProgressShown(): Boolean {
        return DialogUtil.isShowing(activity, TAG_DIALOG_PROGRESS)
    }

    companion object {
        public const val TAG_DIALOG_PROGRESS = "login.TAG_DIALOG_PROGRESS"
        public const val TAG_DIALOG_ERROR = "login.TAG_DIALOG_ERROR"

        fun newInstance(): LoginFragment {
            return LoginFragment()
        }
    }
}
