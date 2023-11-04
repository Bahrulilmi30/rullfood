package com.catnip.rullfood.di

import com.catnip.rullfood.data.local.database.AppDatabase
import com.catnip.rullfood.data.local.database.datasource.CartDataSource
import com.catnip.rullfood.data.local.database.datasource.CartDatabaseDataSource
import com.catnip.rullfood.data.local.datastore.UserPreferenceDataSource
import com.catnip.rullfood.data.local.datastore.UserPreferenceDataSourceImpl
import com.catnip.rullfood.data.local.datastore.appDataStore
import com.catnip.rullfood.data.network.api.datasource.RestaurantDataSource
import com.catnip.rullfood.data.network.api.datasource.RestaurantDataSourceImpl
import com.catnip.rullfood.data.network.api.service.RestaurantService
import com.catnip.rullfood.data.network.firebase.auth.FirebaseAuthDataSource
import com.catnip.rullfood.data.network.firebase.auth.FirebaseAuthDataSourceImpl
import com.catnip.rullfood.data.repository.CartRepository
import com.catnip.rullfood.data.repository.CartRepositoryImpl
import com.catnip.rullfood.data.repository.MenuRepository
import com.catnip.rullfood.data.repository.MenuRepositoryImpl
import com.catnip.rullfood.data.repository.UserRepository
import com.catnip.rullfood.data.repository.UserRepositoryImpl
import com.catnip.rullfood.presentation.cart.CartViewModel
import com.catnip.rullfood.presentation.changepforile.ProfileViewModel1
import com.catnip.rullfood.presentation.checkout.CheckoutViewModel
import com.catnip.rullfood.presentation.details.DetailViewModel
import com.catnip.rullfood.presentation.home.HomeViewModel
import com.catnip.rullfood.presentation.login.LoginViewModel
import com.catnip.rullfood.presentation.profile.ProfileViewModel
import com.catnip.rullfood.presentation.register.RegisterViewModel
import com.catnip.rullfood.presentation.splashscreen.SplashViewModel
import com.catnip.rullfood.utils.PreferenceDataStoreHelper
import com.catnip.rullfood.utils.PreferenceDataStoreHelperImpl
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.google.firebase.auth.FirebaseAuth
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

object AppModules {
    private val localModule = module {
        single { AppDatabase.getInstance(androidContext()) }
        single { get<AppDatabase>().cartDao() }
        single { androidContext().appDataStore }
        single<PreferenceDataStoreHelper> { PreferenceDataStoreHelperImpl(get()) }
    }

    private val networkModule = module {
        single<ChuckerInterceptor> { ChuckerInterceptor(androidContext()) }
        single<RestaurantService> { RestaurantService.invoke(get()) }
        single { FirebaseAuth.getInstance() }
    }

    private val dataSourceModule = module {
        single<CartDataSource> { CartDatabaseDataSource(get()) }
        single<UserPreferenceDataSource> { UserPreferenceDataSourceImpl(get()) }
        single<RestaurantDataSource> { RestaurantDataSourceImpl(get()) }
        single<FirebaseAuthDataSource> { FirebaseAuthDataSourceImpl(get()) }
    }

    private val repositoryModule = module {
        single<CartRepository> { CartRepositoryImpl(get(), get()) }
        single<MenuRepository> { MenuRepositoryImpl(get()) }
        single<UserRepository> { UserRepositoryImpl(get()) }
    }

    private val viewModelModule = module {
        viewModelOf(::HomeViewModel)
        viewModelOf(::CartViewModel)
        viewModelOf(::CheckoutViewModel)
        viewModelOf(::ProfileViewModel)
        viewModelOf(::LoginViewModel)
        viewModelOf(::RegisterViewModel)
        viewModelOf(::SplashViewModel)
        viewModelOf(::ProfileViewModel1)
        viewModel { params -> DetailViewModel(get(), params.get()) }
    }

    val modules = listOf(
        localModule,
        networkModule,
        dataSourceModule,
        repositoryModule,
        viewModelModule
    )
}
