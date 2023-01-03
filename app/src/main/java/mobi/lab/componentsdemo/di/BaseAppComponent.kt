package mobi.lab.componentsdemo.di

import mobi.lab.componentsdemo.App
import mobi.lab.componentsdemo.login.LoginFragment
import mobi.lab.componentsdemo.main.MainFragment
import mobi.lab.componentsdemo.prototype.PrototypeActivity
import mobi.lab.componentsdemo.splash.SplashActivity

interface BaseAppComponent {
    fun inject(target: App)
    fun inject(target: SplashActivity)
    fun inject(target: PrototypeActivity)
    fun inject(target: MainFragment)
    fun inject(target: LoginFragment)
}
